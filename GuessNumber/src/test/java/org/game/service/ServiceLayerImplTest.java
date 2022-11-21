package org.game.service;

import org.game.dao.GameDao;
import org.game.dto.Game;
import org.game.dto.Round;
import org.junit.Before;
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

    // use to fill games in setUp method
    @Autowired
    GameDaoStub gameDaoStub;

    @Before
    public void setUp() {
        gameDaoStub.fillGame();
    }

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

    @Test
    public void checkPartialAndExactGuess() {
        // real answer is 1100
        Round round = testService.checkGuess(1, "1213");
        assertEquals(1, round.getGameId());
        assertEquals("1213", round.getGuess());
        assertEquals("e:1:p:1", round.getResult());
    }

    @Test
    public void checkPartialGuess() {
        // real answer is 1100
        Round round = testService.checkGuess(1, "0011");
        assertEquals(1, round.getGameId());
        assertEquals("0011", round.getGuess());
        assertEquals("e:0:p:4", round.getResult());
    }

    @Test
    public void checkExactGuess() {
        // real answer is 1100
        Round round = testService.checkGuess(1, "1100");
        assertEquals(1, round.getGameId());
        System.out.println(round.getGuess().toString());
        assertEquals("1100", round.getGuess());
        assertEquals("e:4:p:0", round.getResult());
        // check that game is finished and we can see the answer
        assertTrue(testService.getGameById(1).isFinished());
        assertEquals("1100", testService.getGameById(1).getAnswer());
    }

}