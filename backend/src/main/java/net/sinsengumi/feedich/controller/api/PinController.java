package net.sinsengumi.feedich.controller.api;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.sinsengumi.feedich.controller.AbstractController;
import net.sinsengumi.feedich.controller.UserController;
import net.sinsengumi.feedich.model.Pin;
import net.sinsengumi.feedich.model.http.PinResponse;
import net.sinsengumi.feedich.service.PinService;

@RestController
@RequestMapping("api/pins")
@RequiredArgsConstructor
public class PinController extends AbstractController {

    private final PinService pinService;

    @PutMapping
    public PinResponse addPin(@RequestParam String title, String url) {
        Pin pin = new Pin();
        pin.setUserId(UserController.USER_ID);
        pin.setTitle(title);
        pin.setUrl(url);

        pinService.create(pin);
        return pin.toResponse();
    }

    @DeleteMapping("{pinId}")
    public String removePin(@PathVariable int pinId) {
        Pin pin = pinService.findById(pinId);
        authorizeResource(pin, UserController.USER_ID);

        pinService.delete(pinId);
        return "OK";
    }
}
