package io.github.ititus.tictactoe.lib.board;

import io.github.ititus.tictactoe.lib.Mark;
import io.github.ititus.tictactoe.lib.Pos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface WritableBoard extends Board {

    static WritableBoard of() {
        return new ArrayBoard(Mark.CROSS);
    }

    static WritableBoard of(Board board) {
        ArrayBoard copy = new ArrayBoard(board.getStartingMark());
        for (int i = 0; i < board.getTurn(); i++) {
            copy.place(board.getPos(i));
        }

        return copy;
    }

    static WritableBoard of(int encodedBoard) {
        WritableBoard board = new ArrayBoard(encodedBoard >= 0 ? Mark.CROSS : Mark.CIRCLE);
        if (encodedBoard != 0 && encodedBoard != Integer.MIN_VALUE) {
            encodedBoard = Math.abs(encodedBoard);

            List<Pos> positions = new ArrayList<>(SIZE * SIZE);
            while (encodedBoard > 0) {
                if (positions.size() >= SIZE * SIZE) {
                    throw new IllegalArgumentException();
                }

                positions.add(Pos.of((encodedBoard % 10) - 1));
                encodedBoard /= 10;
            }

            for (int i = positions.size() - 1; i >= 0; i--) {
                if (board.checkGameEnd().isPresent()) {
                    throw new IllegalArgumentException();
                }

                board.place(positions.get(i));
            }
        }

        return board;
    }

    static Optional<WritableBoard> optionalOf(int encodedBoard) {
        try {
            return Optional.of(of(encodedBoard));
        } catch (IllegalArgumentException ignored) {
            return Optional.empty();
        }
    }

    void place(Pos pos);

}
