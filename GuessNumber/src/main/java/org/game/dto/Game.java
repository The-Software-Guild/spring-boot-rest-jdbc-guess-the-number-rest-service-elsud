package org.game.dto;

import java.util.Objects;

public class Game {

    private int gameId;
    private String answer;
    private boolean finished;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;
        Game game = (Game) o;
        return getGameId() == game.getGameId() && getAnswer() == game.getAnswer() && isFinished() == game.isFinished();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGameId(), getAnswer(), isFinished());
    }
}
