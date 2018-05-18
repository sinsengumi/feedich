package net.sinsengumi.feedich.model.http;

import java.util.Date;
import java.util.List;

import lombok.Data;
import net.sinsengumi.feedich.model.Import.ImportStatus;
import net.sinsengumi.feedich.model.ImportFeed.ImportFeedStatus;

@Data
public class ImportResponse {

    private int id;
    private ImportStatus status;
    private Date createdAt;
    private Date updatedAt;
    private List<ImportFeedResponse> importFeeds;

    @Data
    public static class ImportFeedResponse {
        private String title;
        private String xmlUrl;
        private String htmlUrl;
        private ImportFeedStatus status;
    }
}
