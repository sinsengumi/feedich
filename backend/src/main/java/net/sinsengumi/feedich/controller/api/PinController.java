package net.sinsengumi.feedich.controller.api;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<PinResponse>> pins(@AuthenticationPrincipal FeedichOAuth2User user) {
        List<PinResponse> pins = pinService.findByUserId(user.getId()).stream()
            .map(Pin::toResponse)
            .collect(Collectors.toList());

        return ResponseEntity.ok(pins);
    }

    @PutMapping
    public ResponseEntity<PinResponse> addPin(@RequestParam String title, @RequestParam String url, @AuthenticationPrincipal FeedichOAuth2User user) {
        Pin alreadyExistPin = pinService.findByUserIdAndUrl(user.getId(), url);
        if (alreadyExistPin != null) {
            return new ResponseEntity<>(alreadyExistPin.toResponse(), HttpStatus.OK);
        }

        Date now = new Date();

        Pin pin = new Pin();
        pin.setUserId(user.getId());
        pin.setTitle(title);
        pin.setUrl(url);
        pin.setCreatedAt(now);
        pin.setUpdatedAt(now);

        pinService.create(pin);

        return new ResponseEntity<>(pin.toResponse(), HttpStatus.CREATED);
    }

    @DeleteMapping("{pinId}")
    public ResponseEntity<Void> removePin(@PathVariable int pinId, @AuthenticationPrincipal FeedichOAuth2User user) {
        Pin pin = pinService.findById(pinId);
        authorizeResource(pin, user.getId());

        pinService.delete(pinId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> clearPins(@AuthenticationPrincipal FeedichOAuth2User user) {
        pinService.clear(user.getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
