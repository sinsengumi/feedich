package net.sinsengumi.feedich.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import net.sinsengumi.feedich.model.ImportFeed;
import net.sinsengumi.feedich.model.ImportFeed.ImportFeedStatus;

@Repository
public interface ImportFeedRepository {

    int create(@Param("importFeeds") List<ImportFeed> importFeeds);

    List<ImportFeed> findByImportId(int importId);

    int updateStatus(@Param("id") int id, @Param("status") ImportFeedStatus status);
}
