package io.github.ititus.tictactoe.lib.board;

import io.github.ititus.tictactoe.lib.Mark;
import io.github.ititus.tictactoe.lib.Pos;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.github.ititus.tictactoe.lib.Mark.NONE;
import static io.github.ititus.tictactoe.lib.Pos.ALL;
import static io.github.ititus.tictactoe.lib.Pos.LINES;

public interface Board {

    int SIZE = 3;

    static int swapStartingMark(int encodedBoard) {
        if (encodedBoard == 0) {
            return Integer.MIN_VALUE;
        } else if (encodedBoard == Integer.MIN_VALUE) {
            return 0;
        }

        return -encodedBoard;
    }

    static Mark swapMark(Mark mark) {
        return mark == NONE ? NONE : mark.enemy();
    }

    static int swapMarksIgnoringHistory(int encodedBoard) {
        if (encodedBoard < 0) {
            throw new IllegalArgumentException();
        }

        int encoded = 0;
        int shift = 0;
        while (encodedBoard > 0) {
            int mark = encodedBoard & 0x3;
            if (mark == Mark.CROSS.ordinal()) {
                mark = Mark.CIRCLE.ordinal();
            } else if (mark == Mark.CIRCLE.ordinal()) {
                mark = Mark.CROSS.ordinal();
            }

            encoded |= (mark << shift);

            encodedBoard >>>= 2;
            shift += 2;
        }

        return encoded;
    }

    static List<Board> allCrossStart() {
        return findBoards("/boards_cross.txt");
    }

    static List<Board> allCircleStart() {
        return findBoards("/boards_circle.txt");
    }

    static List<Board> findBoards(String path) {
        InputStream is = Board.class.getResourceAsStream(path);
        if (is == null) {
            throw new RuntimeException();
        }

        try (is; BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            return br.lines()
                    .mapToInt(Integer::parseInt)
                    .mapToObj(n -> (Board) WritableBoard.of(n))
                    .toList();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    Mark get(Pos pos);

    Pos getPos(int turn);

    Mark getStartingMark();

    default Mark getCurrentMark() {
        return getTurn() % 2 == 0 ? getStartingMark() : getStartingMark().enemy();
    }

    int getTurn();

    default Optional<Mark> checkGameEnd() {
        int turn = getTurn();
        if (turn < 2 * SIZE - 1) {
            return Optional.empty();
        }

        return LINES.stream()
                .map(line -> {
                    Mark winningMark = null;
                    for (Pos pos : line) {
                        Mark mark = get(pos);
                        if (mark.empty() || (winningMark != null && mark != winningMark)) {
                            return Optional.<Mark>empty();
                        } else {
                            winningMark = mark;
                        }
                    }

                    return Optional.ofNullable(winningMark);
                })
                .flatMap(Optional::stream)
                .findAny()
                .or(() -> turn >= SIZE * SIZE ? Optional.of(NONE) : Optional.empty());
    }

    default int encode() {
        boolean pos = getStartingMark() == Mark.CROSS;
        int turn = getTurn();
        if (turn == 0) {
            return pos ? 0 : Integer.MIN_VALUE;
        }

        int encoded = 0;
        for (int i = 0; i < turn; i++) {
            encoded *= 10;
            encoded += getPos(i).index() + 1;
        }

        return pos ? encoded : -encoded;
    }

    default int encodeIgnoringHistory() {
        int encoded = 0;
        for (Pos pos : ALL) {
            encoded <<= 2;
            encoded |= get(pos).ordinal() & 0x3;
        }

        return encoded;
    }

    default boolean isEqualToIgnoringHistory(Board board) {
        if (getTurn() != board.getTurn()) {
            return false;
        }

        return ALL.stream().allMatch(pos -> get(pos) == board.get(pos));
    }

    default boolean isEqualToWithHistory(Board board) {
        int turn = getTurn();
        if (turn != board.getTurn()) {
            return false;
        }

        return IntStream.range(0, turn).allMatch(i -> getPos(i) == board.getPos(i));
    }

    default Stream<Pos> emptyStream() {
        return ALL.stream().filter(pos -> get(pos).empty());
    }

    default List<Pos> empty() {
        return emptyStream().toList();
    }
}
