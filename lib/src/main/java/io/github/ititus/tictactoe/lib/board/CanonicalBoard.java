package io.github.ititus.tictactoe.lib.board;

import io.github.ititus.data.pair.Pair;
import io.github.ititus.tictactoe.lib.Mark;
import io.github.ititus.tictactoe.lib.Pos;
import io.github.ititus.tictactoe.lib.Transform;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.List;

import static io.github.ititus.tictactoe.lib.Mark.CROSS;

public class CanonicalBoard extends AbstractBoard {

    private static final Int2ObjectMap<Board> CANONICAL_BOARDS = getCanonicalBoards(Board.allCrossStart());

    private final Board board;
    private final boolean swap;
    private final TurnCache.Obj<Pair<Board, Transform>> canonicalBoardCache;

    public CanonicalBoard(Board board) {
        this.board = board;
        this.swap = board.getStartingMark() != CROSS;
        this.canonicalBoardCache = new TurnCache.Obj<>(this::findCanonicalBoard);
    }

    private static Int2ObjectMap<Board> getCanonicalBoards(List<Board> boards) {
        Int2ObjectMap<Board> canonicalBoards = new Int2ObjectOpenHashMap<>();
        for (Board board : boards) {
            canonicalBoards.put(board.encodeIgnoringHistory(), board);
        }

        return Int2ObjectMaps.unmodifiable(canonicalBoards);
    }

    public Transform transform() {
        return canonicalBoardCache.get(getTurn()).b();
    }

    public Board canonicalBoard() {
        return canonicalBoardCache.get(getTurn()).a();
    }

    private Pair<Board, Transform> findCanonicalBoard() {
        for (Transform t : Transform.VALUES) {
            int encoded = new TransformedBoard(board, t).encodeIgnoringHistory();
            Board canonical = CANONICAL_BOARDS.get(swap ? Board.swapMarksIgnoringHistory(encoded) : encoded);
            if (canonical != null) {
                return Pair.of(canonical, t);
            }
        }

        throw new RuntimeException();
    }

    @Override
    public Mark get(Pos pos) {
        return canonicalBoard().get(pos);
    }

    @Override
    public Pos getPos(int turn) {
        return canonicalBoard().getPos(turn);
    }

    @Override
    public Mark getStartingMark() {
        return CROSS;
    }

    @Override
    public int getTurn() {
        return board.getTurn();
    }

    @Override
    public int encode() {
        return canonicalBoard().encode();
    }

    @Override
    public int encodeIgnoringHistory() {
        return canonicalBoard().encodeIgnoringHistory();
    }
}
