package pl.ssh.frontservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.ssh.frontservice.config.ProxyConfig;
import pl.ssh.frontservice.model.dto.Book;
import pl.ssh.frontservice.model.dto.PostBook;
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
        var customer = customerService.getCustomerByUsername(authentication.getName());

        model.addAttribute("book", book);
        model.addAttribute("isAdmin", customerService.isAdmin(customer.getId()));
        model.addAttribute("comments", itemsService.getAllCommentsByItemId(id));

        if (authentication != null) {
            if (authentication.isAuthenticated()) {
                model.addAttribute("isInCustomerLibrary",
                        itemsService.getItem(
                                customer.getId(),
                                book.id,
                                ProxyConfig.BOOKS) != null);
            }
        }

        return "book";
    }

    @GetMapping("/create")
    public String createBook(Model model) {
        model.addAttribute("book", new PostBook());
        return "addBook";
    }

    @PostMapping("/create")
    public String createBook(@ModelAttribute("book") PostBook postBook, Authentication authentication) {
        var customer = customerService.getCustomerByUsername(authentication.getName());
        if (!customerService.isAdmin(customer.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "You don't have permission to do this!"
            );
        }
        itemsService.createObject(itemsService.mapBook(postBook), ProxyConfig.BOOKS);
        return "redirect:/books";
    }

    @PostMapping("/remove")
    public String removeBook(Authentication authentication, @ModelAttribute("itemId") String itemId) {

        var customer = customerService.getCustomerByUsername(authentication.getName());
        if (!customerService.isAdmin(customer.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "You don't have permission to do this!"
            );
        }
        itemsService.removeItem(ProxyConfig.BOOKS, UUID.fromString(itemId));
        return "redirect:/books";
    }
}
