package pl.ssh.frontservice.model;

import pl.ssh.frontservice.model.dto.Book;
import pl.ssh.frontservice.model.dto.Game;
import pl.ssh.frontservice.model.dto.Movie;

import java.util.ArrayList;
import java.util.List;

public class ItemResponse {
    public List<Book> books;
    public List<Movie> movies;
    public List<Game> games;

    public ItemResponse() {
        this.books = new ArrayList<>();
        this.movies = new ArrayList<>();
        this.games = new ArrayList<>();
    }
}
