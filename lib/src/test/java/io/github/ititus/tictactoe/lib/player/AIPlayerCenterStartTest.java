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

import static io.github.ititus.tictactoe.lib.Mark.CROSS;
import static io.github.ititus.tictactoe.lib.Mark.NONE;
import static io.github.ititus.tictactoe.lib.Pos.*;
import static io.github.ititus.tictactoe.lib.board.Board.swapMark;
import static io.github.ititus.tictactoe.lib.board.Board.swapStartingMark;
import static io.github.ititus.tictactoe.lib.player.AIPlayerTest.*;
import static org.assertj.core.api.Assertions.assertThat;

class AIPlayerCenterStartTest {

    @Test
    @DisplayName("Center -> Top (Left)")
    void testTurn1() {
        assertThat(next(5)).isIn(ALL); // 51, 52
    }

    @Nested
    class CornerResponse {

        @SuppressWarnings("unused")
        static Stream<Arguments> testNext() {
            return Stream.of(
                    args(51, BOTTOM_RIGHT),

                    args(135, BOTTOM_RIGHT),
                    args(195, TOP_RIGHT, BOTTOM_LEFT),
                    args(215, BOTTOM),
                    args(275, BOTTOM)
            );
        }

        @SuppressWarnings("unused")
        static Stream<Arguments> testWinner() {
            return Stream.of(
                    args(51, NONE),

                    args(135, NONE),
                    args(195, NONE),
                    args(215, NONE), // a circle win is possible against non-perfect players but unlikely
                    args(275, NONE)
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
    class SideResponse {

        @SuppressWarnings("unused")
        static Stream<Arguments> testNext() {
            return Stream.of(
                    args(52, TOP_LEFT, TOP_RIGHT, LEFT, RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT),

                    args(125, BOTTOM_RIGHT),
                    args(165, BOTTOM_RIGHT),
                    args(245, BOTTOM),
                    args(285, TOP_LEFT, TOP_RIGHT) // this allows a possible circle win
            );
        }

        @SuppressWarnings("unused")
        static Stream<Arguments> testWinner() {
            return Stream.of(
                    args(52, CROSS),

                    args(125, CROSS),
                    args(165, CROSS),
                    args(245, CROSS),
                    args(285, NONE) // a circle win is possible against non-perfect players but unlikely
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
