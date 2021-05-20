package com.example.checkers;

import com.example.checkers.model.Cell;
import com.example.checkers.model.CheckersDesk;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private boolean assertPossibleEquals(List<Map<Cell, Cell>> list1, List<Map<Cell, Cell>> list2) {
        if (list1.size() != list2.size()) return false;
        for (int i = 0; i < list1.size(); i++)
            for (Map.Entry<Cell, Cell> entry : list1.get(i).entrySet())
                for (Map.Entry<Cell, Cell> entry1 : list1.get(i).entrySet()) {
                    if (!entry.getKey().equals(entry1.getKey())) return false;
                }
        return true;
    }

    private boolean assertRequiredEquals(List<Map<Cell, Cell>> list1, List<Map<Cell, Cell>> list2) {
        if (list1.size() != list2.size()) return false;
        for (int i = 0; i < list1.size(); i++)
            for (Map.Entry<Cell, Cell> entry : list1.get(i).entrySet())
                for (Map.Entry<Cell, Cell> entry1 : list1.get(i).entrySet()) {
                    if (!entry.getKey().equals(entry1.getKey()) || !entry.getValue().equals(entry1.getValue()))
                        return false;
                }
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
    public void moving() {
        List<List<Cell>> newCells = newCells();
        desk.checkersDesk();
        desk.initDesk();

        newCells.get(5).get(6).setChecker(null);
        newCells.get(4).get(7).setChecker(new CheckersDesk.Checker(CheckersDesk.Colors.WHITE, false));
        desk.moving(CheckersDesk.cells.get(5).get(6), CheckersDesk.cells.get(4).get(7));
        assertTrue(assertContentEquals(newCells, CheckersDesk.cells));

        newCells.get(2).get(1).setChecker(null);
        newCells.get(3).get(0).setChecker(new CheckersDesk.Checker(CheckersDesk.Colors.BLACK, false));
        desk.moving(CheckersDesk.cells.get(2).get(1), CheckersDesk.cells.get(3).get(0));
        assertTrue(assertContentEquals(newCells, CheckersDesk.cells));

        newCells.get(5).get(0).setChecker(null);
        newCells.get(4).get(1).setChecker(new CheckersDesk.Checker(CheckersDesk.Colors.WHITE, false));
        desk.moving(CheckersDesk.cells.get(0).get(1), CheckersDesk.cells.get(3).get(2));
        assertFalse(assertContentEquals(newCells, CheckersDesk.cells));

    }

    @Test
    public void possibleWays() {
        desk.checkersDesk();
        desk.initDesk();
        List<Map<Cell, Cell>> list = new ArrayList<>();
        Map<Cell, Cell> map1 = new HashMap<>();
        Map<Cell, Cell> map2 = new HashMap<>();

        map1.put(new Cell(4, 7, null), null);
        map2.put(new Cell(4, 5, null), null);
        list.add(map1);
        list.add(map2);

        assertTrue(assertPossibleEquals(desk.possibleWays(CheckersDesk.cells.get(5).get(6), false), list));
        list.clear();
        map1.clear();
        map2.clear();

        map1.put(new Cell(3, 2, null), null);
        map2.put(new Cell(3, 4, null), null);
        list.add(map1);
        list.add(map2);

        assertTrue(assertPossibleEquals(desk.possibleWays(CheckersDesk.cells.get(2).get(3), true), list));
        assertFalse(assertPossibleEquals(desk.possibleWays(CheckersDesk.cells.get(2).get(3), false), list));
        list.clear();
        map1.clear();
        map2.clear();

        map1.put(new Cell(6, 1, null), null);
        list.add(map1);

        assertFalse(assertPossibleEquals(desk.possibleWays(CheckersDesk.cells.get(2).get(3), true), list));
    }

    @Test
    public void requiredMoves() {
        desk.checkersDesk();
        desk.initDesk();
        List<Map<Cell, Cell>> list = new ArrayList<>();
        Map<Cell, Cell> map1 = new HashMap<>();
        Map<Cell, Cell> map2 = new HashMap<>();

        desk.moving(CheckersDesk.cells.get(5).get(6), CheckersDesk.cells.get(4).get(5));
        desk.moving(CheckersDesk.cells.get(2).get(7), CheckersDesk.cells.get(3).get(6));
        map1.put(CheckersDesk.cells.get(2).get(7), CheckersDesk.cells.get(3).get(6));
        list.add(map1);

        assertTrue(assertRequiredEquals(desk.requiredMoves(false), list));
        list.clear();
        map1.clear();

        desk.moving(CheckersDesk.cells.get(2).get(3), CheckersDesk.cells.get(3).get(4));
        map1.put(CheckersDesk.cells.get(2).get(7), CheckersDesk.cells.get(3).get(6));
        map2.put(CheckersDesk.cells.get(2).get(3), CheckersDesk.cells.get(3).get(6));
        list.add(map1);
        list.add(map2);

        assertTrue(assertRequiredEquals(desk.requiredMoves(false), list));
        assertFalse(assertRequiredEquals(desk.requiredMoves(true), list));
    }

    @Test
    public void consumption() {
        desk.checkersDesk();
        desk.initDesk();
        List<List<Cell>> newCells = newCells();

        newCells.get(5).get(6).setChecker(null);
        newCells.get(4).get(5).setChecker(new CheckersDesk.Checker(CheckersDesk.Colors.WHITE, false));
        desk.moving(CheckersDesk.cells.get(5).get(6), CheckersDesk.cells.get(4).get(5));

        newCells.get(2).get(7).setChecker(null);
        newCells.get(3).get(6).setChecker(new CheckersDesk.Checker(CheckersDesk.Colors.BLACK, false));
        desk.moving(CheckersDesk.cells.get(2).get(7), CheckersDesk.cells.get(3).get(6));

        newCells.get(4).get(5).setChecker(null);
        newCells.get(3).get(6).setChecker(null);
        newCells.get(2).get(7).setChecker(new CheckersDesk.Checker(CheckersDesk.Colors.WHITE, false));
        desk.consumption(CheckersDesk.cells.get(4).get(5), CheckersDesk.cells.get(2).get(7));

        assertTrue(assertContentEquals(newCells, CheckersDesk.cells));

        newCells.get(2).get(5).setChecker(null);
        newCells.get(3).get(6).setChecker(new CheckersDesk.Checker(CheckersDesk.Colors.BLACK, false));
        desk.moving(CheckersDesk.cells.get(2).get(5), CheckersDesk.cells.get(3).get(6));

        newCells.get(2).get(7).setChecker(null);
        newCells.get(3).get(6).setChecker(null);
        newCells.get(4).get(5).setChecker(new CheckersDesk.Checker(CheckersDesk.Colors.WHITE, false));
        desk.consumption(CheckersDesk.cells.get(2).get(7), CheckersDesk.cells.get(4).get(5));

        assertTrue(assertContentEquals(newCells, CheckersDesk.cells));
    }

    @Test
    public void becomingQueen() {
        desk.checkersDesk();
        desk.initDesk();
        List<List<Cell>> newCells = newCells();

        newCells.get(5).get(6).setChecker(null);
        newCells.get(4).get(5).setChecker(new CheckersDesk.Checker(CheckersDesk.Colors.WHITE, false));
        desk.moving(CheckersDesk.cells.get(5).get(6), CheckersDesk.cells.get(4).get(5));

        newCells.get(2).get(7).setChecker(null);
        newCells.get(3).get(6).setChecker(new CheckersDesk.Checker(CheckersDesk.Colors.BLACK, false));
        desk.moving(CheckersDesk.cells.get(2).get(7), CheckersDesk.cells.get(3).get(6));

        newCells.get(4).get(5).setChecker(null);
        newCells.get(3).get(6).setChecker(null);
        newCells.get(2).get(7).setChecker(new CheckersDesk.Checker(CheckersDesk.Colors.WHITE, false));
        desk.consumption(CheckersDesk.cells.get(4).get(5), CheckersDesk.cells.get(2).get(7));

        newCells.get(2).get(5).setChecker(null);
        newCells.get(3).get(4).setChecker(new CheckersDesk.Checker(CheckersDesk.Colors.BLACK, false));
        desk.moving(CheckersDesk.cells.get(2).get(5), CheckersDesk.cells.get(3).get(4));

        newCells.get(5).get(0).setChecker(null);
        newCells.get(4).get(1).setChecker(new CheckersDesk.Checker(CheckersDesk.Colors.WHITE, false));
        desk.moving(CheckersDesk.cells.get(5).get(0), CheckersDesk.cells.get(4).get(1));

        newCells.get(1).get(6).setChecker(null);
        newCells.get(2).get(5).setChecker(new CheckersDesk.Checker(CheckersDesk.Colors.BLACK, false));
        desk.moving(CheckersDesk.cells.get(1).get(6), CheckersDesk.cells.get(2).get(5));

        newCells.get(4).get(1).setChecker(null);
        newCells.get(3).get(0).setChecker(new CheckersDesk.Checker(CheckersDesk.Colors.WHITE, false));
        desk.moving(CheckersDesk.cells.get(4).get(1), CheckersDesk.cells.get(3).get(0));

        newCells.get(0).get(5).setChecker(null);
        newCells.get(1).get(6).setChecker(new CheckersDesk.Checker(CheckersDesk.Colors.BLACK, false));
        desk.moving(CheckersDesk.cells.get(0).get(5), CheckersDesk.cells.get(1).get(6));

        newCells.get(2).get(7).setChecker(null);
        newCells.get(1).get(6).setChecker(null);
        newCells.get(0).get(5).setChecker(new CheckersDesk.Checker(CheckersDesk.Colors.WHITE, true));
        desk.consumption(CheckersDesk.cells.get(2).get(7), CheckersDesk.cells.get(0).get(5));

        assertTrue(assertContentEquals(newCells, CheckersDesk.cells));
    }
}


