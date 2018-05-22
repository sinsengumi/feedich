package net.sinsengumi.feedich.controller.api;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.sinsengumi.feedich.controller.AbstractController;
import net.sinsengumi.feedich.service.SlackService;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class ContactController extends AbstractController {

    private final SlackService slackService;

    @PostMapping("contact")
    public ResponseEntity<Void> contact(@Validated ContactForm form, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        slackService.contact(form.getMessage() + System.lineSeparator() + System.lineSeparator() + "by " + form.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Data
    public static class ContactForm {
        @NotEmpty
        @Email
        private String email;

        @NotEmpty
        @Size(min = 1, max = 4000)
        private String message;
    }
}
