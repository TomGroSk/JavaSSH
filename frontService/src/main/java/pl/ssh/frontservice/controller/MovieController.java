package pl.ssh.frontservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.ssh.frontservice.config.ProxyConfig;
import pl.ssh.frontservice.service.CustomerService;
import pl.ssh.frontservice.service.ItemsService;

import javax.validation.Valid;
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
        var customer = customerService.getCustomerByUsername(authentication.getName());

        model.addAttribute("movie", movie);
        model.addAttribute("isAdmin", customerService.isAdmin(customer.getId()));
        model.addAttribute("comments", itemsService.getAllCommentsByItemId(id));

        if(authentication != null)
        {
            if(authentication.isAuthenticated()){
                model.addAttribute("isInCustomerLibrary",
                        itemsService.getItem(
                                customer.getId(),
                                movie.id,
                                ProxyConfig.MOVIES) != null);
            }
        }

        return "movie";
    }

    @PostMapping("/remove")
    public String removeMovie(Authentication authentication, @ModelAttribute("itemId") String itemId){
        var customer = customerService.getCustomerByUsername(authentication.getName());
        if(!customerService.isAdmin(customer.getId())){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "You don't have permission to do this!"
            );
        }
        itemsService.removeItem(ProxyConfig.MOVIES, UUID.fromString(itemId));
        return "redirect:/movies";
    }
}
