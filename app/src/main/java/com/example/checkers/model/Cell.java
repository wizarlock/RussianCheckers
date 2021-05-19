package com.example.checkers.model;

import androidx.annotation.Nullable;

public class Cell {
    private final int x;
    private final int y;
    private CheckersDesk.Checker checker;

    public Cell(int y, int x, CheckersDesk.Checker checker) {
        this.y = y;
        this.x = x;
        this.checker = checker;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public CheckersDesk.Checker getChecker() {
        return checker;
    }

    public void setChecker(CheckersDesk.Checker checker) {
        this.checker = checker;
    }

    public static boolean cellExist(Cell cell) {
        return cell.getX() <= 7 && cell.getX() >= 0 && cell.getY() <= 7 && cell.getY() >= 0;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (!(other instanceof Cell)) return false;
        if (this.getX() != ((Cell) other).getX()) return false;
        return this.getY() == ((Cell) other).getY();
    }
}

