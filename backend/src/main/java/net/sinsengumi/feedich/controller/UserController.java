package net.sinsengumi.feedich.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController extends AbstractController {

    public static int USER_ID = 1;

    @GetMapping("/user/switch")
    public String userSwitch(@RequestParam(defaultValue = "1", required = false) int userId) {
        USER_ID = userId;
        return redirect("/");
    }
}
