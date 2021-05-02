package com.example.checkers.model;

import java.util.ArrayList;
import java.util.List;

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
        void onCheckerRemoved(Cell from, Checker checker);
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
            for (int j  = 0; j < columns; j++) {
                if ((i + j) % 2 != 0) {
                    Checker checker = new Checker(Colors.BLACK);
                    checkers.get(i).add(j, checker);
                    onCheckerActionListener.onCheckerAdded(new Cell(j, i), checker);
                }
            }
        }
        for (int i = rows - 1; i > rows - 4; i--) {
            for (int j  = 0; j < columns; j++) {
                if ((i + j) % 2 != 0) {
                    Checker checker = new Checker(Colors.WHITE);
                    checkers.get(i).set(j, checker);
                    onCheckerActionListener.onCheckerAdded(new Cell(j, i), checker);
                }
            }
        }
    }

    public boolean hasChecker(Cell cell) {
        return checkers.get(cell.getY()).get(cell.getX()) != null;
    }
}

