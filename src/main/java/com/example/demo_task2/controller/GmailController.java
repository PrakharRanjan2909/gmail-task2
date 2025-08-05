package com.example.demo_task2.controller;

import com.example.demo_task2.gmail.GmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class GmailController {

    @GetMapping("/emails")
    public List<Map<String, String>> getEmails() throws Exception {
        return GmailService.listEmails();
    }
}


