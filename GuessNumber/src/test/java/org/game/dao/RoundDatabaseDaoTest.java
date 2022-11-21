package org.game.dao;

import org.game.dto.Round;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoundDatabaseDaoTest {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    RoundDao roundDao;
    @Autowired
    GameDao gameDao;

    @Before
    public void setUp() {
        final String DELETE_ROUND = "DELETE FROM round;";
        jdbcTemplate.update(DELETE_ROUND);
    }

    @Test
    public void addRoundAndList() {
        // create round that should be in list
        int gameId1 = gameDao.addGame("1111").getGameId();
        Round round1 = new Round();
        round1.setGuess("1111");
        round1.setGuessTime(LocalDateTime.now().withNano(0));
        round1.setGameId(gameId1);
        round1.setResult("e:4:p:0");
        Round added1 = roundDao.addRound(round1);

        // create round that should not be in list
        int gameId2 = gameDao.addGame("2222").getGameId();
        Round round2 = new Round();
        round2.setGuess("4444");
        round2.setGuessTime(LocalDateTime.now().withNano(0));
        round2.setGameId(gameId2);
        round2.setResult("e:0:p:0");
        Round added2 = roundDao.addRound(round2);

        List<Round> rounds = roundDao.listRounds(gameId1);
        assertEquals(1, rounds.size());
        Round fromList = rounds.get(0);
        assertEquals(added1.getRoundId(), fromList.getRoundId());
        assertEquals(round1.getGuess(), fromList.getGuess());
        assertEquals(round1.getGuessTime(), fromList.getGuessTime());
        assertEquals(round1.getResult(), fromList.getResult());
        assertEquals(round1.getGameId(), fromList.getGameId());

        int newGameId = gameDao.addGame("3333").getGameId();
        List<Round> emptyRounds = roundDao.listRounds(newGameId);
        assertEquals(0, emptyRounds.size());
    }
}