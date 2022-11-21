package org.game.dao;

import org.game.dto.Game;

import java.util.List;

public interface GameDao {

    public Game getGame(int gameId);

    public List<Game> listGames();

    public Game addGame(String answer);

    public boolean finishGame(int gameId);

}
