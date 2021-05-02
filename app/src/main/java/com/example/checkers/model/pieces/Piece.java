package com.example.checkers.model.pieces;

public class Piece {
    private final boolean color;
    public Piece(boolean color){
        this.color = color;
    }
    public boolean getColor(){
        return this.color;
    }
}
