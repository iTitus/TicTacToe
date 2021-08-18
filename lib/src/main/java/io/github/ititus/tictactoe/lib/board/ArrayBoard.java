package io.github.ititus.tictactoe.lib.board;

import io.github.ititus.tictactoe.lib.Mark;
import io.github.ititus.tictactoe.lib.Pos;

import java.util.Arrays;

import static io.github.ititus.tictactoe.lib.Mark.NONE;

public class ArrayBoard extends AbstractBoard implements WritableBoard {

    private final Pos[] history;
    private final Mark[] board;
    private final Mark startingMark;
    private int turn;

    public ArrayBoard(Mark startingMark) {
        this.history = new Pos[SIZE * SIZE];
        this.board = new Mark[SIZE * SIZE];
        Arrays.fill(this.board, NONE);
        this.startingMark = startingMark;
        this.turn = 0;
    }

    @Override
    public Mark get(Pos pos) {
        return board[pos.index()];
    }

    @Override
    public Pos getPos(int turn) {
        if (turn < 0 || turn >= this.turn) {
            throw new IllegalArgumentException();
        }

        return history[turn];
    }

    @Override
    public Mark getStartingMark() {
        return startingMark;
    }

    @Override
    public int getTurn() {
        return turn;
    }

    @Override
    public void place(Pos pos) {
        if (get(pos).notEmpty() || checkGameEnd().isPresent()) {
            throw new IllegalArgumentException();
        }

        board[pos.index()] = getCurrentMark();
        history[turn++] = pos;
    }
}
