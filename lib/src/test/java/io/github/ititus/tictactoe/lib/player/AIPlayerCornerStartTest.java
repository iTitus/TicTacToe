package io.github.ititus.tictactoe.lib.player;

import io.github.ititus.tictactoe.lib.Mark;
import io.github.ititus.tictactoe.lib.Pos;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.github.ititus.tictactoe.lib.Mark.*;
import static io.github.ititus.tictactoe.lib.Pos.*;
import static io.github.ititus.tictactoe.lib.board.Board.swapMark;
import static io.github.ititus.tictactoe.lib.board.Board.swapStartingMark;
import static io.github.ititus.tictactoe.lib.player.AIPlayerTest.*;
import static org.assertj.core.api.Assertions.assertThat;

class AIPlayerCornerStartTest {

    @Test
    @DisplayName("Corner -> Center")
    void testTurn1() {
        assertThat(next(1)).isEqualTo(CENTER);
    }

    @Nested
    class NearSideResponse {

        @SuppressWarnings("unused")
        static Stream<Arguments> testNext() {
            return Stream.of(
                    args(12, LEFT, CENTER, BOTTOM_LEFT), // 124, 125, 127; safe cross win

                    args(123, CENTER), // 1235; safe draw
                    args(124, BOTTOM_LEFT), // 1247; safe cross win
                    args(125, BOTTOM_RIGHT), // 1259; safe cross win
                    args(126, CENTER), // 1265; safe draw
                    args(127, LEFT), // 1234; safe cross win
                    args(128, BOTTOM_LEFT, BOTTOM_RIGHT), // 1287, 1289; safe draw
                    args(129, CENTER) // 1295; safe draw
            );
        }

        @SuppressWarnings("unused")
        static Stream<Arguments> testWinner() {
            return Stream.of(
                    args(12, CROSS),

                    args(123, NONE),
                    args(124, CROSS),
                    args(125, CROSS),
                    args(126, NONE),
                    args(127, CROSS),
                    args(128, NONE),
                    args(129, NONE)
            );
        }

        @ParameterizedTest
        @MethodSource
        void testNext(int startingBoard, Iterable<? extends Pos> expected) {
            assertThat(next(startingBoard)).isIn(expected);
        }

        @ParameterizedTest
        @MethodSource("testNext")
        void testNextFlipped(int startingBoard, Iterable<? extends Pos> expected) {
            assertThat(next(swapStartingMark(startingBoard))).isIn(expected);
        }

        @ParameterizedTest
        @MethodSource
        void testWinner(int startingBoard, Mark expected) {
            assertThat(winner(startingBoard)).isEqualTo(expected);
        }

        @ParameterizedTest
        @MethodSource("testWinner")
        void testWinnerFlipped(int startingBoard, Mark expected) {
            assertThat(winner(swapStartingMark(startingBoard))).isEqualTo(swapMark(expected));
        }
    }

    @Nested
    class NearCornerResponse {

        @SuppressWarnings("unused")
        static Stream<Arguments> testNext() {
            return Stream.of(
                    args(13, LEFT, BOTTOM_LEFT, BOTTOM_RIGHT), // 134, 137, 139; safe cross win

                    args(132, RIGHT, BOTTOM_RIGHT), // 1326, 1329; safe circle win
                    args(134, BOTTOM_LEFT), // 1327; safe cross win
                    args(135, BOTTOM_RIGHT), // 1359; safe draw
                    args(136, LEFT, CENTER), // 1287, 1365; safe draw
                    args(137, LEFT), // 1237; safe cross win
                    args(138, BOTTOM_RIGHT), // 1389; safe draw
                    args(139, CENTER) // 1395; safe cross win
            );
        }

        @SuppressWarnings("unused")
        static Stream<Arguments> testWinner() {
            return Stream.of(
                    args(13, CROSS),

                    args(132, CIRCLE),
                    args(134, CROSS),
                    args(135, NONE),
                    args(136, NONE),
                    args(137, CROSS),
                    args(138, NONE),
                    args(139, CROSS)
            );
        }

        @ParameterizedTest
        @MethodSource
        void testNext(int startingBoard, Iterable<? extends Pos> expected) {
            assertThat(next(startingBoard)).isIn(expected);
        }

        @ParameterizedTest
        @MethodSource("testNext")
        void testNextFlipped(int startingBoard, Iterable<? extends Pos> expected) {
            assertThat(next(swapStartingMark(startingBoard))).isIn(expected);
        }

        @ParameterizedTest
        @MethodSource
        void testWinner(int startingBoard, Mark expected) {
            assertThat(winner(startingBoard)).isEqualTo(expected);
        }

        @ParameterizedTest
        @MethodSource("testWinner")
        void testWinnerFlipped(int startingBoard, Mark expected) {
            assertThat(winner(swapStartingMark(startingBoard))).isEqualTo(swapMark(expected));
        }
    }

    @Nested
    class CenterResponse {

        @SuppressWarnings("unused")
        static Stream<Arguments> testNext() {
            return Stream.of(
                    args(15, RIGHT, BOTTOM, BOTTOM_RIGHT), // 156, 159; cross win possible - but unlikely - against non-perfect player

                    args(152, TOP_RIGHT), // 1325; safe draw
                    args(153, TOP), // 1235; safe draw
                    args(156, BOTTOM_RIGHT), // 1569; circle win possible - but unlikely - against non-perfect player
                    args(159, TOP, LEFT) // 1295; safe draw
            );
        }

        @SuppressWarnings("unused")
        static Stream<Arguments> testWinner() {
            return Stream.of(
                    args(15, NONE),

                    args(152, NONE),
                    args(153, NONE),
                    args(156, NONE),
                    args(159, NONE)
            );
        }

        @ParameterizedTest
        @MethodSource
        void testNext(int startingBoard, Iterable<? extends Pos> expected) {
            assertThat(next(startingBoard)).isIn(expected);
        }

        @ParameterizedTest
        @MethodSource("testNext")
        void testNextFlipped(int startingBoard, Iterable<? extends Pos> expected) {
            assertThat(next(swapStartingMark(startingBoard))).isIn(expected);
        }

        @ParameterizedTest
        @MethodSource
        void testWinner(int startingBoard, Mark expected) {
            assertThat(winner(startingBoard)).isEqualTo(expected);
        }

        @ParameterizedTest
        @MethodSource("testWinner")
        void testWinnerFlipped(int startingBoard, Mark expected) {
            assertThat(winner(swapStartingMark(startingBoard))).isEqualTo(swapMark(expected));
        }
    }

    @Nested
    class FarSideResponse {

        @SuppressWarnings("unused")
        static Stream<Arguments> testNext() {
            return Stream.of(
                    args(16, TOP_RIGHT, CENTER, BOTTOM_LEFT), // 127, 165, 167; safe cross win

                    args(127, LEFT), // 1234; safe cross win =163
                    args(129, CENTER), // 1295; safe draw
                    args(162, TOP_RIGHT), // 1326; safe circle win
                    args(164, BOTTOM_LEFT), // 1328; circle win possible - but unlikely - against non-perfect player
                    args(165, BOTTOM_RIGHT), // 1659; safe cross win
                    args(167, LEFT), // 1238; safe cross win
                    args(168, CENTER) // 1568; safe draw
            );
        }

        @SuppressWarnings("unused")
        static Stream<Arguments> testWinner() {
            return Stream.of(
                    args(16, CROSS),

                    args(127, CROSS),
                    args(129, NONE),
                    args(162, CIRCLE),
                    args(164, NONE),
                    args(165, CROSS),
                    args(167, CROSS),
                    args(168, NONE)
            );
        }

        @ParameterizedTest
        @MethodSource
        void testNext(int startingBoard, Iterable<? extends Pos> expected) {
            assertThat(next(startingBoard)).isIn(expected);
        }

        @ParameterizedTest
        @MethodSource("testNext")
        void testNextFlipped(int startingBoard, Iterable<? extends Pos> expected) {
            assertThat(next(swapStartingMark(startingBoard))).isIn(expected);
        }

        @ParameterizedTest
        @MethodSource
        void testWinner(int startingBoard, Mark expected) {
            assertThat(winner(startingBoard)).isEqualTo(expected);
        }

        @ParameterizedTest
        @MethodSource("testWinner")
        void testWinnerFlipped(int startingBoard, Mark expected) {
            assertThat(winner(swapStartingMark(startingBoard))).isEqualTo(swapMark(expected));
        }
    }

    @Nested
    class FarCornerResponse {

        @SuppressWarnings("unused")
        static Stream<Arguments> testNext() {
            return Stream.of(
                    args(19, TOP_RIGHT, BOTTOM_LEFT), // 137; safe cross win

                    args(137, LEFT), // 1237; safe cross win
                    args(192, TOP_RIGHT), // 1329; safe circle win
                    args(195, TOP_RIGHT, BOTTOM_LEFT), // 1359; safe draw
                    args(196, CENTER) // 1569; circle win possible - but unlikely - against non-perfect player
            );
        }

        @SuppressWarnings("unused")
        static Stream<Arguments> testWinner() {
            return Stream.of(
                    args(19, CROSS),

                    args(137, CROSS),
                    args(192, CIRCLE),
                    args(195, NONE),
                    args(196, NONE)
            );
        }

        @ParameterizedTest
        @MethodSource
        void testNext(int startingBoard, Iterable<? extends Pos> expected) {
            assertThat(next(startingBoard)).isIn(expected);
        }

        @ParameterizedTest
        @MethodSource("testNext")
        void testNextFlipped(int startingBoard, Iterable<? extends Pos> expected) {
            assertThat(next(swapStartingMark(startingBoard))).isIn(expected);
        }

        @ParameterizedTest
        @MethodSource
        void testWinner(int startingBoard, Mark expected) {
            assertThat(winner(startingBoard)).isEqualTo(expected);
        }

        @ParameterizedTest
        @MethodSource("testWinner")
        void testWinnerFlipped(int startingBoard, Mark expected) {
            assertThat(winner(swapStartingMark(startingBoard))).isEqualTo(swapMark(expected));
        }
    }
}
