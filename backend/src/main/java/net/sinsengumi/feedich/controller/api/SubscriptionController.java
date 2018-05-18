package net.sinsengumi.feedich.controller.api;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rometools.opml.feed.opml.Opml;
import com.rometools.rome.io.FeedException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sinsengumi.feedich.controller.AbstractController;
import net.sinsengumi.feedich.exception.InvalidOpmlException;
import net.sinsengumi.feedich.model.Feed;
import net.sinsengumi.feedich.model.FeedichOAuth2User;
import net.sinsengumi.feedich.model.Import;
import net.sinsengumi.feedich.model.ImportFeed;
import net.sinsengumi.feedich.model.Subscription;
import net.sinsengumi.feedich.model.UserItem;
import net.sinsengumi.feedich.model.http.ImportResponse;
import net.sinsengumi.feedich.model.http.SubscriptionResponse;
import net.sinsengumi.feedich.model.http.UserItemResponse;
import net.sinsengumi.feedich.service.FeedOpmlService;
import net.sinsengumi.feedich.service.ImportFeedService;
import net.sinsengumi.feedich.service.ImportService;
import net.sinsengumi.feedich.service.SubscriptionService;
import net.sinsengumi.feedich.service.UserItemService;
import net.sinsengumi.feedich.util.SpringUtil;

@Slf4j
@RestController
@RequestMapping("api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController extends AbstractController {

    private final SubscriptionService subscriptionService;
    private final UserItemService userItemService;
    private final FeedOpmlService feedOpmlService;
    private final ImportService importService;
    private final ImportFeedService importFeedService;

    @GetMapping
    public ResponseEntity<List<SubscriptionResponse>> subscriptions(@AuthenticationPrincipal FeedichOAuth2User user) {
        List<SubscriptionResponse> result = subscriptionService.findByUserId(user.getId()).stream()
                .map(Subscription::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("{subscriptionId}/items")
    public ResponseEntity<List<UserItemResponse>> items(@PathVariable int subscriptionId, @AuthenticationPrincipal FeedichOAuth2User user) {
        Subscription subscription = subscriptionService.findById(subscriptionId);
        authorizeResource(subscription, user.getId());

        List<UserItemResponse> result = userItemService.findUnreadItems(user.getId(), subscription.getFeedId()).stream()
                .map(UserItem::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @PutMapping
    public ResponseEntity<SubscriptionResponse> subscribe(@RequestParam String feedUrl, @AuthenticationPrincipal FeedichOAuth2User user) {
        SubscriptionResponse result = subscriptionService.subscribe(user.getId(), feedUrl).toResponse();
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping("{subscriptionId}")
    public ResponseEntity<Void> unsubscribe(@PathVariable int subscriptionId, @AuthenticationPrincipal FeedichOAuth2User user) {
        Subscription subscription = subscriptionService.findById(subscriptionId);
        authorizeResource(subscription, user.getId());

        subscriptionService.unsubscribe(subscriptionId, user.getId(), subscription.getFeedId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("import")
    public ResponseEntity<ImportResponse> importOpml(@RequestParam(required = false) MultipartFile importOpml, @AuthenticationPrincipal FeedichOAuth2User user) {
        if (SpringUtil.isEmptyMultipartFile(importOpml)) {
            throw new InvalidOpmlException("OPML ファイルが空です");
        }

        try {
            Opml opml = feedOpmlService.importOpml(importOpml);
            if (opml.getOutlines().isEmpty()) {
                throw new InvalidOpmlException("OPML ファイルのエントリーがありません");
            }

            Date now = new Date();
            Import import_ = new Import();
            import_.setUserId(user.getId());
            import_.setCreatedAt(now);
            import_.setUpdatedAt(now);

            importService.create(import_, opml);

            List<ImportFeed> importFeedsTmp = importFeedService.findByImportId(import_.getId());
            import_.setImportFeeds(importFeedsTmp);

            importService.importFeeds(user.getId(), importFeedsTmp);

            return new ResponseEntity<>(import_.toResponse(), HttpStatus.ACCEPTED);
        } catch (IllegalArgumentException | FeedException | IOException e) {
            throw new InvalidOpmlException("不正な OPML ファイルです", e);
        }
    }

    @GetMapping("export")
    public ResponseEntity<byte[]> exportOpml(@AuthenticationPrincipal FeedichOAuth2User user) {
        List<Feed> feeds = subscriptionService.findByUserId(user.getId()).stream()
                .map(s -> s.getFeed())
                .collect(Collectors.toList());
        String opml = feedOpmlService.exportOpml(feeds);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/xml; charset=UTF-8");
        headers.setContentDispositionFormData("filename", "feedich_subscriptions.xml");
        return new ResponseEntity<>(opml.getBytes(StandardCharsets.UTF_8), headers, HttpStatus.OK);
    }

    @GetMapping("latestImport")
    public ResponseEntity<ImportResponse> latestImport(@AuthenticationPrincipal FeedichOAuth2User user) {
        Import latestImport = importService.getLatestImport(user.getId());
        return latestImport == null ? ResponseEntity.ok(null) : ResponseEntity.ok(latestImport.toResponse());
    }
}
