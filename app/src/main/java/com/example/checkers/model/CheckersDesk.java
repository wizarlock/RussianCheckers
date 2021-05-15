package com.example.checkers.model;

import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckersDesk {

    public OnCheckerActionListener getOnCheckerActionListener() {
        return onCheckerActionListener;
    }

    public void setOnCheckerActionListener(OnCheckerActionListener onCheckerActionListener) {
        this.onCheckerActionListener = onCheckerActionListener;
    }

    public static class Checker {
        private final Colors color;
        private boolean condition;

        public Checker(Colors color, boolean condition) {
            this.color = color;
            this.condition = condition;
        }

        public Colors getColor() {
            return color;
        }

        public void setCondition(Boolean condition) {
            this.condition = condition;
        }

        public boolean getCondition() {
            return condition;
        }
    }

    public enum Colors {
        WHITE, BLACK
    }

    public interface OnCheckerActionListener {
        void onCheckerAdded(Cell cell);

        void onQueenAdded(Cell cell);

        void onCheckerMoved(Cell from, Cell to);

        void onCheckerRemoved(Cell cell);
    }

    private OnCheckerActionListener onCheckerActionListener;
    static final int rows = 8;
    static final int columns = 8;

    public static List<List<Cell>> cells;

    public CheckersDesk() {
        cells = new ArrayList<>(rows);

        for (int i = 0; i < rows; i++) {
            cells.add(i, new ArrayList<>(columns));
            for (int j = 0; j < columns; j++) {
                cells.get(i).add(j, new Cell(i, j, null));
            }
        }
    }

    public void initDesk() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < columns; j++) {
                if ((i + j) % 2 != 0) {
                    Checker checker = new Checker(Colors.BLACK, false);
                    cells.get(i).get(j).setChecker(checker);
                    onCheckerActionListener.onCheckerAdded(new Cell(i, j, checker));
                }
            }
        }
        for (int i = rows - 1; i > rows - 4; i--) {
            for (int j = 0; j < columns; j++) {
                if ((i + j) % 2 != 0) {
                    Checker checker = new Checker(Colors.WHITE, false);
                    cells.get(i).get(j).setChecker(checker);
                    onCheckerActionListener.onCheckerAdded(new Cell(i, j, checker));
                }
            }
        }
    }

    public static boolean cellExist(Cell cell) {
        return cell.getX() <= 7 && cell.getX() >= 0 && cell.getY() <= 7 && cell.getY() >= 0;
    }

    public static boolean becomingQueen(Cell cell) {
        if (cell.getChecker() != null)
            if ((cell.getChecker().getColor() == Colors.BLACK && cell.getY() == 7) || (cell.getChecker().getColor() == Colors.WHITE && cell.getY() == 0)) {
                cells.get(cell.getY()).get(cell.getX()).getChecker().setCondition(true);
                return true;
            }
        return false;
    }

    public static boolean finishGame(boolean whoseMove) {
        if (!whoseMove) {
            for (int i = 0; i < rows; i++)
                for (int j = 0; j < columns; j++) {
                    Cell cell = cells.get(i).get(j);
                    if (cell.getChecker() != null)
                        if (cell.getChecker().getColor() == Colors.WHITE) return false;
                }
        } else {
            for (int i = 0; i < rows; i++)
                for (int j = 0; j < columns; j++) {
                    Cell cell = cells.get(i).get(j);
                    if (cell.getChecker() != null)
                        if (cell.getChecker().getColor() == Colors.BLACK) return false;
                }
        }
        return true;
    }

    public static List<Pair<Cell, Cell>> requiredMoves(boolean whoseMove) {
        Map<Integer, Pair<Integer, Integer>> coordinates = new HashMap<>();
        coordinates.put(0, new Pair<>(-1, -1));
        coordinates.put(1, new Pair<>(-1, 1));
        coordinates.put(2, new Pair<>(1, -1));
        coordinates.put(3, new Pair<>(1, 1));
        int coefficient = 1;

        List<Pair<Cell, Cell>> requiredMoves = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell cell = cells.get(i).get(j);
                if (cell.getChecker() != null)
                    if ((cell.getChecker().getColor() == Colors.BLACK && whoseMove) ||
                            (!whoseMove && cells.get(i).get(j).getChecker().getColor() == Colors.WHITE))
                        if (!cell.getChecker().getCondition()) {
                            for (Map.Entry<Integer, Pair<Integer, Integer>> entry : coordinates.entrySet()) {
                                if (cellExist(new Cell(cell.getY() + 2 * entry.getValue().first, cell.getX() + 2 * entry.getValue().second, null)))
                                    if (cells.get(cell.getY() + entry.getValue().first).get(cell.getX() + entry.getValue().second).getChecker() != null)
                                        if (cells.get(cell.getY() + entry.getValue().first).get(cell.getX() + entry.getValue().second).getChecker().getColor() != cell.getChecker().getColor())
                                            if (cells.get(cell.getY() + 2 * entry.getValue().first).get(cell.getX() + 2 * entry.getValue().second).getChecker() == null)
                                                requiredMoves.add(new Pair<>(cells.get(cell.getY() + 2 * entry.getValue().first).get(cell.getX() + 2 * entry.getValue().second), cell));
                            }
                        } else {
                            for (Map.Entry<Integer, Pair<Integer, Integer>> entry : coordinates.entrySet()) {
                                while (cellExist(new Cell(cell.getY() + (coefficient + 1) * entry.getValue().first, cell.getX() + (coefficient + 1) * entry.getValue().second, null))) {
                                    if (cells.get(cell.getY() + coefficient * entry.getValue().first).get(cell.getX() + coefficient * entry.getValue().second).getChecker() != null) {
                                        if (cells.get(cell.getY() + coefficient * entry.getValue().first).get(cell.getX() + coefficient * entry.getValue().second).getChecker().getColor() != cell.getChecker().getColor()) {
                                            while (cellExist(new Cell(cell.getY() + (coefficient + 1) * entry.getValue().first, cell.getX() + (coefficient + 1) * entry.getValue().second, null))) {
                                                if (cells.get(cell.getY() + (coefficient + 1) * entry.getValue().first).get(cell.getX() + (coefficient + 1) * entry.getValue().second).getChecker() == null) {
                                                    requiredMoves.add(new Pair<>(cells.get(cell.getY() + (coefficient + 1) * entry.getValue().first).get(cell.getX() + (coefficient + 1) * entry.getValue().second), cell));
                                                    coefficient++;
                                                } else break;
                                            }
                                        }
                                        break;
                                    } else coefficient++;
                                }
                                coefficient = 1;
                            }
                        }
            }
        }
        return requiredMoves;
    }

    public static List<Pair<Cell, Cell>> possibleWays(Cell cell, boolean whoseMove) {
        List<Pair<Cell, Cell>> possibleWays = new ArrayList<>();

        Map<Integer, Pair<Integer, Integer>> coordinates = new HashMap<>();
        coordinates.put(0, new Pair<>(-1, -1));
        coordinates.put(1, new Pair<>(-1, 1));
        coordinates.put(2, new Pair<>(1, -1));
        coordinates.put(3, new Pair<>(1, 1));
        int coefficient = 1;

        if (!cell.getChecker().getCondition()) {
            for (Map.Entry<Integer, Pair<Integer, Integer>> entry : coordinates.entrySet()) {
                if (cellExist(new Cell(cell.getY() + entry.getValue().first, cell.getX() + entry.getValue().second, null)))
                    if (cells.get(cell.getY() + entry.getValue().first).get(cell.getX() + entry.getValue().second).getChecker() == null)
                        if ((cell.getChecker().getColor() == Colors.BLACK && entry.getValue().first == 1 && whoseMove) ||
                                (cell.getChecker().getColor() == Colors.WHITE && entry.getValue().first == -1 && !whoseMove))
                            possibleWays.add(new Pair<>(cells.get(cell.getY() + entry.getValue().first).get(cell.getX() + entry.getValue().second), null));
            }
        } else {
            for (Map.Entry<Integer, Pair<Integer, Integer>> entry : coordinates.entrySet()) {
                while (cellExist(new Cell(cell.getY() + coefficient * entry.getValue().first, cell.getX() + coefficient * entry.getValue().second, null))) {
                    if (cells.get(cell.getY() + coefficient * entry.getValue().first).get(cell.getX() + coefficient * entry.getValue().second).getChecker() == null) {
                        if ((cell.getChecker().getColor() == Colors.BLACK && whoseMove) ||
                                (cell.getChecker().getColor() == Colors.WHITE && !whoseMove))
                            possibleWays.add(new Pair<>(cells.get(cell.getY() + coefficient * entry.getValue().first).get(cell.getX() + coefficient * entry.getValue().second), null));
                        coefficient++;
                    } else break;
                }
                coefficient = 1;
            }
        }
        return possibleWays;
    }

    public static List<Pair<Cell, Cell>> canEatMore(Cell cell) {
        Map<Integer, Pair<Integer, Integer>> coordinates = new HashMap<>();
        coordinates.put(0, new Pair<>(-1, -1));
        coordinates.put(1, new Pair<>(-1, 1));
        coordinates.put(2, new Pair<>(1, -1));
        coordinates.put(3, new Pair<>(1, 1));

        List<Pair<Cell, Cell>> possibleWays = new ArrayList<>();
        int coefficient = 1;
        if (!cell.getChecker().getCondition()) {
            for (Map.Entry<Integer, Pair<Integer, Integer>> entry : coordinates.entrySet()) {
                if (cellExist(new Cell(cell.getY() + 2 * entry.getValue().first, cell.getX() + 2 * entry.getValue().second, null)))
                    if (cells.get(cell.getY() + entry.getValue().first).get(cell.getX() + entry.getValue().second).getChecker() != null)
                        if (cells.get(cell.getY() + entry.getValue().first).get(cell.getX() + entry.getValue().second).getChecker().getColor() != cell.getChecker().getColor())
                            if (cells.get(cell.getY() + 2 * entry.getValue().first).get(cell.getX() + 2 * entry.getValue().second).getChecker() == null)
                                possibleWays.add(new Pair<>(cells.get(cell.getY() + 2 * entry.getValue().first).get(cell.getX() + 2 * entry.getValue().second), null));
            }
        } else {
            for (Map.Entry<Integer, Pair<Integer, Integer>> entry : coordinates.entrySet()) {
                while (cellExist(new Cell(cell.getY() + (coefficient + 1) * entry.getValue().first, cell.getX() + (coefficient + 1) * entry.getValue().second, null))) {
                    if (cells.get(cell.getY() + coefficient * entry.getValue().first).get(cell.getX() + coefficient * entry.getValue().second).getChecker() != null) {
                        if (cells.get(cell.getY() + coefficient * entry.getValue().first).get(cell.getX() + coefficient * entry.getValue().second).getChecker().getColor() != cell.getChecker().getColor()) {
                            while (cellExist(new Cell(cell.getY() + (coefficient + 1) * entry.getValue().first, cell.getX() + (coefficient + 1) * entry.getValue().second, null))) {
                                if (cells.get(cell.getY() + (coefficient + 1) * entry.getValue().first).get(cell.getX() + (coefficient + 1) * entry.getValue().second).getChecker() == null) {
                                    possibleWays.add(new Pair<>(cells.get(cell.getY() + (coefficient + 1) * entry.getValue().first).get(cell.getX() + (coefficient + 1) * entry.getValue().second), cell));
                                    coefficient++;
                                } else break;
                            }
                        }
                        break;
                    } else coefficient++;
                }
                coefficient = 1;
            }
        }
        return possibleWays;
    }

    public static boolean checkForMove(boolean whoseMove) {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++) {
                Cell cell = cells.get(i).get(j);
                if (cell.getChecker() != null)
                    if (possibleWays(cell, whoseMove).size() != 0) return false;
            }
        return true;
    }
}



