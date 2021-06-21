package pl.ssh.moviesservice.Controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.ssh.moviesservice.Entity.Movie;
import pl.ssh.moviesservice.FileStorage.BlobStorage;
import pl.ssh.moviesservice.Repository.MovieRepository;
import pl.ssh.moviesservice.Service.MovieService;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/movies")
public class MovieController {
    private final MovieRepository movieRepository;
    private final MovieService movieService;
    private final BlobStorage blobStorage;

    public MovieController(MovieRepository movieRepository, MovieService movieService, BlobStorage blobStorage) {
        this.movieRepository = movieRepository;
        this.movieService = movieService;
        this.blobStorage = blobStorage;
    }

    @GetMapping("/get")
    public List<Movie> getAllMovie() {
        var movies =  movieRepository.findAll();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        for (Movie m : movies) {
            if(m.releaseDate != null)
            {
                m.setFormattedDate(df.format(m.releaseDate));
            }
        }
        return movies;
    }

    @GetMapping("/get/{id}")
    public Movie getMovieById(@ModelAttribute("id") UUID id) {
        var movie = movieRepository.findById(id);
        if (movie.isPresent()) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            if(movie.get().releaseDate != null)
            {
                movie.get().setFormattedDate(df.format(movie.get().releaseDate));
            }
            return movie.get();
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Entity does not exist!"
        );
    }

    @PostMapping("/create")
    public UUID createMovie(@RequestBody Movie movie) {
        if (movie.filename != null && movie.fileContent != null) {
            var stream = movieService.convertFileContentToStream(movie.fileContent);
            try {
                movie.cover = blobStorage.uploadFile(movie.filename, stream, stream.available());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        var createdMovie = movieRepository.save(movie);
        return createdMovie.id;
    }

    @PostMapping("/update")
    public UUID updateMovie(@RequestBody Movie movie) {
        var currentMovie = movieRepository.findById(movie.id);
        if (currentMovie.isPresent()) {
            movieRepository.save(movie);
            return movie.id;
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Entity does not exist!"
        );
    }

    @DeleteMapping("/delete/{id}")
    public void deleteMovie(@ModelAttribute("id") UUID id) {
        var movie = movieRepository.findById(id);
        movie.ifPresent(movieRepository::delete);
    }
}
