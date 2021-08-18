package io.github.ititus.tictactoe.lib;

import io.github.ititus.tictactoe.lib.board.BoardPrinter;
import io.github.ititus.tictactoe.lib.board.WritableBoard;
import io.github.ititus.tictactoe.lib.player.Player;

import java.util.Optional;

public class Game {

    private final WritableBoard board;
    private final Player cross;
    private final Player circle;
    private final BoardPrinter printer;
    private Mark winner;

    public Game(WritableBoard board, Player cross, Player circle, BoardPrinter printer) {
        this.board = board;
        this.cross = cross;
        this.circle = circle;
        this.printer = printer;
        this.winner = board.checkGameEnd().orElse(null);
    }

    public Mark run() {
        while (winner == null) {
            step();
        }

        return winner;
    }

    public Optional<Mark> step() {
        if (winner != null) {
            throw new IllegalStateException();
        }

        printer.print(board);

        Player currentPlayer = switch (board.getCurrentMark()) {
            case CROSS -> cross;
            case CIRCLE -> circle;
            default -> throw new IllegalStateException();
        };

        board.place(currentPlayer.nextTurn());

        Optional<Mark> winner = board.checkGameEnd();
        if (winner.isPresent()) {
            this.winner = winner.get();
            printer.announceGameEnd(board, this.winner);
        }

        return winner;
    }
}
