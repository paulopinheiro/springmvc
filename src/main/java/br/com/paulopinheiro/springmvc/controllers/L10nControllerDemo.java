package br.com.paulopinheiro.springmvc.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class L10nControllerDemo {
    @Autowired
    private MessageSource messageSource;

    @GetMapping("/lang-demo") //Same as @RequestMapping(value="/lang-demo", method = RequestMethod.GET)
    public String langDemo() {
        return "langDemo";
    }

    @GetMapping("/message-source-demo")
    public String messageSourceDemo(HttpSession session) {
        String message = messageSource.getMessage("random.text", null, LocaleContextHolder.getLocale());
        session.setAttribute("testMsg", message);
        System.out.println(message);

        return "messageSourceDemo";
    }
}
