package io.github.ititus.tictactoe.lib.player;

import io.github.ititus.tictactoe.lib.Mark;
import io.github.ititus.tictactoe.lib.Pos;
import io.github.ititus.tictactoe.lib.board.Board;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static io.github.ititus.tictactoe.lib.Pos.LINES;

public abstract class AbstractPlayer implements Player {

    protected final Board board;
    protected final Mark mark;

    protected AbstractPlayer(Board board, Mark mark) {
        this.board = board;
        this.mark = mark;
    }

    Pos randPos() {
        Pos pos;
        do
        {
            pos = Pos.of(ThreadLocalRandom.current().nextInt(Board.SIZE * Board.SIZE));
        } while (board.get(pos).notEmpty());

        return pos;
    }

    Optional<Pos> findWinningPos() {
        return findWinningPosInternal(false);
    }

    Optional<Pos> findEnemyWinningPos() {
        return findWinningPosInternal(true);
    }

    private Optional<Pos> findWinningPosInternal(boolean enemy) {
        return LINES.stream()
                .map(line -> findWinningPosInternal(line, enemy))
                .flatMap(Optional::stream)
                .findAny();
    }

    private Optional<Pos> findWinningPosInternal(List<Pos> line, boolean enemy) {
        Pos winningPos = null;
        for (Pos pos : line) {
            Mark mark = board.get(pos);
            if (mark.empty()) {
                if (winningPos != null) {
                    return Optional.empty();
                }

                winningPos = pos;
            } else if (enemy == (mark == this.mark)) {
                return Optional.empty();
            }
        }

        return Optional.ofNullable(winningPos);
    }
}
