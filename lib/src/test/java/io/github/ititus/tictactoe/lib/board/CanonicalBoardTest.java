package io.github.ititus.tictactoe.lib.board;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static io.github.ititus.tictactoe.lib.board.WritableBoard.optionalOf;
import static org.assertj.core.api.Assertions.assertThat;

@Disabled("slow")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CanonicalBoardTest {

    IntSet encodedBoardsIgnoringHistory;

    @BeforeAll
    void setup() {
        encodedBoardsIgnoringHistory = IntOpenHashSet.toSet(Board.allCrossStart().stream().mapToInt(Board::encodeIgnoringHistory));
    }

    @Test
    void testCalculateTransform0() {
        for (int encoded = 0; encoded <= 0; encoded++) {
            optionalOf(encoded)
                    .map(CanonicalBoard::new)
                    .ifPresent(board -> assertThat(board.encodeIgnoringHistory()).isIn(encodedBoardsIgnoringHistory));
        }
    }

    @Test
    void testCalculateTransform1() {
        for (int encoded = 1; encoded <= 9; encoded++) {
            optionalOf(encoded)
                    .map(CanonicalBoard::new)
                    .ifPresent(board -> assertThat(board.encodeIgnoringHistory()).isIn(encodedBoardsIgnoringHistory));
        }
    }

    @Test
    void testCalculateTransform2() {
        for (int encoded = 12; encoded <= 98; encoded++) {
            optionalOf(encoded)
                    .map(CanonicalBoard::new)
                    .ifPresent(board -> assertThat(board.encodeIgnoringHistory()).isIn(encodedBoardsIgnoringHistory));
        }
    }

    @Test
    void testCalculateTransform3() {
        for (int encoded = 123; encoded <= 987; encoded++) {
            optionalOf(encoded)
                    .map(CanonicalBoard::new)
                    .ifPresent(board -> assertThat(board.encodeIgnoringHistory()).isIn(encodedBoardsIgnoringHistory));
        }
    }

    @Test
    void testCalculateTransform4() {
        for (int encoded = 1234; encoded <= 9876; encoded++) {
            optionalOf(encoded)
                    .map(CanonicalBoard::new)
                    .ifPresent(board -> assertThat(board.encodeIgnoringHistory()).isIn(encodedBoardsIgnoringHistory));
        }
    }

    @Test
    void testCalculateTransform5() {
        for (int encoded = 12345; encoded <= 98765; encoded++) {
            optionalOf(encoded)
                    .map(CanonicalBoard::new)
                    .ifPresent(board -> assertThat(board.encodeIgnoringHistory()).isIn(encodedBoardsIgnoringHistory));
        }
    }

    @Test
    void testCalculateTransform6() {
        for (int encoded = 123456; encoded <= 987654; encoded++) {
            optionalOf(encoded)
                    .map(CanonicalBoard::new)
                    .ifPresent(board -> assertThat(board.encodeIgnoringHistory()).isIn(encodedBoardsIgnoringHistory));
        }
    }

    @Test
    void testCalculateTransform7() {
        for (int encoded = 1234567; encoded <= 9876543; encoded++) {
            optionalOf(encoded)
                    .map(CanonicalBoard::new)
                    .ifPresent(board -> assertThat(board.encodeIgnoringHistory()).isIn(encodedBoardsIgnoringHistory));
        }
    }

    @Test
    void testCalculateTransform8() {
        for (int encoded = 12345678; encoded <= 98765432; encoded++) {
            optionalOf(encoded)
                    .map(CanonicalBoard::new)
                    .ifPresent(board -> assertThat(board.encodeIgnoringHistory()).isIn(encodedBoardsIgnoringHistory));
        }
    }

    @Test
    void testCalculateTransform9() {
        for (int encoded = 123456789; encoded <= 987654321; encoded++) {
            optionalOf(encoded)
                    .map(CanonicalBoard::new)
                    .ifPresent(board -> assertThat(board.encodeIgnoringHistory()).isIn(encodedBoardsIgnoringHistory));
        }
    }
}
