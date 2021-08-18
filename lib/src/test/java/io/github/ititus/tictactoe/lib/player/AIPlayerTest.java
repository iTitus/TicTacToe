package io.github.ititus.tictactoe.lib.player;

import io.github.ititus.tictactoe.lib.Game;
import io.github.ititus.tictactoe.lib.Mark;
import io.github.ititus.tictactoe.lib.Pos;
import io.github.ititus.tictactoe.lib.board.Board;
import io.github.ititus.tictactoe.lib.board.SilentBoardPrinter;
import io.github.ititus.tictactoe.lib.board.WritableBoard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;

import static io.github.ititus.tictactoe.lib.Mark.CIRCLE;
import static io.github.ititus.tictactoe.lib.Mark.CROSS;
import static io.github.ititus.tictactoe.lib.Pos.ALL;
import static io.github.ititus.tictactoe.lib.player.AICapabilities.PERFECT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class AIPlayerTest {

    static Mark winner(int startingBoard) {
        WritableBoard board = WritableBoard.of(startingBoard);
        Game game = new Game(
                board,
                /*new SimulationAIPlayer(board, CROSS, AICapabilities.builder().level(BLOCK).build()), //*/ new HeuristicAIPlayer(board, CROSS, AICapabilities.builder().level(PERFECT).build()),
                /*new SimulationAIPlayer(board, CIRCLE, AICapabilities.builder().level(BLOCK).build()), //*/ new HeuristicAIPlayer(board, CIRCLE, AICapabilities.builder().level(PERFECT).build()),
                SilentBoardPrinter.INSTANCE
        );
        return game.run();
    }

    static Pos next(int startingBoard) {
        Board board = WritableBoard.of(startingBoard);
        Player player = /*new SimulationAIPlayer(board, CROSS, AICapabilities.builder().level(BLOCK).build()); //*/ new HeuristicAIPlayer(board, board.getCurrentMark(), AICapabilities.builder().level(PERFECT).build());
        return player.nextTurn();
    }

    static Arguments args(int startingBoard, Mark expected) {
        return arguments(startingBoard, expected);
    }

    static Arguments args(int startingBoard, Pos... expected) {
        return arguments(startingBoard, List.of(expected));
    }

    @Test
    @DisplayName("Start")
    void testTurn0_1() {
        assertThat(next(0)).isIn(ALL);
    }

    @Test
    @DisplayName("Start Flipped")
    void testTurn0_2() {
        assertThat(next(Integer.MIN_VALUE)).isIn(ALL);
    }
}
