package pl.ssh.frontservice.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.ssh.frontservice.config.ProxyConfig;
import pl.ssh.frontservice.service.CustomerService;
import pl.ssh.frontservice.service.ItemsService;

import java.util.UUID;

@Controller
@RequestMapping("/books")
public class BookController {
    private final ItemsService itemsService;
    private final CustomerService customerService;

    public BookController(ItemsService itemsService, CustomerService customerService) {
        this.itemsService = itemsService;
        this.customerService = customerService;
    }

    @GetMapping
    public String getAllBooks(Model model) {
        model.addAttribute("books", itemsService.getAllBooks());
        return "books";
    }

    @GetMapping("/{id}")
    public String getBook(Authentication authentication, Model model, @PathVariable UUID id) {
        var book = itemsService.getBookById(id);
        model.addAttribute("book", book);

        if(authentication != null)
        {
            if(authentication.isAuthenticated()){
                model.addAttribute("isInCustomerLibrary",
                        itemsService.getItem(
                                customerService.getCustomerByUsername(authentication.getName()).getId(),
                                book.id,
                                ProxyConfig.BOOKS) != null);
            }
        }

        return "book";
    }
}
