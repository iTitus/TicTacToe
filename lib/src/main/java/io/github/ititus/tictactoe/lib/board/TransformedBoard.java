package io.github.ititus.tictactoe.lib.board;

import io.github.ititus.tictactoe.lib.Mark;
import io.github.ititus.tictactoe.lib.Pos;
import io.github.ititus.tictactoe.lib.Transform;

public class TransformedBoard extends AbstractBoard {

    private final Board board;
    private final Transform transform;

    public TransformedBoard(Board board, Transform transform) {
        this.board = board;
        this.transform = transform;
    }

    public Transform transform() {
        return transform;
    }

    @Override
    public Mark get(Pos pos) {
        return board.get(transform.transform(pos));
    }

    @Override
    public Pos getPos(int turn) {
        return transform.inverseTransform(board.getPos(turn));
    }

    @Override
    public Mark getStartingMark() {
        return board.getStartingMark();
    }

    @Override
    public Mark getCurrentMark() {
        return board.getCurrentMark();
    }

    @Override
    public int getTurn() {
        return board.getTurn();
    }

    @Override
    public int encode() {
        if (transform == Transform.IDENTITY) {
            return board.encode();
        }

        return super.encode();
    }

    @Override
    public int encodeIgnoringHistory() {
        if (transform == Transform.IDENTITY) {
            return board.encodeIgnoringHistory();
        }

        return super.encodeIgnoringHistory();
    }
}
