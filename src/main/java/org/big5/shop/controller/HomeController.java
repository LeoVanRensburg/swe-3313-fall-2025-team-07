package org.big5.shop.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(HttpSession session) {
        if (session == null || session.getAttribute("userId") == null){
            return "redirect:/login";
        }
        return "index";
    }
}
