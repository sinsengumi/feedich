package net.sinsengumi.feedich.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sinsengumi.feedich.exception.ApplicationException;

@Slf4j
public final class HttpUtil {

    private HttpUtil() {
    }

    public static String getRemoteAddr(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (StringUtils.isEmpty(xForwardedFor)) {
            return request.getRemoteAddr();
        } else {
            return xForwardedFor.split(",")[0];
        }
    }

    public static String getFinalUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            return url;
        }

        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("HEAD");
            con.setInstanceFollowRedirects(false);
            con.connect();

            if (con.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM || con.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
                String redirectUrl = con.getHeaderField("Location");
                return getFinalUrl(redirectUrl);
            }

            return con.getResponseCode() == HttpURLConnection.HTTP_OK ? url : null;
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
            return null;
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    public static String resolveUrl(URL accessUrl, String relativeOrAbsolutePath) {
        try {
            String path = accessUrl.getPath();
            String baseUrl = accessUrl.getProtocol() + "://" + accessUrl.getHost() + "/";
            String accessUrlBase = baseUrl + path.substring(0, path.lastIndexOf("/") + 1);

            if (relativeOrAbsolutePath.matches("^https?://.*")) {
                // URL
                return relativeOrAbsolutePath;
            } else if (relativeOrAbsolutePath.startsWith("//")) {
                // 絶対 URL
                return new URI(accessUrl.getProtocol() + ":" + relativeOrAbsolutePath).normalize().toString();
            } else if (relativeOrAbsolutePath.startsWith("/")) {
                // 絶対パス
                return new URI(baseUrl + relativeOrAbsolutePath).normalize().toString();
            } else {
                // 相対パス
                return new URI(accessUrlBase + relativeOrAbsolutePath).normalize().toString();
            }
        } catch (URISyntaxException e) {
            throw new ApplicationException("URL を解決できませんでした", e);
        }
    }

    public static HtmlMeta extractHtmlMeta(String urlStr) {
        HtmlMeta htmlMeta = new HtmlMeta();
        try {
            URL url = new URL(urlStr);
            Document document = Jsoup.parse(url, 10 * 1000);

            Element favicon = document.select("link[rel~=(icon|shortcut icon)]").first();
            htmlMeta.setFavicon(extractFavicon(favicon, url));

            Element ogImage = document.select("meta[property~=og:image]").first();
            htmlMeta.setOgImage(extractOgImage(ogImage, url));
            return htmlMeta;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return htmlMeta;
        }
    }

    public static String extractFavicon(Element element, URL accessUrl) {
        try {
            String faviconUrl = null;
            if (element == null) {
                // element がない場合は、/favicon.ico にチャレンジする。
                faviconUrl = accessUrl.getProtocol() + "://" + accessUrl.getHost() + "/favicon.ico";
            } else {
                faviconUrl = resolveUrl(accessUrl, element.attr("href"));
            }
            return getFinalUrl(faviconUrl);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return null;
        }
    }

    public static String extractOgImage(Element element, URL accessUrl) {
        try {
            if (element == null) {
                return null;
            } else {
                String ogImageUrl = resolveUrl(accessUrl, element.attr("content"));
                return getFinalUrl(ogImageUrl);
            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return null;
        }
    }

    @Data
    public static class HtmlMeta {
        private String favicon;
        private String ogImage;
    }
}
