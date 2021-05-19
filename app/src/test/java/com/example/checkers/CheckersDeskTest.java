package com.example.checkers;

import android.util.Pair;

import com.example.checkers.model.Cell;
import com.example.checkers.model.CheckersDesk;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CheckersDeskTest {
    CheckersDesk desk = new CheckersDesk();

    private List<List<Cell>> newCells() {
        List<List<Cell>> newCells = new ArrayList<>(8);
        for (int i = 0; i < 8; i++) {
            newCells.add(i, new ArrayList<>(8));
            for (int j = 0; j < 8; j++) {
                newCells.get(i).add(j, new Cell(i, j, null));
            }
        }
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 != 0) {
                    CheckersDesk.Checker checker = null;
                    if (i < 3) checker = new CheckersDesk.Checker(CheckersDesk.Colors.BLACK, false);
                    else if (i > 4)
                        checker = new CheckersDesk.Checker(CheckersDesk.Colors.WHITE, false);
                    if (checker != null) newCells.get(i).get(j).setChecker(checker);
                }
            }
        return newCells;
    }

    private boolean assertContentEquals(List<List<Cell>> list1, List<List<Cell>> list2) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (!list1.get(i).get(j).equals(list2.get(i).get(j))) return false;
            }
        }
        return true;
    }

    private boolean assertPossibleEquals(List<Pair<Cell, Cell>> list1, List<Pair<Cell, Cell>> list2) {
        if (list1.size() != list2.size()) return false;
        for (int i = 0; i < list1.size(); i++)
            if (!list1.get(i).first.equals(list2.get(i).first)) return false;
        return true;
    }
    private boolean assertRequiredEquals(List<Pair<Cell, Cell>> list1, List<Pair<Cell, Cell>> list2) {
        if (list1.size() != list2.size()) return false;
        for (int i = 0; i < list1.size(); i++)
            if (!list1.get(i).first.equals(list2.get(i).first) || !list1.get(i).second.equals(list2.get(i).second))
                return false;
        return true;
    }

    @Test
    public void checkersDesk() {
        List<List<Cell>> newCells = new ArrayList<>(8);
        for (int i = 0; i < 8; i++) {
            newCells.add(i, new ArrayList<>(8));
            for (int j = 0; j < 8; j++) {
                newCells.get(i).add(j, new Cell(i, j, null));
            }
        }
        desk.checkersDesk();
        assertTrue(assertContentEquals(newCells, CheckersDesk.cells));
    }

    @Test
    public void initDesk() {
        List<List<Cell>> cells = newCells();
        desk.checkersDesk();
        desk.initDesk();
        assertTrue(assertContentEquals(cells, CheckersDesk.cells));
    }

    @Test
    public void CellExist() {
        assertTrue(Cell.cellExist(new Cell(0, 0, null)));
        assertTrue(Cell.cellExist(new Cell(5, 7, null)));
        assertTrue(Cell.cellExist(new Cell(3, 6, null)));
        assertTrue(Cell.cellExist(new Cell(2, 2, null)));
        assertTrue(Cell.cellExist(new Cell(1, 4, null)));
        assertFalse(Cell.cellExist(new Cell(0, 8, null)));
        assertFalse(Cell.cellExist(new Cell(9, 6, null)));
        assertFalse(Cell.cellExist(new Cell(-1, 5, null)));
        assertFalse(Cell.cellExist(new Cell(4, 10, null)));
        assertFalse(Cell.cellExist(new Cell(5, -5, null)));
    }

    @Test
    public void checkerMoving() {
        List<List<Cell>> newCells = newCells();
        desk.checkersDesk();
        desk.initDesk();

        newCells.get(5).get(6).setChecker(null);
        newCells.get(4).get(7).setChecker(new CheckersDesk.Checker(CheckersDesk.Colors.WHITE, false));
        desk.checkerMoving(CheckersDesk.cells.get(5).get(6), CheckersDesk.cells.get(4).get(7));
        assertTrue(assertContentEquals(newCells, CheckersDesk.cells));

        newCells.get(4).get(7).setChecker(null);
        newCells.get(5).get(6).setChecker(new CheckersDesk.Checker(CheckersDesk.Colors.WHITE, false));
        desk.checkerMoving(CheckersDesk.cells.get(4).get(7), CheckersDesk.cells.get(5).get(6));

        newCells.get(2).get(1).setChecker(null);
        newCells.get(3).get(0).setChecker(new CheckersDesk.Checker(CheckersDesk.Colors.BLACK, false));
        desk.checkerMoving(CheckersDesk.cells.get(2).get(1), CheckersDesk.cells.get(3).get(0));
        assertTrue(assertContentEquals(newCells, CheckersDesk.cells));

        newCells.get(3).get(0).setChecker(null);
        newCells.get(2).get(1).setChecker(new CheckersDesk.Checker(CheckersDesk.Colors.BLACK, false));
        desk.checkerMoving(CheckersDesk.cells.get(3).get(0), CheckersDesk.cells.get(2).get(1));

        newCells.get(5).get(0).setChecker(null);
        newCells.get(4).get(1).setChecker(new CheckersDesk.Checker(CheckersDesk.Colors.WHITE, false));
        desk.checkerMoving(CheckersDesk.cells.get(0).get(1), CheckersDesk.cells.get(3).get(2));
        assertFalse(assertContentEquals(newCells, CheckersDesk.cells));

        desk.checkerMoving(CheckersDesk.cells.get(3).get(2), CheckersDesk.cells.get(0).get(1));
    }

    @Test
    public void possibleWays() {
        desk.checkersDesk();
        desk.initDesk();
        List<Pair<Cell, Cell>> list1 = desk.possibleWays(CheckersDesk.cells.get(5).get(6), false);
        List<Pair<Cell, Cell>> list = new ArrayList<>();

        list.add(new Pair<>(new Cell(4, 7, null), null));
        list.add(new Pair<>(new Cell(4, 5, null), null));
        assertPossibleEquals(list1, list);
        assertTrue(assertPossibleEquals(desk.possibleWays(CheckersDesk.cells.get(5).get(6), false), list));
        list.clear();

        list.add(new Pair<>(new Cell(3, 6, null), null));
        assertTrue(assertPossibleEquals(desk.possibleWays(CheckersDesk.cells.get(2).get(7), true), list));
        list.clear();

        list.add(new Pair<>(new Cell(3, 0, null), null));
        assertFalse(assertPossibleEquals(desk.possibleWays(CheckersDesk.cells.get(2).get(7), true), list));
        list.clear();

        list.add(new Pair<>(new Cell(4, 1, null), null));
        assertFalse(assertPossibleEquals(desk.possibleWays(CheckersDesk.cells.get(5).get(2), false), list));
    }

    @Test
    public void requiredMoves() {
        desk.checkersDesk();
        desk.initDesk();
        desk.checkerMoving(CheckersDesk.cells.get(5).get(6), CheckersDesk.cells.get(4).get(5));
        desk.checkerMoving(CheckersDesk.cells.get(2).get(7), CheckersDesk.cells.get(3).get(6));

        List<Pair<Cell, Cell>> list = new ArrayList<>();

        list.add(new Pair<>(new Cell(2, 7, null), new Cell(4, 5, CheckersDesk.cells.get(4).get(5).getChecker())));
        assertTrue(assertRequiredEquals(desk.requiredMoves(false), list));
        list.clear();
    }

    @Test
    public void becomingQueen() {
        List<List<Cell>> cells = new ArrayList<>(8);
        for (int i = 0; i < 8; i++) {
            cells.add(i, new ArrayList<>(8));
            for (int j = 0; j < 8; j++) {
                cells.get(i).add(j, new Cell(i, j, null));
            }
        }
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 != 0) {
                    CheckersDesk.Checker checker = null;
                    if (i < 3) checker = new CheckersDesk.Checker(CheckersDesk.Colors.BLACK, false);
                    else if (i > 4)
                        checker = new CheckersDesk.Checker(CheckersDesk.Colors.WHITE, false);
                    if (checker != null) cells.get(i).get(j).setChecker(checker);
                }
            }
        desk.checkersDesk();
        desk.initDesk();
        cells.get(0).get(5).setChecker(null);
    }
}