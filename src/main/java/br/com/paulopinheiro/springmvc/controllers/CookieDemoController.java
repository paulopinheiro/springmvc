package br.com.paulopinheiro.springmvc.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class CookieDemoController {

    @RequestMapping("/set-cookie")
    public String setCookie(HttpServletResponse response) {
        System.out.println("Putting test_cookie");
        response.addCookie(new Cookie("test_cookie", "test_value"));

        return "cookieDemo";
    }

    @RequestMapping("/extract-cookie")
    public String extractCookie(@CookieValue(value = "test_cookie", defaultValue = "null")String testCookie) {
        System.out.println("Cookie value: " + testCookie);
        return "cookieDemo";
    }
}
