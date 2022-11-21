package org.game.service;

import org.game.dto.Round;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
public class ServiceLayerImplCheckResultTest {

    @Autowired
    ServiceLayer testService;

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

}
