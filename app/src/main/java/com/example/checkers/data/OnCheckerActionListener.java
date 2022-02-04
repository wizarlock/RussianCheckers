package com.example.checkers.data;

import android.view.View;

import com.example.checkers.model.Cell;

import java.util.List;
import java.util.Map;

public interface OnCheckerActionListener {
    void onCheckerAdded(Cell cell);

    void onQueenAdded(Cell cell);

    void onCheckerMoved(Cell from, Cell to);

    void onCheckerRemoved(Cell cell);

    List<View> colorForMoves(List<Map<Cell, Cell>> pairs, View view, List<List<Cell>> cells, Cell cell);

    void boardClear(List<View> views);
}
