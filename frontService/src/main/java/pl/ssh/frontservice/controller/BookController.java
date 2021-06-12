package pl.ssh.frontservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.ssh.frontservice.service.ItemsService;

import java.util.UUID;

@Controller
@RequestMapping("/books")
public class BookController {
    private final ItemsService itemsService;

    public BookController(ItemsService itemsService) {
        this.itemsService = itemsService;
    }

    @GetMapping
    public String getAllBooks(Model model) {
        model.addAttribute("books", itemsService.getAllBooks());
        return "books";
    }

    @GetMapping("/{id}")
    public String getBook(Model model, @PathVariable UUID id) {
        model.addAttribute("book", itemsService.getBookById(id));
        return "book";
    }
}
