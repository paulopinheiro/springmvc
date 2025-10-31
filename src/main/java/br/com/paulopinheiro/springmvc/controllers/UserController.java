package br.com.paulopinheiro.springmvc.controllers;

import br.com.paulopinheiro.springmvc.model.UserModel;
import br.com.paulopinheiro.springmvc.model.UserModelValid;
import br.com.paulopinheiro.springmvc.persistence.entities.User;
import br.com.paulopinheiro.springmvc.persistence.repositories.RoleRepository;
import br.com.paulopinheiro.springmvc.persistence.repositories.UserRepository;
import br.com.paulopinheiro.springmvc.security.SetupDataLoader;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class UserController {
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private RoleRepository roleRepository;
    @Autowired private UserRepository userRepository;

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

    // ======= USER REGISTRATION for Spring Security Demo

    @RequestMapping("/user-registration-form-security-demo")
    public String userRegistrationSecurityDemo(Model model) {
        model.addAttribute("user", new UserModelValid());
        return "userRegistrationSecurityDemo";
    }

    @RequestMapping("/create-user-security-demo")
    public String registerNewUserAccount(@Valid @ModelAttribute("user") UserModel userModel, 
                                         BindingResult br) {
        User user = userRepository.findByEmail(userModel.getEmail());

        if (Optional.ofNullable(user).isPresent()) {
            throw new RuntimeException("User with such email already exists: " + userModel.getEmail());
        }

        user = new User();

        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        user.setEmail(userModel.getEmail());
        user.setEnabled(true);
        user.setRoles(Arrays.asList(roleRepository.findByName(SetupDataLoader.ROLE_USER)));
        userRepository.save(user);

        return "redirect:/test/login_page";
    }
}
