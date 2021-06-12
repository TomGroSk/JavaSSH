package pl.ssh.frontservice.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.ssh.frontservice.service.CustomerService;
import pl.ssh.frontservice.service.ItemsService;

@Controller
@RequestMapping("/")
public class MainController {

    private final ItemsService itemsService;
    private final CustomerService customerService;

    public MainController(ItemsService itemsService, CustomerService customerService) {
        this.itemsService = itemsService;
        this.customerService = customerService;
    }

    @GetMapping
    public String getIndex(Authentication authentication, Model model) {
        if(authentication != null){
            if(authentication.isAuthenticated()){
                var itemResponse = itemsService.getAllItems(customerService.getCustomerByUsername(authentication.getName()).getId());
                model.addAttribute("items", itemResponse);
            }
        }

        return "index";
    }
}
