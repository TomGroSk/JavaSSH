package pl.ssh.frontservice.service;

import org.springframework.stereotype.Service;
import pl.ssh.frontservice.config.ProxyConfig;
import pl.ssh.frontservice.model.Item;
import pl.ssh.frontservice.model.ItemResponse;
import pl.ssh.frontservice.model.dto.Book;
import pl.ssh.frontservice.model.dto.Comment;
import pl.ssh.frontservice.model.dto.Game;
import pl.ssh.frontservice.model.dto.Movie;
import pl.ssh.frontservice.repository.ItemRepository;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ItemsService {

    private final HttpClient client;
    private final ItemRepository itemRepository;
    private final CustomerService customerService;

    public ItemsService(ItemRepository itemRepository, CustomerService customerService) {
        this.itemRepository = itemRepository;
        this.customerService = customerService;
        this.client = HttpClient.newHttpClient();

    }

    public ItemResponse getAllItems(Long customerId) {
        var items = itemRepository.getAllByCustomer_Id(customerId);

        var itemResponse = new ItemResponse();

        for (var item : items) {
            var request = HttpRequest.newBuilder(URI.create(ProxyConfig.URL_BASE + item.getItemType() + "/get/" + item.getItemId().toString()))
                    .build();

            try {
                switch (item.getItemType()) {
                    case ProxyConfig.BOOKS:
                        var book = client.send(request, new JsonBodyHandler<>(Book.class)).body().get();
                        itemResponse.books.add(book);
                        break;
                    case ProxyConfig.MOVIES:
                        var movie = client.send(request, new JsonBodyHandler<>(Movie.class)).body().get();
                        itemResponse.movies.add(movie);
                        break;
                    case ProxyConfig.GAMES:
                        var game = client.send(request, new JsonBodyHandler<>(Game.class)).body().get();
                        itemResponse.games.add(game);
                        break;
                    default:
                        break;
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        return itemResponse;
    }


    public List<Movie> getAllMovies() {
        var request = HttpRequest.newBuilder(URI.create(ProxyConfig.URL_BASE + "movies/get/"))
                .build();
        try {
            var movies = client.send(request, new JsonBodyHandler<>(ArrayList.class)).body().get();
            return movies;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Movie getMovieById(UUID movieId) {
        var request = HttpRequest.newBuilder(URI.create(ProxyConfig.URL_BASE + "movies/get/" + movieId.toString()))
                .build();
        try {
            var movie = client.send(request, new JsonBodyHandler<>(Movie.class)).body().get();
            return movie;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Game> getAllGames() {
        var request = HttpRequest.newBuilder(URI.create(ProxyConfig.URL_BASE + "games/get/"))
                .build();
        try {
            var games = client.send(request, new JsonBodyHandler<>(ArrayList.class)).body().get();
            return games;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Game getGameById(UUID gameId) {
        var request = HttpRequest.newBuilder(URI.create(ProxyConfig.URL_BASE + "games/get/" + gameId.toString()))
                .build();
        try {
            var game = client.send(request, new JsonBodyHandler<>(Game.class)).body().get();
            return game;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Book> getAllBooks() {
        var request = HttpRequest.newBuilder(URI.create(ProxyConfig.URL_BASE + "books/get/"))
                .build();
        try {
            var books = client.send(request, new JsonBodyHandler<>(ArrayList.class)).body().get();
            return books;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Book getBookById(UUID bookId) {
        var request = HttpRequest.newBuilder(URI.create(ProxyConfig.URL_BASE + "books/get/" + bookId.toString()))
                .build();
        try {
            var book = client.send(request, new JsonBodyHandler<>(Book.class)).body().get();
            return book;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Item getItem(Long customerId, UUID itemId, String itemType){
        var items = itemRepository.getAllByCustomer_IdAndItemIdAndItemType(customerId, itemId, itemType);
        if(items.isEmpty()){
            return null;
        }
        return items.get(0);
    }

    public List<Comment> getAllCommentsByItemId(UUID itemId)
    {
        var request = HttpRequest.newBuilder(URI.create(ProxyConfig.URL_BASE + "comments/" + itemId.toString()))
                .build();
        try {
            return client.send(request, new JsonBodyHandler<>(ArrayList.class)).body().get();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
