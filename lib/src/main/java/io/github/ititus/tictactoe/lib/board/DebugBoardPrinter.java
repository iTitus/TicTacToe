package io.github.ititus.tictactoe.lib.board;

import io.github.ititus.tictactoe.lib.Mark;
import io.github.ititus.tictactoe.lib.Pos;

import java.util.ArrayList;
import java.util.List;

import static io.github.ititus.tictactoe.lib.Pos.ALL_HORIZONTALS;
import static io.github.ititus.tictactoe.lib.board.ConsoleBoardPrinter.symbol;

public class DebugBoardPrinter implements BoardPrinter {

    private final List<String> lines = new ArrayList<>();

    @Override
    public void print(Board board) {
        lines.add("+---+---+---+");
        for (List<Pos> horizontal : ALL_HORIZONTALS) {
            StringBuilder b = new StringBuilder();
            for (Pos pos : horizontal) {
                char c = symbol(board.get(pos));
                b.append('|').append(' ').append(c).append(' ');
            }

            lines.add(b.append('|').toString());
            lines.add("+---+---+---+");
        }
    }

    @Override
    public void announceGameEnd(Board board, Mark winner) {
        print(board);
        if (winner.empty()) {
            lines.add("Draw!");
        } else {
            lines.add(winner + " wins!");
        }

        switch (winner) {
            case NONE -> {
            }
            case CIRCLE -> {
                lines.forEach(System.out::println);
                System.out.println();
            }
            case CROSS -> {
            }
        }
    }

    public List<String> getLines() {
        return lines;
    }
}
