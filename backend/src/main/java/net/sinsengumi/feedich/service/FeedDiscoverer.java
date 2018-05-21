package net.sinsengumi.feedich.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    private final static Pattern CDATA_PATTERN = Pattern.compile("<!\\[CDATA\\[(.*?)]]>", Pattern.DOTALL);

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
        log.info("Parse Feed.   url = {}", feedUrl);
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed syndFeed = null;
        try {
            syndFeed = input.build(new XmlReader(new URL(feedUrl)));
        } catch (FeedException e) {
            // リダイレクトを疑う
            feedUrl = HttpUtil.getFinalUrl(feedUrl);
            log.info("Try redirect. url = {}", feedUrl);
            try {
                syndFeed = input.build(new XmlReader(new URL(feedUrl)));
            } catch (FeedException e1) {
                // Unicode を疑う
                log.info("Try Unicode in CDATA... {}", feedUrl);
                try {
                    syndFeed = input.build(getTrimmedUnicodeXML(feedUrl));
                    log.info("Success Unicode in CDATA... {}", feedUrl);
                } catch (IllegalArgumentException | FeedException | IOException e2) {
                    log.info("Failed Unicode in CDATA... {}", feedUrl);
                    throw new ApplicationException("フィードの解析中にエラーが発生しました", e2);
                }
            } catch (IllegalArgumentException | IOException e1) {
                throw new ApplicationException("フィードの解析中にエラーが発生しました", e1);
            }
        } catch (IllegalArgumentException | IOException e) {
            throw new ApplicationException("フィードの解析中にエラーが発生しました", e);
        }

        if (syndFeed != null) {
            syndFeed.setUri(feedUrl);
        }

        patchAtom03(syndFeed);

        toReadableFeedType(syndFeed);

        return syndFeed;
    }

    private void toReadableFeedType(SyndFeed syndFeed) {
        String[] split = syndFeed.getFeedType().split("_");
        if (split.length == 2) {
            String type = split[0];
            String version = split[1];
            if (type.equalsIgnoreCase("atom")) {
                syndFeed.setFeedType("Atom " + version);
            } else {
                syndFeed.setFeedType(type.toUpperCase() + " " + version);
            }
        }
    }

    private void patchAtom03(SyndFeed syndFeed) {
        if (syndFeed.getFeedType().equals("atom_0.3")) {
            Matcher matcher = CDATA_PATTERN.matcher(syndFeed.getDescription());
            if (matcher.matches()) {
                String escapedDescription = matcher.replaceAll("$1");
                syndFeed.setDescription(escapedDescription);
            }
        }
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
        xml = xml.replaceAll("[\\00-\\x08\\x0a-\\x1f\\x7f]", ""); // 制御コード
        StringReader sreader = new StringReader(xml);
        return sreader;
    }
}
