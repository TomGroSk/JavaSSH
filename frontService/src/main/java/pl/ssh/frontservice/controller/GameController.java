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
        model.addAttribute("game", game);

        if(authentication != null)
        {
            if(authentication.isAuthenticated()){
                model.addAttribute("isInCustomerLibrary",
                        itemsService.getItem(
                                customerService.getCustomerByUsername(authentication.getName()).getId(),
                                game.id,
                                ProxyConfig.BOOKS) != null);
            }
        }

        return "game";
    }
}
