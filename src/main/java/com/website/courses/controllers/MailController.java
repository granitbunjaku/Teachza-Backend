package com.website.courses.controllers;

import com.website.courses.requests.MailRequest;
import com.website.courses.services.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MailController {
    private final EmailSenderService emailSenderService;

    @PostMapping("mail")
    public void SendMail(@RequestBody MailRequest request)
    {
        emailSenderService.sendEmail(request.getToEmail(), request.getSubject(), request.getBody());
    }
}
