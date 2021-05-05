package com.example.checkers.model;

public class Cell {
    private final int x;
    private final int y;

    public Cell(int y, int x) {
        this.y = y;
        this.x = x;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}
