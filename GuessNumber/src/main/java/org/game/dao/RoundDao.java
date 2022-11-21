package org.game.dao;

import org.game.dto.Round;

import java.util.List;

public interface RoundDao {

    public List<Round> listRounds(int gameId);

    public Round addRound(Round round);
}
