package pl.ssh.frontservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.ssh.frontservice.service.ItemsService;

import java.util.UUID;

@Controller
@RequestMapping("/movies")
public class MovieController {
    private final ItemsService itemsService;

    public MovieController(ItemsService itemsService) {
        this.itemsService = itemsService;
    }

    @GetMapping
    public String getAllMovies(Model model) {
        model.addAttribute("movies", itemsService.getAllMovies());
        return "movies";
    }

    @GetMapping("/{id}")
    public String getMovie(Model model, @PathVariable UUID id) {
        model.addAttribute("movie", itemsService.getMovieById(id));
        return "movie";
    }
}
