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

        public Checker(Colors color) {
            this.color = color;
        }

        public Colors getColor() {
            return color;
        }
    }

    public enum Colors {
        WHITE, BLACK
    }

    public interface OnCheckerActionListener {
        void onCheckerAdded(Cell cell);

        void onCheckerMoved(Cell from, Cell to, Checker checker);

        void onCheckerRemoved(Cell cell);
    }

    private OnCheckerActionListener onCheckerActionListener;
    public static final int rows = 8;
    public static final int columns = 8;

    private final List<List<Cell>> cells;

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
                    Checker checker = new Checker(Colors.BLACK);
                    cells.get(i).get(j).setChecker(checker);
                    onCheckerActionListener.onCheckerAdded(new Cell(i, j, checker));
                }
            }
        }
        for (int i = rows - 1; i > rows - 4; i--) {
            for (int j = 0; j < columns; j++) {
                if ((i + j) % 2 != 0) {
                    Checker checker = new Checker(Colors.WHITE);
                    cells.get(i).get(j).setChecker(checker);
                    onCheckerActionListener.onCheckerAdded(new Cell(i, j, checker));
                }
            }
        }
    }

    public boolean cellExist(Cell cell) {
        return cell.getX() <= 7 && cell.getX() >= 0 && cell.getY() <= 7 && cell.getY() >= 0;
    }

    public List<Pair<Cell, Cell>> possibleWays(Cell cell) {
        List<Pair<Cell, Cell>> possibleWays = new ArrayList<>();

        List<Pair<Cell, Cell>> requiredMoves = new ArrayList<>();

        Map<Integer, Pair<Integer, Integer>> coordinates = new HashMap<>();
        coordinates.put(0, new Pair<>(-1, -1));
        coordinates.put(1, new Pair<>(-1, 1));
        coordinates.put(2, new Pair<>(1, -1));
        coordinates.put(3, new Pair<>(1, 1));

        for (Map.Entry<Integer, Pair<Integer, Integer>> entry : coordinates.entrySet()) {
            if (cellExist(new Cell(cell.getY() + entry.getValue().second, cell.getX() + entry.getValue().first, null)))
                if (cells.get(cell.getY() + entry.getValue().second).get(cell.getX() + entry.getValue().first).getChecker() == null) {
                    if ((cell.getChecker().getColor() == Colors.BLACK && entry.getValue().second == 1) ||
                            (cell.getChecker().getColor() == Colors.WHITE && entry.getValue().second == -1))
                        possibleWays.add(new Pair<>(new Cell(cell.getY() + entry.getValue().second, cell.getX() + entry.getValue().first, null), null));
                } else {
                    if (cells.get(cell.getY() + entry.getValue().second).get(cell.getX() + entry.getValue().first).getChecker().getColor() != cell.getChecker().getColor())
                        if (cellExist(new Cell(cell.getY() + 2* entry.getValue().second, cell.getX() + 2* entry.getValue().first,null)))
                            if (cells.get(cell.getY() + 2* entry.getValue().second).get(cell.getX() + 2* entry.getValue().first).getChecker() == null)
                                requiredMoves.add(new Pair<>(new Cell(cell.getY() + 2* entry.getValue().second, cell.getX() + 2* entry.getValue().first,null),
                                        new Cell(cell.getY() + entry.getValue().second, cell.getX() + entry.getValue().first,null)));
                }
        }
        if (requiredMoves.size() == 0) return possibleWays;
        else return requiredMoves;
    }
}



