package org.game.service;

import org.game.dto.Game;
import org.game.dto.Round;

import java.util.List;


public interface ServiceLayer {

    public int startGame();

    public List<Game> getGames();

    public Game getGameById(int gameId);

    public List<Round> getRoundList(int gameId);

    public Round checkGuess(int gameId, String guess);

}
