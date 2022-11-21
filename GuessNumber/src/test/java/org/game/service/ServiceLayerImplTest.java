package org.game.service;

import org.game.dto.Game;
import org.game.dto.Round;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
public class ServiceLayerImplTest {

    @Autowired
    ServiceLayer testService;

    @Test
    public void startGame() {
        int gameId = testService.startGame();
        assertEquals(5, gameId);
    }

    @Test
    public void getGames() {
        List<Game> games = testService.getGames();
        games.sort(Comparator.comparing(Game::getGameId));
        assertEquals(2, games.size());
        Game notFinished = games.get(0);
        Game finished = games.get(1);
        assertFalse(notFinished.isFinished());
        assertTrue(finished.isFinished());
        // answer of not finished game is not visible
        assertEquals("****", notFinished.getAnswer());
        // answer of finished game is visible
        assertEquals("2222", finished.getAnswer());
        assertEquals(1, notFinished.getGameId());
        assertEquals(2, finished.getGameId());

    }

    @Test
    public void getGameById() {
        Game game = testService.getGameById(1);
        assertEquals(1, game.getGameId());
        assertFalse(game.isFinished());
        assertEquals("****", game.getAnswer());
        Game finishedGame = testService.getGameById(2);
        assertEquals(2, finishedGame.getGameId());
        assertTrue(finishedGame.isFinished());
        assertEquals("2222", finishedGame.getAnswer());
        assertNull(testService.getGameById(3));
    }

    @Test
    public void checkGuessForNotExistingGame() {
        assertNull(testService.checkGuess(3, "3333"));
    }

    @Test
    public void checkGuessForFinishedGame() {
        assertNull(testService.checkGuess(2, "3333"));
    }

    @Test
    public void checkWrongGuess() {
        Round round = testService.checkGuess(1, "3333");
        assertEquals(1, round.getGameId());
        assertEquals("3333", round.getGuess());
        assertEquals("e:0:p:0", round.getResult());
    }

}