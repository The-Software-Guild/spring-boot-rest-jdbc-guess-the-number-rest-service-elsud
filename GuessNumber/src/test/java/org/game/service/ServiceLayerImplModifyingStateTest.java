package org.game.service;

import org.game.dto.Round;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
public class ServiceLayerImplModifyingStateTest {

    @Autowired
    ServiceLayer testService;

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
