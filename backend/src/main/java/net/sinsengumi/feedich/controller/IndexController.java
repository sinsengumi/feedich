package net.sinsengumi.feedich.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IndexController extends AbstractController {

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("userId", UserController.USER_ID);
        return "index";
    }

}
