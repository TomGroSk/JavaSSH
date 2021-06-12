package pl.ssh.frontservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.ssh.frontservice.service.ItemsService;

import java.util.UUID;

@Controller
@RequestMapping("/games")
public class GameController {
    private final ItemsService itemsService;

    public GameController(ItemsService itemsService) {
        this.itemsService = itemsService;
    }

    @GetMapping
    public String getAllGames(Model model) {
        model.addAttribute("games", itemsService.getAllGames());
        return "games";
    }

    @GetMapping("/{id}")
    public String getGame(Model model, @PathVariable UUID id) {
        model.addAttribute("game", itemsService.getGameById(id));
        return "game";
    }
}
