package io.github.ititus.tictactoe.lib.player;

import io.github.ititus.tictactoe.lib.Mark;
import io.github.ititus.tictactoe.lib.Pos;
import io.github.ititus.tictactoe.lib.board.Board;
import io.github.ititus.tictactoe.lib.board.CanonicalBoard;

import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

import static io.github.ititus.tictactoe.lib.Pos.*;

public class HeuristicAIPlayer extends AbstractPlayer {

    private final AICapabilities capabilities;

    public HeuristicAIPlayer(Board board, Mark mark, AICapabilities capabilities) {
        super(new CanonicalBoard(board), board.getStartingMark() == Mark.CROSS ? mark : mark.enemy());
        this.capabilities = capabilities;
    }

    @Override
    public Pos nextTurn() {
        Pos pos = switch (board.getTurn()) {
            case 0 -> first();
            case 1 -> reactToFirst();
            case 2 -> reactToSecond();
            default -> normal();
        };
        return ((CanonicalBoard) board).transform().transform(pos);
    }

    Pos first() {
        return capabilities.blockFork() ? TOP_LEFT : randPos();
    }

    Pos reactToFirst() {
        if (!capabilities.blockFork()) {
            return randPos();
        }

        return switch (board.getPos(0)) {
            case TOP_LEFT -> CENTER; // safe draw
            case TOP -> BOTTOM; // 158; undefined
            case CENTER -> TOP_LEFT; // undefined, but loss possible
            default -> throw new IllegalStateException();
        };
    }

    Pos reactToSecond() {
        if (!capabilities.blockFork()) {
            return randPos();
        }

        return switch (board.getPos(0)) {
            case TOP_LEFT -> switch (board.getPos(1)) {
                case TOP_LEFT, LEFT, BOTTOM_LEFT, BOTTOM -> throw new IllegalStateException();
                case TOP -> LEFT; // 457; safe win
                case TOP_RIGHT -> LEFT; // 479; safe win
                case CENTER -> RIGHT; // (6/8)9; undecided
                case RIGHT -> TOP_RIGHT; // 57; safe win
                case BOTTOM_RIGHT -> TOP_RIGHT; // (3/7); safe win
            };
            case TOP -> switch (board.getPos(1)) {
                case TOP, TOP_RIGHT, RIGHT, BOTTOM_RIGHT -> throw new IllegalStateException();
                case TOP_LEFT -> BOTTOM_LEFT; // 479; undecided
                case LEFT -> TOP_LEFT; // 15; safe win
                case CENTER -> LEFT; // (4/6)(7/9); undecided
                case BOTTOM_LEFT -> TOP_LEFT; // safe win
                case BOTTOM -> LEFT; // (4/6)5(7/9); undecided
            };
            case CENTER -> switch (board.getPos(1)) {
                case TOP_RIGHT, LEFT, CENTER, RIGHT, BOTTOM_LEFT, BOTTOM, BOTTOM_RIGHT -> throw new IllegalStateException();
                case TOP_LEFT -> BOTTOM_RIGHT; // undecided
                case TOP -> TOP_LEFT; // (1/3)(4/6)(7/9); safe win
            };
            default -> throw new IllegalStateException();
        };
    }

    private Optional<Pos> overrideForkDefense() {
        Pos pos = switch (board.encode()) {
            case 132 -> RIGHT; // or BOTTOM_RIGHT
            case 156 -> BOTTOM_RIGHT;
            case 196 -> CENTER;
            case 214 -> RIGHT; // or BOTTOM
            case 216 -> BOTTOM_LEFT;
            case 285 -> TOP_LEFT; // or TOP_RIGHT
            default -> null;
        };
        return Optional.ofNullable(pos);
    }

    Pos normal() {
        Optional<Pos> pos = Optional.empty();

        if (capabilities.win()) {
            pos = pos.or(this::findWinningPos);
        }

        if (capabilities.block()) {
            pos = pos.or(this::findEnemyWinningPos);
        }

        if (capabilities.fork()) {
            pos = pos.or(this::findPotentialFork);
        }

        if (capabilities.blockFork()) {
            pos = pos.or(this::overrideForkDefense);
            pos = pos.or(this::findPotentialEnemyFork);
            pos = pos.or(() -> tryPos(CENTER));
            pos = pos.or(this::findOppositeCorner);
            pos = pos.or(() -> findFirstFree(CORNERS.stream()));
            pos = pos.or(() -> findFirstFree(SIDES.stream()));
        }

        return pos.orElseGet(this::randPos);
    }

    Optional<Pos> findPotentialFork() {
        return board.emptyStream()
                .filter(emptyPos -> findPotentialWins(emptyPos, false).skip(1).findAny().isPresent())
                .findFirst();
    }

    Optional<Pos> findPotentialEnemyFork() {
        List<Pos> empty = board.empty();

        SortedSet<Pos> potentialEnemyForks = new TreeSet<>();
        for (Pos emptyPos : empty) {
            if (findPotentialWins(emptyPos, true).skip(1).findAny().isPresent()) { // check if there are multiple possible wins
                potentialEnemyForks.add(emptyPos);
            }
        }

        if (potentialEnemyForks.isEmpty()) {
            return Optional.empty();
        } else if (potentialEnemyForks.size() == 1) {
            return Optional.of(potentialEnemyForks.first());
        }

        // there are multiple possible forks for the enemy, so try to force them to block

        // find positions where we can force a block without letting the enemy create a fork while blocking
        SortedSet<Pos> potentialWinsSafe = new TreeSet<>();
        for (Pos emptyPos : empty) {
            List<Pos> potentialWins = findPotentialWins(emptyPos, false).toList();
            if (!potentialWins.isEmpty() && potentialWins.stream().noneMatch(potentialEnemyForks::contains)) {
                potentialWinsSafe.add(emptyPos);
            }
        }

        if (potentialWinsSafe.isEmpty()) {
            // there are no ways to force the enemy to block, just block one of the potential forks and hope for the best
            return Optional.of(potentialEnemyForks.first());
        }

        // blocking a fork AND forcing them to block is very good
        for (Pos potentialWin : potentialWinsSafe) {
            if (potentialEnemyForks.contains(potentialWin)) {
                return Optional.of(potentialWin);
            }
        }

        // the next best thing: force them to block somewhere else
        return Optional.of(potentialWinsSafe.first());
    }

    Optional<Pos> findOppositeCorner() {
        Mark enemy = mark.enemy();
        Optional<Pos> pos = Optional.empty();

        if (board.get(TOP_LEFT) == enemy) {
            pos = pos.or(() -> tryPos(BOTTOM_RIGHT));
        }

        if (pos.isEmpty() && board.get(TOP_RIGHT) == enemy) {
            pos = pos.or(() -> tryPos(BOTTOM_LEFT));
        }

        if (pos.isEmpty() && board.get(BOTTOM_LEFT) == enemy) {
            pos = pos.or(() -> tryPos(TOP_RIGHT));
        }

        if (pos.isEmpty() && board.get(BOTTOM_RIGHT) == enemy) {
            pos = pos.or(() -> tryPos(TOP_LEFT));
        }

        return pos;
    }

    Optional<Pos> findFirstFree(Stream<Pos> stream) {
        return stream
                .map(this::tryPos)
                .flatMap(Optional::stream)
                .findAny();
    }

    private Optional<Pos> tryPos(Pos pos) {
        return Optional.of(pos).filter(p -> board.get(p).empty());
    }

    private Stream<Pos> findPotentialWins(Pos replacementPos, boolean enemy) {
        return LINES.stream()
                .map(line -> {
                    Pos potentialWinningPos = null;
                    for (Pos pos : line) {
                        if (pos.equals(replacementPos)) {
                            continue;
                        }

                        Mark mark = board.get(pos);
                        if (mark.empty()) {
                            if (potentialWinningPos != null) {
                                return Optional.<Pos>empty();
                            }

                            potentialWinningPos = pos;
                        } else if (enemy == (mark == this.mark)) {
                            return Optional.<Pos>empty();
                        }
                    }

                    return Optional.ofNullable(potentialWinningPos);
                })
                .flatMap(Optional::stream);
    }
}
