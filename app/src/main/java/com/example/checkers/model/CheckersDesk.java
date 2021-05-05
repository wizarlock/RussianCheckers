package com.example.checkers.model;

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
        void onCheckerAdded(Cell position, Checker checker);

        void onCheckerMoved(Cell from, Cell to, Checker checker);

        void onCheckerRemoved(Cell from);
    }

    private OnCheckerActionListener onCheckerActionListener;
    public static final int rows = 8;
    public static final int columns = 8;

    private final List<List<Checker>> checkers;

    public CheckersDesk() {
        checkers = new ArrayList<>(rows);

        for (int i = 0; i < rows; i++) {
            checkers.add(i, new ArrayList<>(columns));
            for (int j = 0; j < columns; j++) {
                checkers.get(i).add(j, null);
            }
        }
    }

    public void initDesk() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < columns; j++) {
                if ((i + j) % 2 != 0) {
                    Checker checker = new Checker(Colors.BLACK);
                    checkers.get(i).add(j, checker);
                    onCheckerActionListener.onCheckerAdded(new Cell(i, j), checker);
                }
            }
        }
        for (int i = rows - 1; i > rows - 4; i--) {
            for (int j = 0; j < columns; j++) {
                if ((i + j) % 2 != 0) {
                    Checker checker = new Checker(Colors.WHITE);
                    checkers.get(i).add(j, checker);
                    onCheckerActionListener.onCheckerAdded(new Cell(i, j), checker);
                }
            }
        }
    }

    public boolean hasChecker(Cell cell) {
        return checkers.get(cell.getY()).get(cell.getX()) != null;
    }

    public boolean cellExist(Cell position) {
        return position.getX() <= 7 && position.getX() >= 0 && position.getY() <= 7 && position.getY() >= 0;
    }

    public List<Cell> possibleWays(Cell position, CheckersDesk.Checker checker) {
        boolean flag = false;
        List<Cell> possibleWays = new ArrayList<>();
        Map<Cell, Cell> requiredMoves = new HashMap<>();
        if (cellExist(new Cell(position.getY() + 1, position.getX() + 1))) {
            if (!hasChecker(new Cell(position.getY() + 1, position.getX() + 1))) {
                if (checker.getColor() == Colors.BLACK)
                    possibleWays.add(new Cell(position.getY() + 1, position.getX() + 1));
            } else {
                if (checkers.get(position.getY() + 1).get(position.getX() + 1).getColor() != checker.getColor())
                    if (cellExist(new Cell(position.getY() + 2, position.getX() + 2)))
                        if (!hasChecker(new Cell(position.getY() + 2, position.getX() + 2)))
                            requiredMoves.put(new Cell(position.getY() + 2, position.getX() + 2), new Cell (position.getY() + 1, position.getX() + 1));
            }
        }
        if (cellExist(new Cell(position.getY() - 1, position.getX() + 1))) {
            if (!hasChecker(new Cell(position.getY() - 1, position.getX() + 1))) {
                if (checker.getColor() == Colors.BLACK)
                    possibleWays.add(new Cell(position.getY() - 1, position.getX() + 1));
            } else {
                if (checkers.get(position.getY() - 1).get(position.getX() + 1).getColor() != checker.getColor())
                    if (cellExist(new Cell(position.getY() - 2, position.getX() + 2)))
                        if (!hasChecker(new Cell(position.getY() - 2, position.getX() + 2)))
                            requiredMoves.put(new Cell(position.getY() - 2, position.getX() + 2), new Cell(position.getY() - 1, position.getX() + 1));
            }
        }
        if (cellExist(new Cell(position.getY() + 1, position.getX() - 1))) {
            if (!hasChecker(new Cell(position.getY() + 1, position.getX() - 1))) {
                if (checker.getColor() == Colors.WHITE)
                    possibleWays.add(new Cell(position.getY() + 1, position.getX() - 1));
            } else {
                if (checkers.get(position.getY() + 1).get(position.getX() - 1).getColor() != checker.getColor())
                    if (cellExist(new Cell(position.getY() + 2, position.getX() - 2)))
                        if (!hasChecker(new Cell(position.getY() + 2, position.getX() - 2)))
                            requiredMoves.put(new Cell(position.getY() + 2, position.getX() - 2), new Cell(position.getY() + 1, position.getX() - 1));
            }
        }
        if (cellExist(new Cell(position.getY() - 1, position.getX() - 1))) {
            if (!hasChecker(new Cell(position.getY() - 1, position.getX() - 1))) {
                if (checker.getColor() == Colors.WHITE)
                    possibleWays.add(new Cell(position.getY() - 1, position.getX() - 1));
            } else {
                if (checkers.get(position.getY() - 1).get(position.getX() - 1).getColor() != checker.getColor())
                    if (cellExist(new Cell(position.getY() - 2, position.getX() - 2)))
                        if (!hasChecker(new Cell(position.getY() - 2, position.getX() - 2)))
                            requiredMoves.put(new Cell(position.getY() - 2, position.getX() - 2), new Cell(position.getY() - 1, position.getX() - 1));
            }
        }
        if (requiredMoves.size() == 0) return possibleWays;


}



