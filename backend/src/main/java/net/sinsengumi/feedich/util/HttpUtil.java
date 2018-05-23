package net.sinsengumi.feedich.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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
            return url;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    public static String extractFavicon(String urlStr) {
        String faviconUrl = null;
        try {
            URL url = new URL(urlStr);
            Document document = Jsoup.parse(url, 10 * 1000);
            Element favicon = document.select("link[rel~=(icon|shortcut icon)]").first();
            if (favicon != null) {
                faviconUrl = resolveUrl(urlStr, favicon.attr("href"));
            } else {
                String candidateFaviconUrl = url.getProtocol() + "://" + url.getHost() + "/favicon.ico";
                if (exists(candidateFaviconUrl)) {
                    faviconUrl = candidateFaviconUrl;
                }
            }
            return getFinalUrl(faviconUrl);
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
            return faviconUrl;
        }
    }

    public static boolean exists(String url) {
        HttpURLConnection con = null;
        try {
            String finalUrl = getFinalUrl(url);
            con = (HttpURLConnection) new URL(finalUrl).openConnection();
            con.setRequestMethod("HEAD");
            con.connect();
            return con.getResponseCode() == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            return false;
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    public static String resolveUrl(String accessUrl, String relativeOrAbsolutePath) {
        try {
            URL url = new URL(accessUrl);
            String path = url.getPath();
            String baseUrl = url.getProtocol() + "://" + url.getHost() + "/";
            String accessUrlBase = baseUrl + path.substring(0, path.lastIndexOf("/") + 1);

            if (relativeOrAbsolutePath.matches("^https?://.*")) {
                // URL
                return relativeOrAbsolutePath;
            } else if (relativeOrAbsolutePath.startsWith("//")) {
                // 絶対 URL
                return new URI(url.getProtocol() + ":" + relativeOrAbsolutePath).normalize().toString();
            } else if (relativeOrAbsolutePath.startsWith("/")) {
                // 絶対パス
                return new URI(baseUrl + relativeOrAbsolutePath).normalize().toString();
            } else {
                // 相対パス
                return new URI(accessUrlBase + relativeOrAbsolutePath).normalize().toString();
            }
        } catch (MalformedURLException | URISyntaxException e) {
            throw new ApplicationException("URL を解決できませんでした", e);
        }
    }
}
