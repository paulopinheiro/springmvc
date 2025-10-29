package br.com.paulopinheiro.springmvc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class LoginController {
    @RequestMapping("/login_page")
    public String login() {
        return "login";
    }
}
