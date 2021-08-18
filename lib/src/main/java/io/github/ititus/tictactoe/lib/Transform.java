package io.github.ititus.tictactoe.lib;

import io.github.ititus.tictactoe.lib.board.Board;
import io.github.ititus.tictactoe.lib.board.TransformedBoard;

import java.util.List;
import java.util.function.UnaryOperator;

public enum Transform {

    IDENTITY(UnaryOperator.identity()) {
        @Override
        public Transform inverse() {
            return IDENTITY;
        }

        @Override
        public Transform compose(Transform before) {
            return before;
        }
    },
    ROTATE_1(Pos::rotateCCW) {
        @Override
        public Transform inverse() {
            return ROTATE_3;
        }

        @Override
        public Transform compose(Transform before) {
            return switch (before) {
                case IDENTITY -> ROTATE_1;
                case ROTATE_1 -> ROTATE_2;
                case ROTATE_2 -> ROTATE_3;
                case ROTATE_3 -> IDENTITY;
                case MIRROR_X -> MIRROR_DIAG_2;
                case MIRROR_Y -> MIRROR_DIAG_1;
                case MIRROR_DIAG_1 -> MIRROR_X;
                case MIRROR_DIAG_2 -> MIRROR_Y;
            };
        }
    },
    ROTATE_2(Pos::mirrorCenter) {
        @Override
        public Transform inverse() {
            return ROTATE_2;
        }

        @Override
        public Transform compose(Transform before) {
            return switch (before) {
                case IDENTITY -> ROTATE_2;
                case ROTATE_1 -> ROTATE_3;
                case ROTATE_2 -> IDENTITY;
                case ROTATE_3 -> ROTATE_1;
                case MIRROR_X -> MIRROR_Y;
                case MIRROR_Y -> MIRROR_X;
                case MIRROR_DIAG_1 -> MIRROR_DIAG_2;
                case MIRROR_DIAG_2 -> MIRROR_DIAG_1;
            };
        }
    },
    ROTATE_3(Pos::rotateCW) {
        @Override
        public Transform inverse() {
            return ROTATE_1;
        }

        @Override
        public Transform compose(Transform before) {
            return switch (before) {
                case IDENTITY -> ROTATE_3;
                case ROTATE_1 -> IDENTITY;
                case ROTATE_2 -> ROTATE_1;
                case ROTATE_3 -> ROTATE_2;
                case MIRROR_X -> MIRROR_DIAG_1;
                case MIRROR_Y -> MIRROR_DIAG_2;
                case MIRROR_DIAG_1 -> MIRROR_Y;
                case MIRROR_DIAG_2 -> MIRROR_X;
            };
        }
    },
    MIRROR_X(Pos::mirrorX) {
        @Override
        public Transform inverse() {
            return MIRROR_X;
        }

        @Override
        public Transform compose(Transform before) {
            return switch (before) {
                case IDENTITY -> MIRROR_X;
                case ROTATE_1 -> MIRROR_DIAG_1;
                case ROTATE_2 -> MIRROR_Y;
                case ROTATE_3 -> MIRROR_DIAG_2;
                case MIRROR_X -> IDENTITY;
                case MIRROR_Y -> ROTATE_2;
                case MIRROR_DIAG_1 -> ROTATE_1;
                case MIRROR_DIAG_2 -> ROTATE_3;
            };
        }
    },
    MIRROR_Y(Pos::mirrorY) {
        @Override
        public Transform inverse() {
            return MIRROR_Y;
        }

        @Override
        public Transform compose(Transform before) {
            return switch (before) {
                case IDENTITY -> MIRROR_Y;
                case ROTATE_1 -> MIRROR_DIAG_2;
                case ROTATE_2 -> MIRROR_X;
                case ROTATE_3 -> MIRROR_DIAG_1;
                case MIRROR_X -> ROTATE_2;
                case MIRROR_Y -> IDENTITY;
                case MIRROR_DIAG_1 -> ROTATE_3;
                case MIRROR_DIAG_2 -> ROTATE_1;
            };
        }
    },
    MIRROR_DIAG_1(Pos::mirrorDiag1) {
        @Override
        public Transform inverse() {
            return MIRROR_DIAG_1;
        }

        @Override
        public Transform compose(Transform before) {
            return switch (before) {
                case IDENTITY -> MIRROR_DIAG_1;
                case ROTATE_1 -> MIRROR_Y;
                case ROTATE_2 -> MIRROR_DIAG_2;
                case ROTATE_3 -> MIRROR_X;
                case MIRROR_X -> ROTATE_3;
                case MIRROR_Y -> ROTATE_1;
                case MIRROR_DIAG_1 -> IDENTITY;
                case MIRROR_DIAG_2 -> ROTATE_2;
            };
        }
    },
    MIRROR_DIAG_2(Pos::mirrorDiag2) {
        @Override
        public Transform inverse() {
            return MIRROR_DIAG_2;
        }

        @Override
        public Transform compose(Transform before) {
            return switch (before) {
                case IDENTITY -> MIRROR_DIAG_2;
                case ROTATE_1 -> MIRROR_X;
                case ROTATE_2 -> MIRROR_DIAG_1;
                case ROTATE_3 -> MIRROR_Y;
                case MIRROR_X -> ROTATE_1;
                case MIRROR_Y -> ROTATE_3;
                case MIRROR_DIAG_1 -> ROTATE_2;
                case MIRROR_DIAG_2 -> IDENTITY;
            };
        }
    };

    public static final List<Transform> VALUES = List.of(values());

    private final UnaryOperator<Pos> transform;

    Transform(UnaryOperator<Pos> transform) {
        this.transform = transform;
    }

    public static Transform between(Board from, Board to) {
        if (from == to) {
            return IDENTITY;
        }

        for (Transform t : VALUES) {
            if (!new TransformedBoard(from, t).isEqualToIgnoringHistory(to)) {
                continue;
            }

            return t;
        }

        throw new RuntimeException();
    }

    public Pos transform(Pos pos) {
        return transform.apply(pos);
    }

    public Pos inverseTransform(Pos pos) {
        return inverse().transform(pos);
    }

    public abstract Transform inverse();

    public abstract Transform compose(Transform before);
}
