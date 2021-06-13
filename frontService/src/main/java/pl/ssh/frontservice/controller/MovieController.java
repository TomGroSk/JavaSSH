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
@RequestMapping("/movies")
public class MovieController {
    private final ItemsService itemsService;
    private final CustomerService customerService;

    public MovieController(ItemsService itemsService, CustomerService customerService) {
        this.itemsService = itemsService;
        this.customerService = customerService;
    }

    @GetMapping
    public String getAllMovies(Model model) {
        model.addAttribute("movies", itemsService.getAllMovies());
        return "movies";
    }

    @GetMapping("/{id}")
    public String getMovie(Authentication authentication, Model model, @PathVariable UUID id) {
        var movie = itemsService.getMovieById(id);
        model.addAttribute("movie", movie);

        if(authentication != null)
        {
            if(authentication.isAuthenticated()){
                model.addAttribute("isInCustomerLibrary",
                        itemsService.getItem(
                                customerService.getCustomerByUsername(authentication.getName()).getId(),
                                movie.id,
                                ProxyConfig.BOOKS) != null);
            }
        }

        return "movie";
    }
}
