package org.game.dao;

import org.game.dto.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Comparator;
import java.util.List;


@Repository
@Profile("prod")
public class RoundDatabaseDao implements RoundDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Round> listRounds(int gameId) {
        final String SELECT_ALL = "SELECT * FROM round WHERE gameId=?;";
        List<Round> rounds = jdbcTemplate.query(SELECT_ALL, new RoundMapper(), gameId);
        rounds.sort(Comparator.comparing(Round::getGuessTime));
        return rounds;
    }

    @Override
    public Round addRound(Round round) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        final String INSERT_ROUND = "INSERT INTO round(gameId, guess, guessTime, result) VALUES(?, ?, ?, ?);";
        jdbcTemplate.update((Connection conn) -> {
            PreparedStatement statement = conn.prepareStatement(
                    INSERT_ROUND, Statement.RETURN_GENERATED_KEYS
            );
            statement.setInt(1, round.getGameId());
            statement.setString(2, round.getGuess());
            statement.setTimestamp(3, Timestamp.valueOf(round.getGuessTime()));
            statement.setString(4, round.getResult());
            return statement;
        }, keyHolder);
        round.setRoundId(keyHolder.getKey().intValue());
        return round;
    }

    private final class RoundMapper implements RowMapper<Round> {
        @Override
        public Round mapRow(ResultSet resultSet, int i) throws SQLException {
            Round round = new Round();
            round.setRoundId(resultSet.getInt("roundId"));
            round.setGameId(resultSet.getInt("gameId"));
            round.setGuess(resultSet.getString("guess"));
            round.setGuessTime(resultSet.getTimestamp("guessTime").toLocalDateTime());
            round.setResult(resultSet.getString("result"));
            return round;
        }
    }
}
