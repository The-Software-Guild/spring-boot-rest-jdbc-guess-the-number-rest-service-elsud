package org.game.dao;

import org.game.dto.Game;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class GameDatabaseDaoTest {

    @Autowired
    GameDao gameDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        final String DELETE_ROUND = "DELETE FROM round;";
        final String DELETE_GAME = "DELETE FROM game;";
        jdbcTemplate.update(DELETE_ROUND);
        jdbcTemplate.update(DELETE_GAME);
    }

    @Test
    public void addAndGetGame() {
        String answer = "1111";
        Game game = new Game();
        game.setAnswer(answer);
        Game addedGame = gameDao.addGame(answer);
        Game receivedGame = gameDao.getGame(addedGame.getGameId());
        assertEquals(addedGame.getAnswer(), receivedGame.getAnswer());
        assertEquals(addedGame.isFinished(), receivedGame.isFinished());
        assertEquals(game.getAnswer(), receivedGame.getAnswer());
        assertEquals(game.isFinished(), receivedGame.isFinished());
    }

    @Test
    public void listGames() {
        String answer1 = "1111";
        String answer2 = "2222";
        gameDao.addGame(answer1);
        List<Game> games = gameDao.listGames();
        assertEquals(1, games.size());
        assertEquals(false, games.get(0).isFinished());
        gameDao.addGame(answer2);
        games = gameDao.listGames();
        assertEquals(2, games.size());
    }

    @Test
    public void finishGame() {
        String answer = "1111";
        Game unfinished = gameDao.addGame(answer);
        gameDao.finishGame(unfinished.getGameId());
        Game finished = gameDao.getGame(unfinished.getGameId());
        assertFalse(unfinished.isFinished());
        assertTrue(finished.isFinished());
    }
}