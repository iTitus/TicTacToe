package io.github.ititus.tictactoe.lib.player;

import io.github.ititus.tictactoe.lib.Mark;
import io.github.ititus.tictactoe.lib.Pos;
import io.github.ititus.tictactoe.lib.board.Board;

import java.util.Scanner;

public class ConsolePlayer extends AbstractPlayer {

    public ConsolePlayer(Board board, Mark mark) {
        super(board, mark);
    }

    @Override
    public Pos nextTurn() {
        System.out.print("Your turn (" + mark + "): ");
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt() - 1;
        return Pos.of(n);
    }
}
