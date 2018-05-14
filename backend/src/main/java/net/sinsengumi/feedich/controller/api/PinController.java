package net.sinsengumi.feedich.controller.api;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.sinsengumi.feedich.controller.AbstractController;
import net.sinsengumi.feedich.model.FeedichOAuth2User;
import net.sinsengumi.feedich.model.Pin;
import net.sinsengumi.feedich.model.http.PinResponse;
import net.sinsengumi.feedich.service.PinService;

@RestController
@RequestMapping("api/pins")
@RequiredArgsConstructor
public class PinController extends AbstractController {

    private final PinService pinService;

    @GetMapping
    public List<PinResponse> pins(@AuthenticationPrincipal FeedichOAuth2User user) {
        return pinService.findByUserId(user.getId()).stream()
                .map(Pin::toResponse)
                .collect(Collectors.toList());
    }

    @PutMapping
    public PinResponse addPin(@RequestParam String title, @RequestParam String url, @AuthenticationPrincipal FeedichOAuth2User user) {
        Pin pin = new Pin();
        pin.setUserId(user.getId());
        pin.setTitle(title);
        pin.setUrl(url);
        pin.setCreatedAt(new Date());

        pinService.create(pin);
        return pin.toResponse();
    }

    @DeleteMapping("{pinId}")
    public String removePin(@PathVariable int pinId, @AuthenticationPrincipal FeedichOAuth2User user) {
        Pin pin = pinService.findById(pinId);
        authorizeResource(pin, user.getId());

        pinService.delete(pinId);
        return "OK";
    }

    @DeleteMapping
    public String clearPins(@AuthenticationPrincipal FeedichOAuth2User user) {
        pinService.clear(user.getId());
        return "OK";
    }
}
