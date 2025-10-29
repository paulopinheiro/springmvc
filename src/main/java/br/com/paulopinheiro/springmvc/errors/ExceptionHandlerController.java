package br.com.paulopinheiro.springmvc.errors;

import br.com.paulopinheiro.springmvc.model.UserModel;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class ExceptionHandlerController {
    @RequestMapping("/user/{email}")
    public String getUser(@PathVariable("email") String email, Model model) throws Exception {
        switch (email) {
            case "first.user@email.com" -> throw new UserNotFoundException(email);
            case "SQL" -> throw new SQLException("SQLException, email = " + email);
            case "IO" -> throw new IOException("IOException, email = " + email);
            case "john.doe@email.com" -> {
                UserModel user = new UserModel();
                user.setFirstName("John");
                user.setLastName("Doe");
                user.setEmail("john.doe@email.com");
                model.addAttribute("user", user);
                return "printUser";
            }
            default -> throw new Exception("Generic Exception, id = " + email);
        }
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException(HttpServletRequest request,
            Exception ex, Model model) {
        model.addAttribute("exception", ex);
        model.addAttribute("url", request.getRequestURL());
        return "error";
    }
}
