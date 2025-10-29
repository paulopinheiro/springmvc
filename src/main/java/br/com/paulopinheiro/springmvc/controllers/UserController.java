package br.com.paulopinheiro.springmvc.controllers;

import br.com.paulopinheiro.springmvc.model.UserModel;
import br.com.paulopinheiro.springmvc.model.UserModelValid;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class UserController {
    @RequestMapping("/user-registration-form")
    public String userReegistrationForm(Model model) {
        model.addAttribute("user", new UserModel());
        return "userRegistrationForm";
    }

    @RequestMapping("/create-user")
    public String createUser(@ModelAttribute("user") UserModel u) {
        System.out.println(u);
        return "registrationConfirmation";
    }

    @RequestMapping("/user-registration-form-with-validation")
    public String userRegistrationFormWithValidation(Model model) {
        model.addAttribute("user", new UserModelValid());
        return "userRegistrationFormWithValidation";
    }

    @RequestMapping("/create-user-with-validation")
    public String createUserWithValidation(@Valid @ModelAttribute("user") UserModelValid u,
            BindingResult br) {
        System.out.println("Are there any data binding errors? " + br.hasErrors());

//		// ================= EXAMPLE of BindingResult API ====================
//		
//		List<FieldError> fieldErrors = br.getFieldErrors();
//		for (FieldError fieldError : fieldErrors) {
//			System.out.println(fieldError.getDefaultMessage());
//		}
//		
//		// ================= EXAMPLE of Programmatic way to validate object ====================
//		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//		Validator validator = factory.getValidator();
//		Set<ConstraintViolation<UserModelValid>> violations = validator.validate(u);
//		for (ConstraintViolation<UserModelValid> constraintViolation : violations) {
//			System.out.println(constraintViolation.getMessage());
//		}
        if (br.hasErrors()) {
            return "userRegistrationFormWithValidation";
        } else {
            return "registrationConfirmation";
        }
    }
}
