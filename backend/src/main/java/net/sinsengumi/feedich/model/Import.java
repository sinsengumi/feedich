package net.sinsengumi.feedich.model;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import net.sinsengumi.feedich.model.http.ImportResponse;

@Data
public class Import implements Authorizable {
    private int id;
    private int userId;
    private ImportStatus status = ImportStatus.RUNNING;
    private Date createdAt;
    private Date updatedAt;

    private List<ImportFeed> importFeeds;

    @Override
    public int getOwner() {
        return userId;
    }

    public ImportResponse toResponse() {
        ImportResponse response = new ImportResponse();
        response.setId(id);
        response.setStatus(status);
        response.setCreatedAt(createdAt);
        response.setUpdatedAt(updatedAt);
        response.setImportFeeds(importFeeds.stream().map(i -> i.toResponse()).collect(Collectors.toList()));
        return response;
    }

    public enum ImportStatus {
        RUNNING,
        FINISHED
    }
}
