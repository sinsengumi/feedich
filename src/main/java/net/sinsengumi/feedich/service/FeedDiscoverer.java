package net.sinsengumi.feedich.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import lombok.extern.slf4j.Slf4j;
import net.sinsengumi.feedich.exception.ApplicationException;
import net.sinsengumi.feedich.util.HttpUtil;

@Slf4j
@Service
public class FeedDiscoverer {

    public List<SyndFeed> discover(String url) {
        // 直接 feedUrl が指定された場合
        try {
            return Arrays.asList(parseFeed(url));
        } catch (Exception e) {
            log.info("Specified normal URL. url = {}" , url);
        }

        try {
            Document document = Jsoup.parse(new URL(url), 2000);
            Elements feedLinks = document.select("link[type~=rss|atom]");
            return feedLinks.stream().map(f -> {
                try {
                    String feedUrl = HttpUtil.resolveUrl(url, f.attr("href"));
                    return parseFeed(feedUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            })
            .filter(f -> f != null)
            .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SyndFeed parseFeed(String feedUrl) {
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed syndFeed = null;
        try {
            syndFeed = input.build(new XmlReader(new URL(feedUrl)));
        } catch (FeedException e) {
            // リダイレクトを疑う
            log.info("Try redirect url... {}", feedUrl);
            feedUrl = HttpUtil.getFinalUrl(feedUrl);
            try {
                syndFeed = input.build(new XmlReader(new URL(feedUrl)));
            } catch (FeedException e1) {
                // Unicode を疑う
                log.info("Try Unicode in CDATA... {}", feedUrl);
                try {
                    syndFeed = input.build(getTrimmedUnicodeXML(feedUrl));
                } catch (IllegalArgumentException | FeedException | IOException e2) {
                    throw new ApplicationException(e2);
                }
            } catch (IllegalArgumentException | IOException e1) {
                throw new ApplicationException(e1);
            }
        } catch (IllegalArgumentException | IOException e) {
            throw new ApplicationException(e);
        }

        return syndFeed;
    }

    private StringReader getTrimmedUnicodeXML(String rss) throws IOException {
        URL url = new URL(rss);
        HttpURLConnection urlconn = (HttpURLConnection) url.openConnection();
        urlconn.setRequestProperty("Accept-Charset", "UTF-8");
        urlconn.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlconn.getInputStream(), "UTF-8"));
        String xml = "";
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            xml += line;
        }
        reader.close();
        urlconn.disconnect();
        xml = xml.replaceAll("[\\00-\\x08\\x0a-\\x1f\\x7f]", "");
        StringReader sreader = new StringReader(xml);
        return sreader;
    }
}
