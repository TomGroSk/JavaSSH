package pl.ssh.frontservice.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.ssh.frontservice.model.Customer;
import pl.ssh.frontservice.repository.CustomerRepository;
import pl.ssh.frontservice.validator.UserValidator;

import javax.validation.Valid;

@Controller
public class UserController {
    private UserValidator userValidator;
    private CustomerRepository userRepository;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(userValidator);
    }

    public UserController(CustomerRepository userRepository, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
    }

    @GetMapping("/register")
    public String getRegistration(Model model) {
        model.addAttribute("newUser", new Customer());
        return "register";
    }

    @PostMapping("/register")
    public String postRegister(@Valid @ModelAttribute("newUser") Customer customer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        String password = customer.getPassword();
        customer.setRole("USER");
        customer.setPassword(new BCryptPasswordEncoder().encode(password));
        userRepository.save(customer);
        return "redirect:/";
    }
}
