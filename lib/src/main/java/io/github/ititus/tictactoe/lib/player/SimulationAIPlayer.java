package io.github.ititus.tictactoe.lib.player;

import io.github.ititus.tictactoe.lib.Game;
import io.github.ititus.tictactoe.lib.Mark;
import io.github.ititus.tictactoe.lib.Pos;
import io.github.ititus.tictactoe.lib.board.Board;
import io.github.ititus.tictactoe.lib.board.SilentBoardPrinter;
import io.github.ititus.tictactoe.lib.board.WritableBoard;

import java.util.Optional;

import static io.github.ititus.tictactoe.lib.Mark.*;

public class SimulationAIPlayer extends AbstractPlayer {

    private static final int N = 10_000;

    private final AICapabilities myCapabilities;
    private final AICapabilities assumedEnemyCapabilities;

    public SimulationAIPlayer(Board board, Mark mark, AICapabilities assumedEnemyCapabilities) {
        super(board, mark);
        this.myCapabilities = AICapabilities.builder().level(AICapabilities.PERFECT).build();
        this.assumedEnemyCapabilities = assumedEnemyCapabilities;
    }

    @Override
    public Pos nextTurn() {
        Optional<Pos> pos = findWinningPos();
        pos = pos.or(this::findEnemyWinningPos);
        pos = pos.or(() -> board.emptyStream()
                .map(this::analyse)
                .sorted()
                .map(Stats::pos)
                .findFirst());
        return pos.orElseThrow();
    }

    private Stats analyse(Pos pos) {
        WritableBoard modified = WritableBoard.of(this.board);
        modified.place(pos);
        Mark enemy = modified.getCurrentMark();

        Stats stats = new Stats(pos);
        for (int i = 0; i < N; i++) {
            WritableBoard board = WritableBoard.of(modified);
            Player cross = new HeuristicAIPlayer(board, CROSS, enemy == CROSS ? assumedEnemyCapabilities : myCapabilities);
            Player circle = new HeuristicAIPlayer(board, CIRCLE, enemy == CROSS ? myCapabilities : assumedEnemyCapabilities);
            Game g = new Game(
                    board,
                    cross,
                    circle,
                    SilentBoardPrinter.INSTANCE
            );

            Mark winner = g.run();
            if (winner == NONE) {
                stats.addDraw();
            } else if (winner == enemy) {
                stats.addLoss();
            } else {
                stats.addWin();
            }
        }

        return stats;
    }

    private static class Stats implements Comparable<Stats> {

        final Pos pos;
        int wins;
        int losses;
        int draws;

        Stats(Pos pos) {
            this.pos = pos;
        }

        Pos pos() {
            return pos;
        }

        void addWin() {
            wins++;
        }

        void addLoss() {
            losses++;
        }

        void addDraw() {
            draws++;
        }

        double total() {
            return wins + losses + draws;
        }

        @Override
        public int compareTo(Stats o) {
            int c = Double.compare(losses / total(), o.losses / o.total());
            if (c != 0) {
                return c;
            }

            c = Double.compare(wins / total(), o.wins / o.total());
            if (c != 0) {
                return -c;
            }

            return Double.compare(draws / total(), o.draws / o.total());
        }
    }
}
