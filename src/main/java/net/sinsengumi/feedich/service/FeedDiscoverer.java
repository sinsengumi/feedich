package net.sinsengumi.feedich.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
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
import net.sinsengumi.feedich.model.Feed;

@Slf4j
@Service
public class FeedDiscoverer {

    public List<Feed> discover(String url) {
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
                    String feedUrl = resolveUrl(url, f.attr("href"));
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

    public Feed parseFeed(String feedUrl) {
        try {
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed syndFeed = input.build(new XmlReader(new URL(feedUrl)));
            Feed feed = new Feed();
            feed.setTitle(syndFeed.getTitle());
            feed.setDescription(syndFeed.getDescription());
            feed.setUrl(syndFeed.getLink());
            feed.setFeedUrl(feedUrl);
            feed.setFeedType(syndFeed.getFeedType());
            if (syndFeed.getIcon() != null) {
                feed.setIcon(syndFeed.getIcon().getUrl());
            }
            return feed;
        } catch (IllegalArgumentException | FeedException | IOException e) {
            throw new ApplicationException(e);
        }
    }

    private String resolveUrl(String accessUrl, String relativeOrAbsolutePath) {
        try {
            URL url = new URL(accessUrl);
            String path = url.getPath();
            String baseUrl = url.getProtocol() + "://" + url.getHost() + "/";
            String accessUrlBase = baseUrl + path.substring(0, path.lastIndexOf("/") + 1);

            if (relativeOrAbsolutePath.matches("^https?://.*")) {
                // URL
                return relativeOrAbsolutePath;
            } else if (relativeOrAbsolutePath.startsWith("/")) {
                // 絶対パス
                return new URI(baseUrl + relativeOrAbsolutePath).normalize().toString();
            } else {
                // 相対パス
                return new URI(accessUrlBase + relativeOrAbsolutePath).normalize().toString();
            }
        } catch (MalformedURLException | URISyntaxException e) {
            throw new ApplicationException(e);
        }
    }
}
