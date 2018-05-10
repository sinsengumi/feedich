package net.sinsengumi.feedich.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.sinsengumi.feedich.controller.AbstractController;

@RestController
@RequestMapping("api/session")
@RequiredArgsConstructor
public class SessionController extends AbstractController {

    @GetMapping("validate")
    public String validate() {
        return "OK";
    }
}
