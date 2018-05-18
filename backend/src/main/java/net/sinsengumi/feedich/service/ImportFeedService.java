package net.sinsengumi.feedich.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import net.sinsengumi.feedich.model.ImportFeed;
import net.sinsengumi.feedich.model.ImportFeed.ImportFeedStatus;
import net.sinsengumi.feedich.repository.ImportFeedRepository;

@Service
@AllArgsConstructor
public class ImportFeedService {

    private final ImportFeedRepository importFeedRepository;

    public int create(List<ImportFeed> importFeeds) {
        return importFeedRepository.create(importFeeds);
    }

    public List<ImportFeed> findByImportId(int importId) {
        return importFeedRepository.findByImportId(importId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int updateStatus(int id, ImportFeedStatus status) {
        return importFeedRepository.updateStatus(id, status);
    }
}
