package org.game.service;

import org.game.dao.GameDao;
import org.game.dto.Game;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Profile("test")
public class GameDaoStub implements GameDao {

    private Map<Integer, Game> games = new HashMap<>();

    public GameDaoStub() {
        Game game1 = new Game();
        game1.setGameId(1);
        game1.setFinished(false);
        game1.setAnswer("1100");
        Game game2 = new Game();
        game2.setGameId(2);
        game2.setFinished(true);
        game2.setAnswer("2222");
        games.put(1, game1);
        games.put(2, game2);
    }
    @Override
    public Game getGame(int gameId) {
        return games.get(gameId);
    }

    @Override
    public List<Game> listGames() {
        return new ArrayList<>(games.values());
    }

    @Override
    public Game addGame(String answer) {
        Game game = new Game();
        game.setFinished(false);
        game.setGameId(5);
        game.setAnswer(answer);
        return game;
    }

    @Override
    public boolean finishGame(int gameId) {
        Game game = games.get(gameId);
        game.setFinished(true);
        return true;
    }

}

