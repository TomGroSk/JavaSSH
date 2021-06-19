package pl.ssh.frontservice.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.ssh.frontservice.service.ItemsService;

@Controller
@RequestMapping("/comment")
public class CommentController {
    private final ItemsService itemsService;

    public CommentController(ItemsService itemsService) {
        this.itemsService = itemsService;
    }

    @PostMapping("/create")
    public String postComment(@ModelAttribute("itemId") String itemId, @ModelAttribute("content") String content, @ModelAttribute("itemType") String itemType, Authentication authentication) {
        var author = authentication.getName();
        itemsService.createComment(itemId, author, content);
        return "redirect:/" + itemType + "/" + itemId;
    }
}
