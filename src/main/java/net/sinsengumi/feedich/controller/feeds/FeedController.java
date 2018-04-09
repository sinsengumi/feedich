package net.sinsengumi.feedich.controller.feeds;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.sinsengumi.feedich.model.Feed;
import net.sinsengumi.feedich.model.Item;
import net.sinsengumi.feedich.service.FeedCrawlService;

@RestController
@RequestMapping("feeds")
@RequiredArgsConstructor
public class FeedController {

    private final FeedCrawlService feedCrawlService;

    @GetMapping("")
    public List<Feed> getFeeds() {
        return null;
    }

    @GetMapping("crawl")
    public List<Item> crawl() {
        feedCrawlService.crawl();
        // feeds を distinct して、crawl する
        // feed から item を取得して item テーブルに格納
        // （この時、重複チェックや最大保管数などを見て格納）
        // ユーザの未読を追加する
        
        // ユーザが未読を取ろうとする
        // -> 購読日以降に配信、かつ未読の item だけを表示する
        return null;
    }
}
