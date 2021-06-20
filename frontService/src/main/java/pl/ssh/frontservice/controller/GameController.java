package pl.ssh.frontservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.ssh.frontservice.config.ProxyConfig;
import pl.ssh.frontservice.model.dto.PostGame;
import pl.ssh.frontservice.service.CustomerService;
import pl.ssh.frontservice.service.ItemsService;

import java.util.UUID;

@Controller
@RequestMapping("/games")
public class GameController {
    private final ItemsService itemsService;
    private final CustomerService customerService;

    public GameController(ItemsService itemsService, CustomerService customerService) {
        this.itemsService = itemsService;
        this.customerService = customerService;
    }

    @GetMapping
    public String getAllGames(Model model) {
        model.addAttribute("games", itemsService.getAllGames());
        return "games";
    }

    @GetMapping("/{id}")
    public String getGame(Authentication authentication, Model model, @PathVariable UUID id) {
        var game = itemsService.getGameById(id);

        var customer = customerService.getCustomerByUsername(authentication.getName());
        model.addAttribute("game", game);
        model.addAttribute("isAdmin", customerService.isAdmin(customer.getId()));
        model.addAttribute("comments", itemsService.getAllCommentsByItemId(id));

        if (authentication != null) {
            if (authentication.isAuthenticated()) {
                model.addAttribute("isInCustomerLibrary",
                        itemsService.getItem(
                                customer.getId(),
                                game.id,
                                ProxyConfig.GAMES) != null);
            }
        }

        return "game";
    }

    @GetMapping("/create")
    public String createGame(Model model) {
        model.addAttribute("game", new PostGame());
        return "addGame";
    }

    @PostMapping("/create")
    public String createGame(@ModelAttribute("game") PostGame postGame, Authentication authentication) {
        var customer = customerService.getCustomerByUsername(authentication.getName());
        if (!customerService.isAdmin(customer.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "You don't have permission to do this!"
            );
        }
        itemsService.createObject(itemsService.mapGame(postGame), ProxyConfig.GAMES);
        return "redirect:/games";
    }

    @PostMapping("/remove")
    public String removeGame(Authentication authentication, @ModelAttribute("itemId") String itemId) {
        var customer = customerService.getCustomerByUsername(authentication.getName());
        if (!customerService.isAdmin(customer.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "You don't have permission to do this!"
            );
        }
        itemsService.removeItem(ProxyConfig.GAMES, UUID.fromString(itemId));
        return "redirect:/games";
    }
}
