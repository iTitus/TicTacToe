package io.github.ititus.tictactoe.lib.board;

import io.github.ititus.tictactoe.lib.Mark;

public class SilentBoardPrinter implements BoardPrinter {

    public static final SilentBoardPrinter INSTANCE = new SilentBoardPrinter();

    private SilentBoardPrinter() {
    }

    @Override
    public void print(Board board) {
    }

    @Override
    public void announceGameEnd(Board board, Mark winner) {
    }
}
