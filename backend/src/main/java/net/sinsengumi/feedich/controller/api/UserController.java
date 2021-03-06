package net.sinsengumi.feedich.controller.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.sinsengumi.feedich.controller.AbstractController;
import net.sinsengumi.feedich.model.FeedichOAuth2User;
import net.sinsengumi.feedich.service.UserService;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController extends AbstractController {

    private final UserService userService;

    @GetMapping("session/validate")
    public ResponseEntity<Void> validate() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("withdraw")
    public ResponseEntity<Void> withdraw(@AuthenticationPrincipal FeedichOAuth2User user) {
        userService.withdraw(user.getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
