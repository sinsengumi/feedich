package net.sinsengumi.feedich.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.rometools.opml.feed.opml.Opml;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sinsengumi.feedich.model.Import;
import net.sinsengumi.feedich.model.Import.ImportStatus;
import net.sinsengumi.feedich.model.ImportFeed;
import net.sinsengumi.feedich.model.ImportFeed.ImportFeedStatus;
import net.sinsengumi.feedich.model.Subscription;
import net.sinsengumi.feedich.repository.ImportRepository;
import net.sinsengumi.feedich.util.StopWatchUtil;

@Slf4j
@Service
@AllArgsConstructor
public class ImportService {

    private final ImportRepository importRepository;
    private final ImportFeedService importFeedService;
    private final SubscriptionService subscriptionService;
    private final ApplicationContext applicationContext;

    public int create(Import import_, Opml opml) {
        int result = importRepository.create(import_);

        List<ImportFeed> importFeeds = opml.getOutlines().stream()
                .map(o -> ImportFeed.buildOutline(o, import_.getId()))
                .collect(Collectors.toList());

        importFeedService.create(importFeeds);
        return result;
    }

    @Async
    public void importFeeds(int userId, List<ImportFeed> importFeeds) {
        StopWatchUtil stopWatch = new StopWatchUtil();
        ImportService service = applicationContext.getBean(ImportService.class);

        List<Subscription> subscriptions = subscriptionService.findByUserId(userId);
        List<ImportFeed> failedFeeds = new ArrayList<>();

        for (ImportFeed importFeed : importFeeds) {
            boolean success = service.importFeed(userId, importFeed, subscriptions);
            if (!success) {
                failedFeeds.add(importFeed);
            }
        }

        if (!failedFeeds.isEmpty()) {
            log.error("Import failed. userId = {}, size = {}", userId, failedFeeds.size());
        }

        service.updateStatus(importFeeds.get(0).getImportId(), ImportStatus.FINISHED);

        log.info("Import feeds. userId = {}, feeds = {}, elapsed = {} (ms)", userId, importFeeds.size(), stopWatch.getTotalTimeMillis());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean importFeed(int userId, ImportFeed importFeed, List<Subscription> subscriptions) {
        log.info("importFeed = {}", importFeed);
        int id = importFeed.getId();

        try {
            boolean alreadySubscribed = subscriptions.stream()
                    .anyMatch(s -> s.getFeed().getFeedUrl().equals(importFeed.getXmlUrl()));
            if (alreadySubscribed) {
                log.info("importFeed {}. AlreadySubscribed.", id);

                importFeedService.updateStatus(id, ImportFeedStatus.ALREADY_SUBSCRIBED);
            } else {
                log.info("importFeed {}. Subscribe.", id);

                subscriptionService.subscribe(userId, importFeed.getXmlUrl());
                importFeedService.updateStatus(id, ImportFeedStatus.SUCCESS);
            }
            return true;
        } catch (Exception e) {
            log.warn("Failed Import {}.", id, e);

            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            importFeedService.updateStatus(id, ImportFeedStatus.FAILED);
            return false;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int updateStatus(int id, ImportStatus status) {
        return importRepository.updateStatus(id, status);
    }

    public Import getLatestImport(int userId) {
        return importRepository.getLatestImport(userId);
    }
}
