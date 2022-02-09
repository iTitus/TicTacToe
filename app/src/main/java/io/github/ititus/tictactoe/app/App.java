package io.github.ititus.tictactoe.app;

import io.github.ititus.commons.math.time.DurationFormatter;
import io.github.ititus.commons.math.time.StopWatch;
import io.github.ititus.tictactoe.lib.Game;
import io.github.ititus.tictactoe.lib.Mark;
import io.github.ititus.tictactoe.lib.board.*;
import io.github.ititus.tictactoe.lib.player.AICapabilities;
import io.github.ititus.tictactoe.lib.player.ConsolePlayer;
import io.github.ititus.tictactoe.lib.player.HeuristicAIPlayer;
import io.github.ititus.tictactoe.lib.player.Player;

import java.text.NumberFormat;
import java.time.Duration;
import java.util.List;
import java.util.Locale;

public class App {

    private static final int N = 1_000_000;

    public static void main(String[] args) {
        playerGame();
    }

    private static void analyseAllCanonicalBoards() {
        List<Board> boards = Board.allCrossStart();
        for (Board board : boards) {
            if (board.checkGameEnd().isPresent()) {
                continue;
            }

            analyseBoard(board.encode());
        }
    }

    private static void specificAnalysis() {
        // analyseBoard(0);

        // System.out.println();

        // analyseBoard(1);
        // analyseBoard(2);
        // analyseBoard(5);

        // System.out.println();

        /*analyseBoard(12);
        analyseBoard(13);
        analyseBoard(15);
        analyseBoard(16);
        analyseBoard(19);*/

        /*analyseBoard(21);
        analyseBoard(132);
        analyseBoard(136);
        analyseBoard(196);
        analyseBoard(214);
        analyseBoard(215);
        analyseBoard(216);
        analyseBoard(218);*/

        /*analyseBoard(24);
        analyseBoard(25);
        analyseBoard(27);
        analyseBoard(28);*/

        // analyseBoard(51);
        /*analyseBoard(135);
        analyseBoard(195);
        analyseBoard(215);
        analyseBoard(275);*/

        // System.out.println();

        // analyseBoard(52);
        /*analyseBoard(125);
        analyseBoard(165);
        analyseBoard(245);
        analyseBoard(285);*/

        analyseBoard(132);
        analyseBoard(214);
        analyseBoard(285);
    }

    private static void analyseBoard(int startBoard) {
        System.out.println(startBoard + ",perfect=cross : " + analyseAI(startBoard, N, AICapabilities.BLOCK, false));
        System.out.println(startBoard + ",perfect=circle: " + analyseAI(startBoard, N, AICapabilities.BLOCK, true));
    }

    private static void genericAnalysis() {
        System.out.println(analyseAI(0, 1_000_000, AICapabilities.RANDOM, false));
        System.out.println(analyseAI(0, 1_000_000, AICapabilities.RANDOM, true));
        System.out.println(analyseAI(0, 1_000_000, AICapabilities.WIN, false));
        System.out.println(analyseAI(0, 1_000_000, AICapabilities.WIN, true));
        System.out.println(analyseAI(0, 1_000_000, AICapabilities.BLOCK, false));
        System.out.println(analyseAI(0, 1_000_000, AICapabilities.BLOCK, true));
        System.out.println(analyseAI(0, 1_000_000, AICapabilities.FORK, false));
        System.out.println(analyseAI(0, 1_000_000, AICapabilities.FORK, true));
        System.out.println(analyseAI(0, 1_000_000, AICapabilities.PERFECT, false));
        System.out.println(analyseAI(0, 1_000_000, AICapabilities.PERFECT, true));
    }

    private static Stats analyseAI(int startBoard, int n, int enemyLevel, boolean swap) {
        Stats stats = new Stats();
        for (int i = 0; i < n; i++) {
            WritableBoard board = WritableBoard.of(startBoard);
            Player cross = new HeuristicAIPlayer(board, Mark.CROSS, AICapabilities.builder().level(swap ? enemyLevel : AICapabilities.PERFECT).build());
            Player circle = new HeuristicAIPlayer(board, Mark.CIRCLE, AICapabilities.builder().level(swap ? AICapabilities.PERFECT : enemyLevel).build());
            Game g = new Game(
                    board,
                    cross,
                    circle,
                    SilentBoardPrinter.INSTANCE
            );
            stats.add(g.run());
        }

        return stats;
    }

    private static void playerGame() {
        WritableBoard board = WritableBoard.of();
        Game g = new Game(
                board,
                new HeuristicAIPlayer(board, Mark.CROSS, AICapabilities.builder().level(AICapabilities.PERFECT).build()),
                new ConsolePlayer(board, Mark.CIRCLE),
                new ConsoleBoardPrinter()
        );
        g.run();
    }

    private static void allTheGames() {
        StopWatch s = StopWatch.createRunning();
        for (long n = 0; ; n++) {
            if (n % 25000 == 0) {
                Duration d = s.stop().dividedBy(25000);
                System.out.println("Game " + n + (n > 0 ? " | " + DurationFormatter.format(d) : ""));
                s.start();
            }

            WritableBoard board = WritableBoard.of();
            Game g = new Game(
                    board,
                    new HeuristicAIPlayer(board, Mark.CROSS, AICapabilities.builder().level(AICapabilities.PERFECT).build()),
                    new HeuristicAIPlayer(board, Mark.CIRCLE, AICapabilities.builder().level(AICapabilities.RANDOM).build()),
                    new DebugBoardPrinter()
            );
            g.run();

            board = WritableBoard.of(Integer.MIN_VALUE);
            g = new Game(
                    board,
                    new HeuristicAIPlayer(board, Mark.CROSS, AICapabilities.builder().level(AICapabilities.PERFECT).build()),
                    new HeuristicAIPlayer(board, Mark.CIRCLE, AICapabilities.builder().level(AICapabilities.RANDOM).build()),
                    new DebugBoardPrinter()
            );
            g.run();
        }
    }

    private static void printSomeStats() {
        List<Board> all = Board.allCrossStart();
        List<Board> notFinished = all.stream()
                .filter(b -> b.checkGameEnd().isEmpty())
                .toList();
        List<Board> finished = all.stream()
                .filter(b -> b.checkGameEnd().isPresent())
                .toList();
        System.out.println("Valid games: " + all.size());
        System.out.println("Unfinished games: " + notFinished.size());
        System.out.println("Finished games: " + finished.size());
        System.out.println("  - X Win: " + finished.stream().filter(b -> b.checkGameEnd().orElseThrow() == Mark.CROSS).count());
        System.out.println("  - O Win: " + finished.stream().filter(b -> b.checkGameEnd().orElseThrow() == Mark.CIRCLE).count());
        List<Board> draws = finished.stream().filter(b -> b.checkGameEnd().orElseThrow() == Mark.NONE).toList();
        System.out.println("  - Draw: " + draws.size());

        ConsoleBoardPrinter p = new ConsoleBoardPrinter();
        draws.forEach(p::print);
    }

    private static class Stats {

        private static final NumberFormat F;

        static {
            F = NumberFormat.getPercentInstance(Locale.ROOT);
            // F.setMinimumIntegerDigits(2);
            F.setMinimumFractionDigits(2);
        }

        int crossWins;
        int circleWins;
        int draws;

        void add(Mark winner) {
            switch (winner) {
                case NONE -> draws++;
                case CROSS -> crossWins++;
                case CIRCLE -> circleWins++;
            }
        }

        int total() {
            return crossWins + circleWins + draws;
        }


        double crossWins() {
            return crossWins / ((double) total());
        }

        double circleWins() {
            return circleWins / ((double) total());
        }

        double draws() {
            return draws / ((double) total());
        }

        @Override
        public String toString() {
            return String.format(Locale.ROOT, "Stats: X: %s [%,d/%,d] | O: %s [%,d/%,d] | Draw: %s [%,d/%,d]", F.format(crossWins()), crossWins, total(), F.format(circleWins()), circleWins, total(), F.format(draws()), draws, total());
        }
    }
}
