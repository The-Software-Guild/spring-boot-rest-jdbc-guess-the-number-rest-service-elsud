package org.game.dao;

import org.game.dto.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
@Profile("prod")
public class GameDatabaseDao implements GameDao {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public Game getGame(int gameId) {
        final String SELECT_GAME_BY_ID = "SELECT * FROM game WHERE gameId=?;";
        try {
            return jdbc.queryForObject(SELECT_GAME_BY_ID, new GameMapper(), gameId);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Game> listGames() {
        final String SELECT_GAMES = "SELECT * FROM game;";
        return jdbc.query(SELECT_GAMES, new GameMapper());
    }

    @Override
    public Game addGame(String answer) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        final String INSERT_GAME = "INSERT INTO game(answer) VALUES(?);";
        jdbc.update((Connection conn) -> {
            PreparedStatement statement = conn.prepareStatement(
                    INSERT_GAME, Statement.RETURN_GENERATED_KEYS
            );
            statement.setString(1, answer);
            return statement;
        }, keyHolder);
        Game game = new Game();
        game.setGameId(keyHolder.getKey().intValue());
        game.setAnswer(answer);
        game.setFinished(false);
        return game;
    }

    @Override
    public boolean finishGame(int gameId) {
        final String UPDATE_GAME = "UPDATE game SET finished=? WHERE gameId=?";
        try {
            return jdbc.update(UPDATE_GAME, true, gameId) > 0;
        } catch (DataAccessException ex) {
            return false;
        }
    }

    private final class GameMapper implements RowMapper<Game> {
        @Override
        public Game mapRow(ResultSet resultSet, int i) throws SQLException {
            Game game = new Game();
            game.setGameId(resultSet.getInt("gameId"));
            game.setAnswer(resultSet.getString("answer"));
            game.setFinished(resultSet.getBoolean("finished"));
            return game;
        }
    }
}
