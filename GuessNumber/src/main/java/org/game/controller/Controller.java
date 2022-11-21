package org.game.controller;

import org.game.dto.Game;
import org.game.dto.Round;
import org.game.service.ServiceLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;

@RestController
@RequestMapping("api")
@Validated
public class Controller {

    private final ServiceLayer SERVICE;

    @Autowired
    public Controller(ServiceLayer service) {
        this.SERVICE = service;
    }

    @PostMapping("/begin")
    @ResponseStatus(HttpStatus.CREATED)
    public int beginGame() {
        return SERVICE.startGame();
    }

    @PostMapping("/guess")
    public ResponseEntity<Round> guessAnswer(
            @RequestParam int gameId,
            @Valid @Pattern(regexp="^[0-9]{4}$") @RequestParam String guess) {
        Round round = SERVICE.checkGuess(gameId, guess);
        if (round == null) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(round, HttpStatus.CREATED);
    }

    @GetMapping("/game")
    public List<Game> getGames() {
        return SERVICE.getGames();
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<Game> findGameById(@PathVariable int gameId) {
        Game game = SERVICE.getGameById(gameId);
        if (game == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(game);
    }

    @GetMapping("/round/{gameId}")
    public List<Round> getRounds(@PathVariable int gameId) {
        return SERVICE.getRoundList(gameId);
    }


}
