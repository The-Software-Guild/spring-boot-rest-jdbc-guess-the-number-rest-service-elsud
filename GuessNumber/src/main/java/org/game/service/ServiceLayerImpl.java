package org.game.service;

import org.game.dao.GameDao;
import org.game.dao.RoundDao;
import org.game.dto.Game;
import org.game.dto.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Repository
@Profile("prod")
public class ServiceLayerImpl implements ServiceLayer {

    private final GameDao GAME_DAO;
    private final RoundDao ROUND_DAO;

    @Autowired
    public ServiceLayerImpl(GameDao GAME_DAO, RoundDao ROUND_DAO) {
        this.ROUND_DAO = ROUND_DAO;
        this.GAME_DAO = GAME_DAO;
    }

    @Override
    public int startGame() {
        return GAME_DAO.addGame(generate_answer()).getGameId();
    }

    @Override
    public List<Game> getGames() {
        List<Game> games = GAME_DAO.listGames();
        games.stream()
                .filter(game -> !game.isFinished())
                .forEach(game -> game.setAnswer("****"));
        return games;
    }

    @Override
    public Game getGameById(int gameId) {
        Game game = GAME_DAO.getGame(gameId);
        if (game != null && !game.isFinished()) {
            game.setAnswer("****");
        }
        return game;
    }

    @Override
    public List<Round> getRoundList(int gameId) {
        return ROUND_DAO.listRounds(gameId);
    }

    @Override
    public Round checkGuess(int gameId, String guess) {
        Round round = new Round(gameId, guess, LocalDateTime.now());
        Game game = GAME_DAO.getGame(gameId);
        if (game == null || game.isFinished()) {
            return null;
        }
        int exact = 0;
        int part = 0;
        String answer = game.getAnswer();
        for (int i=0; i<guess.length(); i++) {
            // exact match of number
            if (guess.charAt(i) == answer.charAt(i)) {
                exact++;
            // partial match
            } else if (answer.contains("" + guess.charAt(i))) {
                part++;
            }
        }
        round.setResult(String.format("e:%s:p:%s", exact, part));
        // finish game if guess is exact answer
        if (exact == 4) {
            System.out.println(4);
            GAME_DAO.finishGame(gameId);
        }
        // add round to storage
        return ROUND_DAO.addRound(round);
    }

    private String generate_answer() {
        Random randomizer = new Random();
        int number;
        Set<Integer> answerSet = new HashSet<>();
        // generate 4 unique numbers
        while (answerSet.size() < 4) {
            number = randomizer.nextInt(10);
            answerSet.add(number);
        }
        // create one number that consists of generated ones
        int answer = 0;
        for (int value : answerSet) {
            answer = answer * 10 + value;
        }
        // generate one more unique number if first unique was 0
        if (answer < 1000) {
            number = 0;
            while (answerSet.contains(number)) {
                number = randomizer.nextInt(9) + 1;
            }
            answer = number * 1000 + answer;
        }
        return Integer.toString(answer);
    }

}
