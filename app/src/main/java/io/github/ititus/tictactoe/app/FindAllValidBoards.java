package io.github.ititus.tictactoe.app;

import io.github.ititus.tictactoe.lib.Transform;
import io.github.ititus.tictactoe.lib.board.Board;
import io.github.ititus.tictactoe.lib.board.TransformedBoard;
import io.github.ititus.tictactoe.lib.board.WritableBoard;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FindAllValidBoards {

    public static void main(String[] args) {
        exportBoards(false);
        exportBoards(true);
    }

    private static void exportBoards(boolean swap) {
        List<String> lines = new ArrayList<>();
        IntSet knownBoardsIgnoringHistory = IntOpenHashSet.of();
        for (int encoded = 0; encoded <= 987654321; encoded++) {
            Board board;
            try {
                board = WritableBoard.of(swap ? Board.swapStartingMark(encoded) : encoded);
            } catch (IllegalArgumentException ignored) {
                continue;
            }

            boolean keep = true;
            IntSet knownTransformsIgnoringHistory = IntOpenHashSet.of();
            for (Transform t : Transform.VALUES) {
                Board transformed = new TransformedBoard(board, t);
                if (knownTransformsIgnoringHistory.add(transformed.encodeIgnoringHistory())) {
                    keep &= knownBoardsIgnoringHistory.add(transformed.encodeIgnoringHistory());
                }
            }

            if (!keep) {
                continue;
            }

            lines.add(Integer.toString(board.encode()));
        }
        try {
            Files.write(Path.of("lib/src/main/resources/boards_" + (swap ? "circle" : "cross") + ".txt"), lines);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
