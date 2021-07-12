package net.deali.intern.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/advertise")
    public String advertise() {
        return "advertise";
    }
}
