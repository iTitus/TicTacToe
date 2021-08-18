package io.github.ititus.tictactoe.lib.board;

import io.github.ititus.tictactoe.lib.Mark;
import io.github.ititus.tictactoe.lib.Pos;

import java.util.List;

import static io.github.ititus.tictactoe.lib.Pos.ALL_HORIZONTALS;

public class ConsoleBoardPrinter implements BoardPrinter {

    static char symbol(Mark mark) {
        return switch (mark) {
            case NONE -> ' ';
            case CROSS -> 'X';
            case CIRCLE -> 'O';
        };
    }

    @Override
    public void print(Board board) {
        System.out.println("+---+---+---+");

        for (List<Pos> horizontal : ALL_HORIZONTALS) {
            StringBuilder b = new StringBuilder();
            for (Pos pos : horizontal) {
                char c = symbol(board.get(pos));
                b.append('|').append(' ').append(c).append(' ');
            }

            System.out.println(b.append('|'));
            System.out.println("+---+---+---+");
        }
    }

    @Override
    public void announceGameEnd(Board board, Mark winner) {
        print(board);
        if (winner.empty()) {
            System.out.println("Draw!");
        } else {
            System.out.println(winner + " wins!");
        }
    }
}
