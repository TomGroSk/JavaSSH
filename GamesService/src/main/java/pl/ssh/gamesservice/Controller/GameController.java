package pl.ssh.gamesservice.Controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.ssh.gamesservice.Entity.Game;
import pl.ssh.gamesservice.FileStorage.BlobStorage;
import pl.ssh.gamesservice.Repository.GameRepository;
import pl.ssh.gamesservice.Service.GameService;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/games")
public class GameController {
    private final GameRepository gameRepository;
    private final GameService gameService;
    private final BlobStorage blobStorage;

    public GameController(GameRepository gameRepository, GameService gameService, BlobStorage blobStorage) {
        this.gameRepository = gameRepository;
        this.gameService = gameService;
        this.blobStorage = blobStorage;
    }

    @GetMapping("/get")
    public List<Game> getAllGames() {
        var games =  gameRepository.findAll();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        for (Game g : games) {
            if(g.publicationDate != null)
            {
                g.setFormattedDate(df.format(g.publicationDate));
            }
        }
        return games;
    }

    @GetMapping("/get/{id}")
    public Game getGameById(@ModelAttribute("id") UUID id) {
        var game = gameRepository.findById(id);
        if (game.isPresent()) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            if(game.get().publicationDate != null)
            {
                game.get().setFormattedDate(df.format(game.get().publicationDate));
            }
            return game.get();
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Entity does not exist!"
        );
    }

    @PostMapping("/create")
    public UUID createGame(@RequestBody Game game) {
        if (game.filename != null && game.fileContent != null) {
            var stream = gameService.convertFileContentToStream(game.fileContent);
            try {
                game.cover = blobStorage.uploadFile(game.filename, stream, stream.available());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        var createdGame = gameRepository.save(game);
        return createdGame.id;
    }

    @PostMapping("/update")
    public UUID updateGame(@RequestBody Game game) {
        var currentGame = gameRepository.findById(game.id);
        if (currentGame.isPresent()) {
            gameRepository.save(game);
            return game.id;
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Entity does not exist!"
        );
    }

    @DeleteMapping("/delete/{id}")
    public void deleteGame(@ModelAttribute("id") UUID id) {
        var game = gameRepository.findById(id);
        game.ifPresent(gameRepository::delete);
    }
}
