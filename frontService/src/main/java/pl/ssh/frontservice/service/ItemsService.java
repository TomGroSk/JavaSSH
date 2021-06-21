package pl.ssh.frontservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import pl.ssh.frontservice.config.ProxyConfig;
import pl.ssh.frontservice.model.Item;
import pl.ssh.frontservice.model.ItemResponse;
import pl.ssh.frontservice.model.dto.*;
import pl.ssh.frontservice.repository.ItemRepository;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;
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
                        var bookResponse = client.send(request, new JsonBodyHandler<>(Book.class));
                        if(bookResponse.statusCode() == 200){
                            var book = bookResponse.body().get();
                            itemResponse.books.add(book);
                        }
                        break;
                    case ProxyConfig.MOVIES:
                        var movieResponse = client.send(request, new JsonBodyHandler<>(Movie.class));
                        if(movieResponse.statusCode() == 200){
                            var movie = movieResponse.body().get();
                            itemResponse.movies.add(movie);
                        }
                        break;
                    case ProxyConfig.GAMES:
                        var gameResponse = client.send(request, new JsonBodyHandler<>(Game.class));
                        if(gameResponse.statusCode() == 200){
                            var game = gameResponse.body().get();
                            itemResponse.games.add(game);
                        }
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

    public Item getItem(Long customerId, UUID itemId, String itemType) {
        var items = itemRepository.getAllByCustomer_IdAndItemIdAndItemType(customerId, itemId, itemType);
        if (items.isEmpty()) {
            return null;
        }
        return items.get(0);
    }

    public void createItem(Item item) {
        itemRepository.save(item);
    }

    public void removeItem(Item item) {
        itemRepository.delete(item);
    }

    public List<Comment> getAllCommentsByItemId(UUID itemId) {
        var request = HttpRequest.newBuilder(URI.create(ProxyConfig.URL_BASE + "comments/" + itemId.toString()))
                .build();
        try {
             return client.send(request, new JsonBodyHandler<>(ArrayList.class)).body().get();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createComment(String itemId, String author, String content) {
        var comment = new PostComment();
        comment.author = author;
        comment.parentId = UUID.fromString(itemId);
        comment.content = content;

        var objectMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = objectMapper
                    .writeValueAsString(comment);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ProxyConfig.URL_BASE + "comments/create"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("content-type", "application/json")
                .build();
        try {
            client.send(request,HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void removeItem(String itemType, UUID itemId){
        var request = HttpRequest.newBuilder(URI.create(ProxyConfig.URL_BASE + itemType + "/delete/" + itemId.toString()))
                .DELETE()
                .build();
        try {
            client.send(request, new JsonBodyHandler<>(ArrayList.class));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void createObject(Object object, String type){
        var objectMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = objectMapper
                    .writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ProxyConfig.URL_BASE + type + "/create"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("content-type", "application/json")
                .build();
        try {
            client.send(request,HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Book mapBook(PostBook postBook){
        var book = new Book();
        book.author = postBook.author;
        book.fileContent = postBook.fileContent;
        book.filename = postBook.filename;
        book.publicationDate = postBook.publicationDate;
        book.description = postBook.description;
        book.isbn = postBook.isbn;
        book.cover = postBook.cover;
        book.title = postBook.title;
        book.publisher = postBook.publisher;
        book.author = postBook.author;
        return book;
    }

    public Game mapGame(PostGame postGame){
        var game = new Game();
        game.author = postGame.author;
        game.fileContent = postGame.fileContent;
        game.filename = postGame.filename;
        game.publicationDate = postGame.publicationDate;
        game.description = postGame.description;
        game.cover = postGame.cover;
        game.title = postGame.title;
        game.publisher = postGame.publisher;
        game.author = postGame.author;
        return game;
    }

    public Movie mapMovie(PostMovie postMovie){
        var movie = new Movie();
        movie.author = postMovie.author;
        movie.fileContent = postMovie.fileContent;
        movie.filename = postMovie.filename;
        movie.releaseDate = postMovie.releaseDate;
        movie.description = postMovie.description;
        movie.cover = postMovie.cover;
        movie.title = postMovie.title;
        movie.publisher = postMovie.publisher;
        movie.author = postMovie.author;
        return movie;
    }
}
