package net.sinsengumi.feedich.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import com.rometools.opml.feed.opml.Opml;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sinsengumi.feedich.model.Import;
import net.sinsengumi.feedich.model.Import.ImportStatus;
import net.sinsengumi.feedich.model.ImportFeed;
import net.sinsengumi.feedich.model.ImportFeed.ImportFeedStatus;
import net.sinsengumi.feedich.model.Subscription;
import net.sinsengumi.feedich.repository.ImportRepository;

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
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<Subscription> subscriptions = subscriptionService.findByUserId(userId);

        for (ImportFeed importFeed : importFeeds) {
            log.info("importFeed = {}", importFeed);
            int id = importFeed.getId();

            try {
                boolean alreadySubscribed = subscriptions.stream()
                        .anyMatch(s -> s.getFeed().getFeedUrl().equals(importFeed.getXmlUrl()));
                if (alreadySubscribed) {
                    log.info("importFeed {}. AlreadySubscribed.", id);
                    importFeed.setStatus(ImportFeedStatus.ALREADY_SUBSCRIBED);
                } else {
                    log.info("importFeed {}. Subscribe.", id);
                    subscriptionService.subscribeOtherTransaction(userId, importFeed.getXmlUrl());
                    importFeed.setStatus(ImportFeedStatus.SUCCESS);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                importFeed.setStatus(ImportFeedStatus.FAILED);
            }

            importFeedService.updateStatus(id, importFeed.getStatus());
        }

        ImportService service = applicationContext.getBean(ImportService.class);
        service.updateStatus(importFeeds.get(0).getImportId(), ImportStatus.FINISHED);

        stopWatch.stop();
        log.info("Import feeds. userId = {}, feeds = {}, elapsed = {} (ms)", userId, importFeeds.size(), stopWatch.getTotalTimeMillis());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int updateStatus(int id, ImportStatus status) {
        return importRepository.updateStatus(id, status);
    }

    public Import getLatestImport(int userId) {
        return importRepository.getLatestImport(userId);
    }
}
