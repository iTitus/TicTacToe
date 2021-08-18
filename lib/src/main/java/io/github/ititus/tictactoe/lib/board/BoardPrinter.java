package io.github.ititus.tictactoe.lib.board;

import io.github.ititus.tictactoe.lib.Mark;

public interface BoardPrinter {

    void print(Board board);

    void announceGameEnd(Board board, Mark winner);

}
