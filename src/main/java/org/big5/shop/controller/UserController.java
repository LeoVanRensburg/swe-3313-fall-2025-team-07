package org.big5.shop.controller;

import org.big5.shop.model.Database;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;


@Controller
public class UserController {
    // ==================================================================================
    // LOGIN
    // ==================================================================================
    @GetMapping("/login")
    public String loginForm(HttpSession session){
        if(session != null && session.getAttribute("userId") != null){
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        Optional <Database.User> userTemp = Database.authenticateUser(email, password);

        if (userTemp.isPresent()) {
            Database.User user = userTemp.get();

            session.setAttribute("userId", user.id);
            //admin role set in DB
            //session.setAttribute("isAdmin", user.isAdmin);

            return "redirect:/";
        } else {
            //invalid login Email or Password
            model.addAttribute("error", "Invalid Email or Password");
            model.addAttribute("email", email);
            return "login";
        }
    }
    // ==================================================================================
    // SIGNUP
    // ==================================================================================
    @GetMapping("/signup")
    public String signupForm(HttpSession session){
        if(session != null && session.getAttribute("userId") != null){
            return "redirect:/";
        }
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String email,
                         @RequestParam String password,
                         @RequestParam String confirmPassword,
                         Model model) {

        //if email already exists
        if (Database.findUserByEmail(email).isPresent()) {
            model.addAttribute("error", "Email already exists");
            model.addAttribute("email", email);
            return "login";
        }

        //if password doesn't match
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "signup";
        }

        Database.User newUser = Database.createUser(email, password, false);

        return "redirect:/";
    }
    // ==================================================================================
    // LOGOUT
    // ==================================================================================
    @PostMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }
}
