package io.github.ititus.tictactoe.lib.board;

import io.github.ititus.tictactoe.lib.Mark;

import java.util.Optional;

public abstract class AbstractBoard implements Board {

    private final TurnCache.Obj<Optional<Mark>> winnerCache;
    private final TurnCache.Int encodeCache;
    private final TurnCache.Int encodeIgnoringHistoryCache;

    protected AbstractBoard() {
        this.winnerCache = new TurnCache.Obj<>(Board.super::checkGameEnd);
        this.encodeCache = new TurnCache.Int(Board.super::encode);
        this.encodeIgnoringHistoryCache = new TurnCache.Int(Board.super::encodeIgnoringHistory);
    }

    @Override
    public Optional<Mark> checkGameEnd() {
        return winnerCache.get(getTurn());
    }

    @Override
    public int encode() {
        return encodeCache.get(getTurn());
    }

    @Override
    public int encodeIgnoringHistory() {
        return encodeIgnoringHistoryCache.get(getTurn());
    }
}
