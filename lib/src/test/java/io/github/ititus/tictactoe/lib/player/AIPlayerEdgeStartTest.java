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

class AIPlayerEdgeStartTest {

    @Test
    @DisplayName("Edge -> Top Left/Top Right/Center/Bottom")
    void testTurn1() {
        assertThat(next(2)).isIn(TOP_LEFT, TOP_RIGHT, CENTER, BOTTOM); // 21, 25, 28
    }

    @Nested
    class NearCornerResponse {

        @SuppressWarnings("unused")
        static Stream<Arguments> testNext() {
            return Stream.of(
                    args(21, LEFT, CENTER, BOTTOM_LEFT, BOTTOM_RIGHT), // 214, 215, 136, 196; safe draw

                    args(132, RIGHT, BOTTOM_RIGHT), // 1326, 1329; safe circle win
                    args(136, LEFT, CENTER), // 1287, 1365; safe draw
                    args(196, CENTER), // 1569; circle win possible - but unlikely - against non-perfect player
                    args(214, RIGHT, BOTTOM), // 2146; circle win possible - but unlikely - against non-perfect player
                    args(215, BOTTOM), // 2158; circle win possible - but unlikely - against non-perfect player
                    args(216, BOTTOM_LEFT), // 2167; safe circle win
                    args(218, CENTER) // 2185; safe circle win
            );
        }

        @SuppressWarnings("unused")
        static Stream<Arguments> testWinner() {
            return Stream.of(
                    args(21, NONE),

                    args(132, CIRCLE),
                    args(136, NONE),
                    args(196, NONE),
                    args(214, NONE),
                    args(215, NONE),
                    args(216, CIRCLE),
                    args(218, CIRCLE)
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
    class EdgeResponse {

        @SuppressWarnings("unused")
        static Stream<Arguments> testNext() {
            return Stream.of(
                    args(24, TOP_LEFT, CENTER), // 124, 245; safe cross win

                    args(124, BOTTOM_LEFT), // 1247; safe cross win
                    args(126, CENTER), // 1265; safe draw
                    args(162, TOP_RIGHT), // 1326; safe circle win
                    args(168, CENTER), // 1568; safe draw
                    args(245, BOTTOM), // 2458; safe cross win
                    args(246, TOP_RIGHT, BOTTOM_RIGHT), // 2146, 2168; circle win possible - but unlikely - against non-perfect player
                    args(248, CENTER) // 2485; safe circle win
            );
        }

        @SuppressWarnings("unused")
        static Stream<Arguments> testWinner() {
            return Stream.of(
                    args(24, CROSS),

                    args(124, CROSS),
                    args(126, NONE),
                    args(162, CIRCLE),
                    args(168, NONE),
                    args(245, CROSS),
                    args(246, NONE),
                    args(248, CIRCLE)
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
                    args(25, LEFT, RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT), // 254, 156; cross win possible - but unlikely - against non-perfect player

                    args(152, TOP_RIGHT), // 1325; safe draw
                    args(156, BOTTOM_RIGHT), // 1569; circle win possible - but unlikely - against non-perfect player
                    args(254, TOP_LEFT, RIGHT), // 2145, 2165; safe draw
                    args(258, TOP_LEFT, TOP_RIGHT, LEFT, RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT) // 2185, 2485; safe circle win
            );
        }

        @SuppressWarnings("unused")
        static Stream<Arguments> testWinner() {
            return Stream.of(
                    args(25, NONE),

                    args(152, NONE),
                    args(156, NONE),
                    args(254, NONE),
                    args(258, CIRCLE)
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
                    args(27, TOP_LEFT), // 134; safe cross win

                    args(134, BOTTOM_LEFT), // 1327; safe cross win
                    args(138, BOTTOM_RIGHT), // 1389; safe draw
                    args(192, TOP_RIGHT), // 1329; safe circle win
                    args(216, BOTTOM_LEFT), // 2167; safe circle win
                    args(218, CENTER), // 2185; safe circle win
                    args(275, BOTTOM), // 2758; safe draw
                    args(276, TOP_LEFT) // 2167; safe circle win
            );
        }

        @SuppressWarnings("unused")
        static Stream<Arguments> testWinner() {
            return Stream.of(
                    args(27, CROSS),

                    args(134, CROSS),
                    args(138, NONE),
                    args(192, CIRCLE),
                    args(216, CIRCLE),
                    args(218, CIRCLE),
                    args(275, NONE),
                    args(276, CIRCLE)
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
    class OppositeEdgeResponse {

        @SuppressWarnings("unused")
        static Stream<Arguments> testNext() {
            return Stream.of(
                    args(28, TOP_LEFT, TOP_RIGHT, LEFT, CENTER, RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT), // 164, 246, 285, 128; safe draw

                    args(128, BOTTOM_LEFT, BOTTOM_RIGHT), // 1287, 1289; safe draw
                    args(164, BOTTOM_LEFT), // 1328; circle win possible - but unlikely - against non-perfect player
                    args(246, TOP_RIGHT, BOTTOM_RIGHT), // 2146, 2168; circle win possible - but unlikely - against non-perfect player
                    args(285, TOP_LEFT, TOP_RIGHT) // 2158; circle win possible - but unlikely - against non-perfect player
            );
        }

        @SuppressWarnings("unused")
        static Stream<Arguments> testWinner() {
            return Stream.of(
                    args(28, NONE),

                    args(128, NONE),
                    args(164, NONE),
                    args(246, NONE),
                    args(285, NONE)
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
