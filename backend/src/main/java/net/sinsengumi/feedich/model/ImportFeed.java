package net.sinsengumi.feedich.model;

import org.apache.commons.lang3.StringUtils;

import com.rometools.opml.feed.opml.Outline;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.sinsengumi.feedich.model.http.ImportResponse.ImportFeedResponse;

@Data
@ToString(of = { "id", "title", "xmlUrl" })
@NoArgsConstructor
public class ImportFeed {
    private int id;
    private int importId;
    private String title;
    private String xmlUrl;
    private String htmlUrl;
    private ImportFeedStatus status = ImportFeedStatus.QUEUED;

    public ImportFeedResponse toResponse() {
        ImportFeedResponse response = new ImportFeedResponse();
        response.setTitle(title);
        response.setXmlUrl(xmlUrl);
        response.setHtmlUrl(htmlUrl);
        response.setStatus(status);
        return response;
    }

    public static ImportFeed buildOutline(Outline outline, int importId) {
        ImportFeed importFeed = new ImportFeed();
        importFeed.setImportId(importId);
        String title = StringUtils.isEmpty(outline.getTitle()) ? outline.getText() : outline.getTitle();
        importFeed.setTitle(StringUtils.trim(title));
        importFeed.setXmlUrl(StringUtils.trim(outline.getXmlUrl()));
        importFeed.setHtmlUrl(StringUtils.trim(outline.getHtmlUrl()));
        return importFeed;
    }

    public enum ImportFeedStatus {
        QUEUED,
        SUCCESS,
        FAILED,
        ALREADY_SUBSCRIBED
    }
}
