package io.github.ititus.tictactoe.app;

import io.github.ititus.commons.data.mutable.Mutable;
import io.github.ititus.commons.data.pair.IntIntPair;
import io.github.ititus.commons.data.pair.Pair;
import io.github.ititus.commons.io.PathUtil;
import io.github.ititus.tictactoe.lib.Mark;
import io.github.ititus.tictactoe.lib.Pos;
import io.github.ititus.tictactoe.lib.Transform;
import io.github.ititus.tictactoe.lib.board.Board;
import io.github.ititus.tictactoe.lib.board.TransformedBoard;
import io.github.ititus.tictactoe.lib.board.WritableBoard;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static io.github.ititus.tictactoe.lib.Mark.NONE;
import static io.github.ititus.tictactoe.lib.Pos.LINES;

public class GameTree {

    private static final Int2ObjectMap<Board> CANONICAL_BOARDS = getCanonicalBoards(Board.allCrossStart());

    private final Int2ObjectMap<Board> canonicalBoards;
    private final Graph graph;

    private GameTree() {
        this.canonicalBoards = CANONICAL_BOARDS;
        this.graph = new Graph(new Int2ObjectOpenHashMap<>(), new HashSet<>());
    }

    private static Int2ObjectMap<Board> getCanonicalBoards(List<Board> boards) {
        Int2ObjectMap<Board> canonicalBoards = new Int2ObjectOpenHashMap<>();
        for (Board board : boards) {
            canonicalBoards.put(board.encodeIgnoringHistory(), board);
        }

        return Int2ObjectMaps.unmodifiable(canonicalBoards);
    }

    public static void main(String[] args) {
        new GameTree().run();
    }

    private void run() {
        for (Int2ObjectMap.Entry<Board> entry : canonicalBoards.int2ObjectEntrySet()) {
            addVertex(entry.getIntKey(), entry.getValue());
        }

        for (Int2ObjectMap.Entry<Board> entry : canonicalBoards.int2ObjectEntrySet()) {
            addEdges(entry.getIntKey(), entry.getValue());
        }

        findAndUpdateWinners(graph.vertices().get(0));

        Queue<Vertex> queue = new ArrayDeque<>();
        Vertex root = graph.vertices().get(0);
        queue.add(root);

        while (!queue.isEmpty()) {
            Vertex v = queue.remove();
            writeDot(v.board().encode());
            for (Edge e : v.startingEdges()) {
                queue.add(e.end());
            }
        }

        try {
            Process p = new ProcessBuilder()
                    .command("wsl", "--", "dot", "-Tpng", "-O", "*.dot")
                    .inheritIO()
                    .directory(new File("./trees"))
                    .start();
            System.out.println("Exit: " + p.waitFor());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            Process p = new ProcessBuilder()
                    .command("wsl", "--", "rm", "-f", "*.dot")
                    .inheritIO()
                    .directory(new File("./trees"))
                    .start();
            System.out.println("Exit: " + p.waitFor());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Mark findAndUpdateWinners(Vertex v) {
        Mark winner;
        if (v.startingEdges().isEmpty()) {
            winner = v.board().checkGameEnd().orElseThrow();
        } else {
            boolean hasUndecided = false;
            boolean hasDraw = false;
            boolean hasWin = false;
            boolean hasLoss = false;
            for (Edge out : v.startingEdges()) {
                Mark childWinner = findAndUpdateWinners(out.end());
                if (childWinner == null) {
                    hasUndecided = true;
                } else if (childWinner == NONE) {
                    hasDraw = true;
                } else if (childWinner == v.board().getCurrentMark()) {
                    hasWin = true;
                } else {
                    hasLoss = true;
                }
            }

            if (hasWin) {
                winner = v.board().getCurrentMark();
            } else if (hasUndecided) {
                winner = null;
            } else if (hasDraw) {
                winner = NONE;
            } else if (hasLoss) {
                winner = v.board().getCurrentMark().enemy();
            } else {
                winner = null;
            }
        }

        v.winner().set(winner);
        return winner;
    }

    private void writeDot(int start) {
        Path dir = PathUtil.createOrResolveRealDir(Path.of("./trees"));
        Path p = dir.resolve(start + ".dot");
        try {
            Files.write(p, export(start, graph));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private List<String> export(int start, Graph graph) {
        List<String> lines = new ArrayList<>();
        lines.add("strict digraph GameTree {");

        Queue<Vertex> queue = new ArrayDeque<>();
        Set<Vertex> visitedVertices = new HashSet<>();
        Set<IntIntPair> visitedEdges = new HashSet<>();

        Vertex root = graph.vertices().get(findCanonical(WritableBoard.of(start)).encodeIgnoringHistory());
        queue.add(root);
        visitedVertices.add(root);

        while (!queue.isEmpty()) {
            Vertex v = queue.remove();
            for (Edge e : v.startingEdges()) {
                Vertex w = e.end();
                visitedVertices.add(w);
                visitedEdges.add(Pair.of(v.board().encode(), w.board().encode()));
                queue.add(w);
            }
        }

        visitedVertices.stream()
                .sorted(Comparator.comparingInt((Vertex v) -> v.board().encode()).thenComparingInt(Vertex::encoded))
                .forEachOrdered(v -> {
                    String winnerStr;
                    if (v.winner().isNotNull()) {
                        winnerStr = " [style=filled, fillcolor=";
                        winnerStr += switch (v.winner().get()) {
                            case NONE -> "yellow";
                            case CROSS -> "green";
                            case CIRCLE -> "red";
                        };
                        winnerStr += "]";
                    } else {
                        winnerStr = "";
                    }
                    lines.add("    " + v.board().encode() + winnerStr + ";");
                });


        visitedEdges.stream()
                .sorted(Comparator.comparingInt(IntIntPair::aInt).thenComparingInt(IntIntPair::bInt))
                .forEachOrdered(p -> lines.add("    " + p.aInt() + " -> " + p.bInt() + ";"));

        lines.add("}");

        return lines;
    }

    private void addVertex(int encoded, Board board) {
        graph.vertices().put(encoded, new Vertex(encoded, board, new HashSet<>(), new HashSet<>(), Mutable.empty()));
    }

    private void addEdges(int start, Board startBoard) {
        if (startBoard.checkGameEnd().isEmpty()) {
            for (Pos pos : findValidPositions(startBoard)) {
                addEdge(start, startBoard, pos);
            }
        }
    }

    private List<Pos> findValidPositions(Board board) {
        List<Pos> positions = win(board);
        if (!positions.isEmpty()) {
            return positions;
        }

        positions = block(board);
        if (!positions.isEmpty()) {
            return positions;
        }

        return board.empty();
    }

    private List<Pos> win(Board board) {
        Mark currentMark = board.getCurrentMark();
        return LINES.stream()
                .map(line -> {
                    Pos winningPos = null;
                    for (Pos pos : line) {
                        Mark mark = board.get(pos);
                        if (mark.empty()) {
                            if (winningPos != null) {
                                return Optional.<Pos>empty();
                            }

                            winningPos = pos;
                        } else if (mark != currentMark) {
                            return Optional.<Pos>empty();
                        }
                    }

                    return Optional.ofNullable(winningPos);
                })
                .flatMap(Optional::stream)
                .toList();
    }

    private List<Pos> block(Board board) {
        Mark currentMark = board.getCurrentMark();
        return LINES.stream()
                .map(line -> {
                    Pos winningPos = null;
                    for (Pos pos : line) {
                        Mark mark = board.get(pos);
                        if (mark.empty()) {
                            if (winningPos != null) {
                                return Optional.<Pos>empty();
                            }

                            winningPos = pos;
                        } else if (mark == currentMark) {
                            return Optional.<Pos>empty();
                        }
                    }

                    return Optional.ofNullable(winningPos);
                })
                .flatMap(Optional::stream)
                .toList();
    }

    private void addEdge(int start, Board startBoard, Pos pos) {
        WritableBoard board = WritableBoard.of(startBoard);
        board.place(pos);
        int end = findCanonical(board).encodeIgnoringHistory();

        Vertex startV = graph.vertices().get(start);
        Vertex endV = graph.vertices().get(end);
        Edge edge = new Edge(startV, endV, pos);
        graph.edges().add(edge);
        startV.startingEdges().add(edge);
        endV.endingEdges().add(edge);
    }

    private Board findCanonical(Board board) {
        for (Transform t : Transform.VALUES) {
            int encoded = new TransformedBoard(board, t).encodeIgnoringHistory();
            Board canonical = canonicalBoards.get(encoded);
            if (canonical != null) {
                return canonical;
            }
        }

        throw new RuntimeException();
    }

    private static final record Graph(
            Int2ObjectMap<Vertex> vertices,
            Set<Edge> edges
    ) {
    }

    private static final record Vertex(
            int encoded,
            Board board,
            Set<Edge> startingEdges,
            Set<Edge> endingEdges,
            Mutable<Mark> winner
    ) {

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (!(o instanceof Vertex)) {
                return false;
            }

            return encoded == ((Vertex) o).encoded;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(encoded);
        }

        @Override
        public String toString() {
            return "Vertex[encoded=" + encoded + ", winner=" + winner + "]";
        }
    }

    private static final record Edge(
            Vertex start,
            Vertex end,
            Pos pos
    ) {
    }
}
