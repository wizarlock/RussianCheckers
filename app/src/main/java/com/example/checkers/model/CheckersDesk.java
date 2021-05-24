package com.example.checkers.model;

import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.example.checkers.model.Checker.Colors.BLACK;
import static com.example.checkers.model.Checker.Colors.WHITE;

public class CheckersDesk {
    private Boolean blacksMoves = false;
    private int numberOfClicks;
    private Cell selectedCell;
    private List<Map<Cell, Cell>> possiblePairsForMove;
    private List<View> allViews = new ArrayList<>();
    private Cell eatingCell = null;

    public static class Coordinates {
        private final int y;
        private final int x;

        public Coordinates(int y, int x) {
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

    public void setOnCheckerActionListener(OnCheckerActionListener onCheckerActionListener) {
        this.onCheckerActionListener = onCheckerActionListener;
    }

    public interface OnCheckerActionListener {
        void onCheckerAdded(Cell cell);

        void onQueenAdded(Cell cell);

        void onCheckerMoved(Cell from, Cell to);

        void onCheckerRemoved(Cell cell);

        List<View> colorForMoves(List<Map<Cell, Cell>> pairs, View view, List<List<Cell>> cells, Cell cell);

        void boardClear(List<View> views);
    }

    private OnCheckerActionListener onCheckerActionListener;
    static final int ROWS = 8;
    static final int COLUMNS = 8;

    public static List<List<Cell>> arrayOfAllCells;

    public void checkersDesk() {
        arrayOfAllCells = new ArrayList<>(ROWS);

        for (int i = 0; i < ROWS; i++) {
            arrayOfAllCells.add(i, new ArrayList<>(COLUMNS));
            for (int j = 0; j < COLUMNS; j++) {
                arrayOfAllCells.get(i).add(j, new Cell(i, j, null));
            }
        }
    }

    public void initDesk() {
        for (int i = 0; i < ROWS; i++)
            for (int j = 0; j < COLUMNS; j++) {
                if ((i + j) % 2 != 0) {
                    Checker checker = null;
                    if (i < 3) checker = new Checker(BLACK, false);
                    else if (i > ROWS - 4) checker = new Checker(WHITE, false);
                    if (checker != null) {
                        Objects.requireNonNull(getCell(i, j)).setChecker(checker);
                        if (onCheckerActionListener != null)
                            onCheckerActionListener.onCheckerAdded(new Cell(i, j, checker));
                    }
                }
            }
    }

    //получение клетки по Y и X
    private Cell getCell(int posY, int posX) {
        Cell cell = new Cell(posY, posX, null);
        if (cell.cellExist(cell.getY(), cell.getX())) return arrayOfAllCells.get(posY).get(posX);
        else return null;
    }

    //Проверяет, подходит ли по цвету и по ходу
    private boolean correctColorEat(boolean whoseMove, Cell cell) {
        return (cell.getChecker().getColor() == BLACK && whoseMove) || (!whoseMove && cell.getChecker().getColor() == WHITE);
    }

    //Проверяет, можно ли так ходить данному цвету
    private boolean correctColorMove(boolean whoseMove, Cell cell, int value) {
        return ((cell.getChecker().getColor() == BLACK && value == 1 && whoseMove) ||
                (cell.getChecker().getColor() == WHITE && value == -1 && !whoseMove));
    }

    //Проверяет возможность стать дамкой
    public boolean becomingQueen(Cell cell) {
        if (cell.getChecker() != null)
            if ((cell.getChecker().getColor() == BLACK && cell.getY() == 7) || (cell.getChecker().getColor() == WHITE && cell.getY() == 0)) {
                cell.getChecker().setCondition(true);
                return true;
            }
        return false;
    }

    //Мапа с координатами
    private Map<Integer, Coordinates> initCoordinates() {
        Map<Integer, Coordinates> coordinates = new HashMap<>();
        coordinates.put(0, new Coordinates(-1, -1));
        coordinates.put(1, new Coordinates(-1, 1));
        coordinates.put(2, new Coordinates(1, -1));
        coordinates.put(3, new Coordinates(1, 1));
        return coordinates;
    }

    //Ищет возможности съесть для игрока
    public List<Map<Cell, Cell>> requiredMoves(boolean whoseMove) {
        Map<Integer, Coordinates> coordinates = initCoordinates();
        int coefficient = 1;
        List<Map<Cell, Cell>> requiredMoves = new ArrayList<>();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                Cell cell = Objects.requireNonNull(getCell(i, j));
                if (cell.getChecker() != null)
                    if (correctColorEat(whoseMove, cell))
                        if (!cell.getChecker().getCondition()) {
                            for (Map.Entry<Integer, Coordinates> entry : coordinates.entrySet()) {
                                Cell potentialCell = getCell(cell.getY() + 2 * entry.getValue().getY(), cell.getX() + 2 * entry.getValue().getX());
                                Cell cellBetweenPotentialCellAndCell = getCell(cell.getY() + entry.getValue().getY(), cell.getX() + entry.getValue().getX());
                                if (potentialCell != null)
                                    if (cellBetweenPotentialCellAndCell != null)
                                    if (cellBetweenPotentialCellAndCell.getChecker() != null)
                                        if (cellBetweenPotentialCellAndCell.getChecker().getColor() != cell.getChecker().getColor())
                                            if (potentialCell.getChecker() == null) {
                                                Map<Cell, Cell> map = new HashMap<>();
                                                map.put(potentialCell, cell);
                                                requiredMoves.add(map);
                                            }
                            }
                        } else {
                            for (Map.Entry<Integer, Coordinates> entry : coordinates.entrySet()) {
                                while (getCell(cell.getY() + (coefficient + 1) * entry.getValue().getY(), cell.getX() + (coefficient + 1) * entry.getValue().getX()) != null) {
                                    Cell potentialCell = getCell(cell.getY() + coefficient * entry.getValue().getY(), cell.getX() + coefficient * entry.getValue().getX());
                                    if (potentialCell != null)
                                    if (potentialCell.getChecker() != null) {
                                        if (potentialCell.getChecker().getColor() != cell.getChecker().getColor()) {
                                            while (getCell(cell.getY() + (coefficient + 1) * entry.getValue().getY(), cell.getX() + (coefficient + 1) * entry.getValue().getX()) != null) {
                                                potentialCell = getCell(cell.getY() + (coefficient + 1) * entry.getValue().getY(), cell.getX() + (coefficient + 1) * entry.getValue().getX());
                                                if (potentialCell != null)
                                                if (potentialCell.getChecker() == null) {
                                                    Map<Cell, Cell> map = new HashMap<>();
                                                    map.put(potentialCell, cell);
                                                    requiredMoves.add(map);
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

    //Ищет возможность для просто перемещения
    public List<Map<Cell, Cell>> possibleWays(Cell cell, boolean whoseMove) {
        List<Map<Cell, Cell>> possibleWays = new ArrayList<>();
        Map<Integer, Coordinates> coordinates = initCoordinates();
        int coefficient = 1;
        if (!cell.getChecker().getCondition()) {
            for (Map.Entry<Integer, Coordinates> entry : coordinates.entrySet()) {
                Cell potentialCell = getCell(cell.getY() + entry.getValue().getY(), cell.getX() + entry.getValue().getX());
                if (potentialCell != null)
                    if (potentialCell.getChecker() == null)
                        if (correctColorMove(whoseMove, cell, entry.getValue().getY())) {
                            Map<Cell, Cell> map = new HashMap<>();
                            map.put(potentialCell, null);
                            possibleWays.add(map);
                        }
            }
        } else {
            for (Map.Entry<Integer, Coordinates> entry : coordinates.entrySet()) {
                while (getCell(cell.getY() + coefficient * entry.getValue().getY(), cell.getX() + coefficient * entry.getValue().getX()) != null) {
                    Cell potential = getCell(cell.getY() + coefficient * entry.getValue().getY(), cell.getX() + coefficient * entry.getValue().getX());
                    if (potential != null)
                    if (potential.getChecker() == null) {
                        if (correctColorEat(whoseMove, cell)) {
                            Map<Cell, Cell> map = new HashMap<>();
                            map.put(potential, null);
                            possibleWays.add(map);
                        }
                        coefficient++;
                    } else break;
                }
                coefficient = 1;
            }
        }
        return possibleWays;
    }

    //Проверяет, может ли есть еще этой шашке
    public boolean canEatMore(Cell cell) {
        Map<Integer, Coordinates> coordinates = initCoordinates();
        int coefficient = 1;
        if (!cell.getChecker().getCondition()) {
            for (Map.Entry<Integer, Coordinates> entry : coordinates.entrySet()) {
                Cell potentialCell = getCell(cell.getY() + 2 * entry.getValue().getY(), cell.getX() + 2 * entry.getValue().getX());
                Cell cellBetweenPotentialCellAndCell = getCell(cell.getY() + entry.getValue().getY(), cell.getX() + entry.getValue().getX());
                if (potentialCell != null)
                    if (cellBetweenPotentialCellAndCell != null)
                    if (cellBetweenPotentialCellAndCell.getChecker() != null)
                        if (cellBetweenPotentialCellAndCell.getChecker().getColor() != cell.getChecker().getColor())
                            if (potentialCell.getChecker() == null)
                                return true;
            }
        } else {
            for (Map.Entry<Integer, Coordinates> entry : coordinates.entrySet()) {
                while (getCell(cell.getY() + (coefficient + 1) * entry.getValue().getY(), cell.getX() + (coefficient + 1) * entry.getValue().getX()) != null) {
                    Cell cellBetweenPotentialCellAndCell = getCell(cell.getY() + coefficient * entry.getValue().getY(), cell.getX() + coefficient * entry.getValue().getX());
                    Cell potentialCell = getCell(cell.getY() + (coefficient + 1) * entry.getValue().getY(), cell.getX() + (coefficient + 1) * entry.getValue().getX());
                    if (potentialCell != null)
                        if (cellBetweenPotentialCellAndCell != null)
                    if (cellBetweenPotentialCellAndCell.getChecker() != null) {
                        if (cellBetweenPotentialCellAndCell.getChecker().getColor() != cell.getChecker().getColor()) {
                            if (potentialCell.getChecker() == null) {
                                return true;
                            } else break;
                        }
                        break;
                    } else coefficient++;
                }
                coefficient = 1;
            }
        }
        return false;
    }

    //Проверяет возможность закончить игру
    public boolean finishGame(boolean whoseMove) {
        if (requiredMoves(whoseMove).size() == 0 && checkForMove(whoseMove)) return true;
        for (int i = 0; i < ROWS; i++)
            for (int j = 0; j < COLUMNS; j++) {
                Cell cell = Objects.requireNonNull(getCell(i, j));
                if (cell.getChecker() != null) {
                    if (cell.getChecker().getColor() == WHITE) return false;
                    if (cell.getChecker().getColor() == BLACK) return false;
                }
            }
        return true;
    }

    //Проверяет, существуют ли вообще возможные ходы для любой шашки
    private boolean checkForMove(boolean whoseMove) {
        for (int i = 0; i < ROWS; i++)
            for (int j = 0; j < COLUMNS; j++) {
                Cell cell = Objects.requireNonNull(getCell(i, j));
                if (cell.getChecker() != null)
                    if (possibleWays(cell, whoseMove).size() != 0) return false;
            }
        return true;
    }

    //Находит клетку, шашка на которой будет удалена
    private Cell deletedChecker(Cell pick, Cell variant) {
        int coefficient = 1;
        int posX = -1;
        int posY = -1;
        if (pick.getY() > variant.getY()) {
            if (pick.getX() > variant.getX()) {
                for (int i = pick.getY() - 1; i > variant.getY(); i--) {
                    if (Objects.requireNonNull(getCell(i, pick.getX() - coefficient)).getChecker() != null) {
                        posX = pick.getX() - coefficient;
                        posY = i;
                        break;
                    }
                    coefficient++;
                }
            } else for (int i = pick.getY() - 1; i > variant.getY(); i--) {
                if (Objects.requireNonNull(getCell(i, pick.getX() + coefficient)).getChecker() != null) {
                    posX = pick.getX() + coefficient;
                    posY = i;
                    break;
                }
                coefficient++;
            }
        }
        if (pick.getY() < variant.getY()) {
            if (pick.getX() > variant.getX()) {
                for (int i = pick.getY() + 1; i < variant.getY(); i++) {
                    if (Objects.requireNonNull(getCell(i, pick.getX() - coefficient)).getChecker() != null) {
                        posX = pick.getX() - coefficient;
                        posY = i;
                        break;
                    }
                    coefficient++;
                }
            } else for (int i = pick.getY() + 1; i < variant.getY(); i++) {
                if (Objects.requireNonNull(getCell(i, pick.getX() + coefficient)).getChecker() != null) {
                    posX = pick.getX() + coefficient;
                    posY = i;
                    break;
                }
                coefficient++;
            }
        }
        return arrayOfAllCells.get(posY).get(posX);
    }

    //Не позволяет выбрать шашки другого игрока в свой ход
    private boolean canPick(boolean whoseMove, Cell cell) {
        if (whoseMove) return cell.getChecker().getColor() == BLACK;
        else return cell.getChecker().getColor() == WHITE;
    }

    //Не позволяет перемещаться шашкой на ту клетку, которой нет в списке возможных вариантов
    private boolean canMove(List<Map<Cell, Cell>> pairs, Cell cell) {
        for (int i = 0; i <= pairs.size() - 1; i++) {
            if (pairs.get(i).containsKey(cell)) return true;
        }
        return false;
    }

    //Не позволяет перемещаться шашкой на ту клетку, которой нет в списке возможных вариантов (для съедения)
    private boolean canEatThis(List<Map<Cell, Cell>> pairs, Cell cell) {
        for (int i = 0; i <= pairs.size() - 1; i++) {
            for (Map.Entry<Cell, Cell> entry : pairs.get(i).entrySet())
                if (entry.getValue().equals(selectedCell) && entry.getKey().equals(cell))
                    return true;
        }
        return false;
    }

    //Процесс перемещения
    public void moving(Cell selectedCell, Cell variant) {
        variant.setChecker(selectedCell.getChecker());
        selectedCell.setChecker(null);
        if (onCheckerActionListener != null)
            onCheckerActionListener.onCheckerMoved(selectedCell, variant);
        if (becomingQueen(variant)) {
            if (onCheckerActionListener != null) {
                onCheckerActionListener.onCheckerRemoved(variant);
                onCheckerActionListener.onQueenAdded(variant);
            }
        }
        numberOfClicks = 0;
    }

    //Процесс поедания
    public void consumption(Cell selectedCell, Cell variant) {
        Cell deletedCell = deletedChecker(selectedCell, variant);
        moving(selectedCell, variant);
        if (onCheckerActionListener != null)
            onCheckerActionListener.onCheckerRemoved(deletedCell);
        deletedCell.setChecker(null);
        //Если возможно съесть больше
        if (!canEatMore(variant)) {
            eatingCell = null;
            blacksMoves = !blacksMoves;
        } else eatingCell = variant;
        numberOfClicks = 0;
    }

    //Находит обязательный ход для шашки
    private List<Map<Cell, Cell>> filterForRequiredMoves(List<Map<Cell, Cell>> pair, Cell cell) {
        List<Map<Cell, Cell>> moves = new ArrayList<>();
        for (int i = 0; i < pair.size(); i++)
            for (Map.Entry<Cell, Cell> entry : pair.get(i).entrySet())
                if (eatingCell != null) {
                    if (cell.equals(eatingCell))
                        if (cell.equals(entry.getValue())) {
                            Map<Cell, Cell> map = new HashMap<>();
                            map.put(entry.getKey(), entry.getValue());
                            moves.add(map);
                        }
                } else {
                    if (cell.equals(entry.getValue())) {
                        Map<Cell, Cell> map = new HashMap<>();
                        map.put(entry.getKey(), entry.getValue());
                        moves.add(map);
                    }
                }
        return moves;
    }

    public String startGame(View view) {
        if (numberOfClicks == 1) {
            Cell cellForSecondClick = Objects.requireNonNull(getCell(Integer.parseInt(((View) view.getParent()).getTag().toString()), Integer.parseInt(view.getTag().toString())));
            //позволяет перевыбрать шашку
            if (cellForSecondClick.getChecker() != null) {
                if (cellForSecondClick.getChecker().getColor() == selectedCell.getChecker().getColor())
                    numberOfClicks = 0;
            } else {
                if (canMove(possiblePairsForMove, cellForSecondClick)) {
                    //Если возможно съесть
                    if (!possiblePairsForMove.get(0).containsValue(null)) {
                        if (eatingCell != null) {
                            selectedCell = eatingCell;
                            if (canEatThis(possiblePairsForMove, cellForSecondClick))
                                consumption(eatingCell, cellForSecondClick);
                        } else {
                            if (canEatThis(possiblePairsForMove, cellForSecondClick))
                                consumption(selectedCell, cellForSecondClick);
                        }
                        numberOfClicks = 0;

                        // Простой ход
                    } else {
                        moving(selectedCell, cellForSecondClick);
                        blacksMoves = !blacksMoves;
                    }
                }
            }
            onCheckerActionListener.boardClear(allViews);
            allViews.clear();
        }
        if (numberOfClicks == 0) {
            Cell cellForFirstClick = Objects.requireNonNull(getCell(Integer.parseInt(((View) view.getParent()).getTag().toString()), Integer.parseInt(view.getTag().toString())));
            //не позволяет выбрать шашку оппонента
            if (cellForFirstClick.getChecker() != null)
                if (canPick(blacksMoves, cellForFirstClick)) {
                    List<Map<Cell, Cell>> requiredMoves = requiredMoves(blacksMoves);
                    List<Map<Cell, Cell>> pair = possibleWays(cellForFirstClick, blacksMoves);
                    if (requiredMoves.size() != 0) {
                        possiblePairsForMove = filterForRequiredMoves(requiredMoves, cellForFirstClick);
                    } else {
                        possiblePairsForMove = pair;
                    }
                    allViews = onCheckerActionListener.colorForMoves(possiblePairsForMove, view, arrayOfAllCells, cellForFirstClick);
                    selectedCell = cellForFirstClick;
                    numberOfClicks = 1;
                }
        }
        String win = "";
        if (finishGame(blacksMoves)) {
            if (blacksMoves) win = "White";
            else win = "Black";
            blacksMoves = false;
        }
        return win;
    }
}





