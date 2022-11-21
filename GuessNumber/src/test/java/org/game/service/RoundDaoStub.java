package org.game.service;

import org.game.dao.RoundDao;
import org.game.dto.Round;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Profile("test")
public class RoundDaoStub implements RoundDao {

    @Override
    public List<Round> listRounds(int gameId) {
        return new ArrayList<>();
    }

    @Override
    public Round addRound(Round round) {
        return round;
    }
}
