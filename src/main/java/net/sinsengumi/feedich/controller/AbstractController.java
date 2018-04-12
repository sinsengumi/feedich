package net.sinsengumi.feedich.controller;

public abstract class AbstractController {

    protected String redirect(String to) {
        return "redirect:" + to;
    }

    protected String forward(String to) {
        return "forward:" + to;
    }
}
