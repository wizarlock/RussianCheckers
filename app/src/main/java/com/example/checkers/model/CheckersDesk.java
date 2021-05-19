package com.example.checkers.model;

import android.util.Pair;
import android.view.View;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.checkers.model.Cell.cellExist;
import static com.example.checkers.model.CheckersDesk.Colors.BLACK;
import static com.example.checkers.model.CheckersDesk.Colors.WHITE;

public class CheckersDesk {
    public static Boolean whoseMove = false;
    public static int count;
    public static Cell selectedCell;
    public static List<Pair<Cell, Cell>> pairs;
    public static List<View> viewPick = new ArrayList<>();

    public static class Checker {
        private final Colors color;
        private boolean condition;

        public Checker(Colors color, boolean condition) {
            this.color = color;
            this.condition = condition;
        }

        public Colors getColor() {
            return color;
        }

        public void setCondition(Boolean condition) {
            this.condition = condition;
        }

        public boolean getCondition() {
            return condition;
        }
    }

    public enum Colors {
        WHITE, BLACK
    }

    public void setOnCheckerActionListener(OnCheckerActionListener onCheckerActionListener) {
        this.onCheckerActionListener = onCheckerActionListener;
    }

    public interface OnCheckerActionListener {
        void onCheckerAdded(Cell cell);

        void onQueenAdded(Cell cell);

        void onCheckerMoved(Cell from, Cell to);

        void onCheckerRemoved(Cell cell);

        List<View> colorForRequiredMoves(List<Pair<Cell, Cell>> pairs);

        List<View> colorForPossibleMoves(List<Pair<Cell, Cell>> pairs, View view, List<List<Cell>> cells, Cell cell);

        void boardClear(List<View> views);
    }

    private OnCheckerActionListener onCheckerActionListener;
    static final int rows = 8;
    static final int columns = 8;

    public static List<List<Cell>> cells;

    public CheckersDesk() {
        cells = new ArrayList<>(rows);

        for (int i = 0; i < rows; i++) {
            cells.add(i, new ArrayList<>(columns));
            for (int j = 0; j < columns; j++) {
                cells.get(i).add(j, new Cell(i, j, null));
            }
        }
    }

    public void initDesk() { ;
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++) {
                if ((i + j) % 2 != 0) {
                    Checker checker = null;
                    if (i < 3) checker = new Checker(Colors.BLACK, false);
                    else if (i > rows - 4) checker = new Checker(Colors.WHITE, false);
                    if (checker != null) {
                        cells.get(i).get(j).setChecker(checker);
                        onCheckerActionListener.onCheckerAdded(new Cell(i, j, checker));
                }
            }
        }
    }

    //Проверяет возможность стать дамкой
    public boolean becomingQueen(Cell cell) {
        if (cell.getChecker() != null)
            if ((cell.getChecker().getColor() == Colors.BLACK && cell.getY() == 7) || (cell.getChecker().getColor() == Colors.WHITE && cell.getY() == 0)) {
                cells.get(cell.getY()).get(cell.getX()).getChecker().setCondition(true);
                return true;
            }
        return false;
    }

    //Мапа с координатами
    private Map<Integer, Pair<Integer, Integer>> initCoordinates () {
        Map<Integer, Pair<Integer, Integer>> coordinates = new HashMap<>();
        coordinates.put(0, new Pair<>(-1, -1));
        coordinates.put(1, new Pair<>(-1, 1));
        coordinates.put(2, new Pair<>(1, -1));
        coordinates.put(3, new Pair<>(1, 1));
        return coordinates;
    }

    //Ищет возможности съесть для игрока
    public List<Pair<Cell, Cell>> requiredMoves(boolean whoseMove) {
        Map<Integer, Pair<Integer, Integer>> coordinates = initCoordinates();
        int coefficient = 1;
        List<Pair<Cell, Cell>> requiredMoves = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell cell = cells.get(i).get(j);
                if (cell.getChecker() != null)
                    if ((cell.getChecker().getColor() == Colors.BLACK && whoseMove) ||
                            (!whoseMove && cells.get(i).get(j).getChecker().getColor() == Colors.WHITE))
                        if (!cell.getChecker().getCondition()) {
                            for (Map.Entry<Integer, Pair<Integer, Integer>> entry : coordinates.entrySet()) {
                                if (cellExist(new Cell(cell.getY() + 2 * entry.getValue().first, cell.getX() + 2 * entry.getValue().second, null)))
                                    if (cells.get(cell.getY() + entry.getValue().first).get(cell.getX() + entry.getValue().second).getChecker() != null)
                                        if (cells.get(cell.getY() + entry.getValue().first).get(cell.getX() + entry.getValue().second).getChecker().getColor() != cell.getChecker().getColor())
                                            if (cells.get(cell.getY() + 2 * entry.getValue().first).get(cell.getX() + 2 * entry.getValue().second).getChecker() == null)
                                                requiredMoves.add(new Pair<>(cells.get(cell.getY() + 2 * entry.getValue().first).get(cell.getX() + 2 * entry.getValue().second), cell));
                            }
                        } else {
                            for (Map.Entry<Integer, Pair<Integer, Integer>> entry : coordinates.entrySet()) {
                                while (cellExist(new Cell(cell.getY() + (coefficient + 1) * entry.getValue().first, cell.getX() + (coefficient + 1) * entry.getValue().second, null))) {
                                    if (cells.get(cell.getY() + coefficient * entry.getValue().first).get(cell.getX() + coefficient * entry.getValue().second).getChecker() != null) {
                                        if (cells.get(cell.getY() + coefficient * entry.getValue().first).get(cell.getX() + coefficient * entry.getValue().second).getChecker().getColor() != cell.getChecker().getColor()) {
                                            while (cellExist(new Cell(cell.getY() + (coefficient + 1) * entry.getValue().first, cell.getX() + (coefficient + 1) * entry.getValue().second, null))) {
                                                if (cells.get(cell.getY() + (coefficient + 1) * entry.getValue().first).get(cell.getX() + (coefficient + 1) * entry.getValue().second).getChecker() == null) {
                                                    requiredMoves.add(new Pair<>(cells.get(cell.getY() + (coefficient + 1) * entry.getValue().first).get(cell.getX() + (coefficient + 1) * entry.getValue().second), cell));
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
    public List<Pair<Cell, Cell>> possibleWays(Cell cell, boolean whoseMove) {
        List<Pair<Cell, Cell>> possibleWays = new ArrayList<>();
        Map<Integer, Pair<Integer, Integer>> coordinates = initCoordinates();
        int coefficient = 1;
        if (!cell.getChecker().getCondition()) {
            for (Map.Entry<Integer, Pair<Integer, Integer>> entry : coordinates.entrySet()) {
                if (cellExist(new Cell(cell.getY() + entry.getValue().first, cell.getX() + entry.getValue().second, null)))
                    if (cells.get(cell.getY() + entry.getValue().first).get(cell.getX() + entry.getValue().second).getChecker() == null)
                        if ((cell.getChecker().getColor() == Colors.BLACK && entry.getValue().first == 1 && whoseMove) ||
                                (cell.getChecker().getColor() == Colors.WHITE && entry.getValue().first == -1 && !whoseMove))
                            possibleWays.add(new Pair<>(cells.get(cell.getY() + entry.getValue().first).get(cell.getX() + entry.getValue().second), null));
            }
        } else {
            for (Map.Entry<Integer, Pair<Integer, Integer>> entry : coordinates.entrySet()) {
                while (cellExist(new Cell(cell.getY() + coefficient * entry.getValue().first, cell.getX() + coefficient * entry.getValue().second, null))) {
                    if (cells.get(cell.getY() + coefficient * entry.getValue().first).get(cell.getX() + coefficient * entry.getValue().second).getChecker() == null) {
                        if ((cell.getChecker().getColor() == Colors.BLACK && whoseMove) ||
                                (cell.getChecker().getColor() == Colors.WHITE && !whoseMove))
                            possibleWays.add(new Pair<>(cells.get(cell.getY() + coefficient * entry.getValue().first).get(cell.getX() + coefficient * entry.getValue().second), null));
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
        Map<Integer, Pair<Integer, Integer>> coordinates = initCoordinates();
        int coefficient = 1;
        if (!cell.getChecker().getCondition()) {
            for (Map.Entry<Integer, Pair<Integer, Integer>> entry : coordinates.entrySet()) {
                if (cellExist(new Cell(cell.getY() + 2 * entry.getValue().first, cell.getX() + 2 * entry.getValue().second, null)))
                    if (cells.get(cell.getY() + entry.getValue().first).get(cell.getX() + entry.getValue().second).getChecker() != null)
                        if (cells.get(cell.getY() + entry.getValue().first).get(cell.getX() + entry.getValue().second).getChecker().getColor() != cell.getChecker().getColor())
                            if (cells.get(cell.getY() + 2 * entry.getValue().first).get(cell.getX() + 2 * entry.getValue().second).getChecker() == null)
                                return true;
            }
        } else {
            for (Map.Entry<Integer, Pair<Integer, Integer>> entry : coordinates.entrySet()) {
                while (cellExist(new Cell(cell.getY() + (coefficient + 1) * entry.getValue().first, cell.getX() + (coefficient + 1) * entry.getValue().second, null))) {
                    if (cells.get(cell.getY() + coefficient * entry.getValue().first).get(cell.getX() + coefficient * entry.getValue().second).getChecker() != null) {
                        if (cells.get(cell.getY() + coefficient * entry.getValue().first).get(cell.getX() + coefficient * entry.getValue().second).getChecker().getColor() != cell.getChecker().getColor()) {
                            if (cells.get(cell.getY() + (coefficient + 1) * entry.getValue().first).get(cell.getX() + (coefficient + 1) * entry.getValue().second).getChecker() == null) {
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
        if (!whoseMove) {
            for (int i = 0; i < rows; i++)
                for (int j = 0; j < columns; j++) {
                    Cell cell = cells.get(i).get(j);
                    if (cell.getChecker() != null)
                        if (cell.getChecker().getColor() == Colors.WHITE) return false;
                }
        } else {
            for (int i = 0; i < rows; i++)
                for (int j = 0; j < columns; j++) {
                    Cell cell = cells.get(i).get(j);
                    if (cell.getChecker() != null)
                        if (cell.getChecker().getColor() == Colors.BLACK) return false;
                }
        }
        return true;
    }

    //Проверяет, существуют ли вообще возможные ходы для любой шашки
    public boolean checkForMove(boolean whoseMove) {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++) {
                Cell cell = cells.get(i).get(j);
                if (cell.getChecker() != null)
                    if (possibleWays(cell, whoseMove).size() != 0) return false;
            }
        return true;
    }

    //Находит клетку, шашка на которой будет удалена
    public Cell deletedChecker(Cell pick, Cell variant) {
        int coefficient = 1;
        int posX = -1;
        int posY = -1;
        if (pick.getY() > variant.getY()) {
            if (pick.getX() > variant.getX()) {
                for (int i = pick.getY() - 1; i > variant.getY(); i--) {
                    if (cells.get(i).get(pick.getX() - coefficient).getChecker() != null) {
                        posX = pick.getX() - coefficient;
                        posY = i;
                        break;
                    }
                    coefficient++;
                }
            } else for (int i = pick.getY() - 1; i > variant.getY(); i--) {
                if (cells.get(i).get(pick.getX() + coefficient).getChecker() != null) {
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
                    if (cells.get(i).get(pick.getX() - coefficient).getChecker() != null) {
                        posX = pick.getX() - coefficient;
                        posY = i;
                        break;
                    }
                    coefficient++;
                }
            } else for (int i = pick.getY() + 1; i < variant.getY(); i++) {
                if (cells.get(i).get(pick.getX() + coefficient).getChecker() != null) {
                    posX = pick.getX() + coefficient;
                    posY = i;
                    break;
                }
                coefficient++;
            }
        }
        return (cells.get(posY).get(posX));
    }

    //Не позволяет любой шашке есть, есть может только та, которая есть в списке
    public boolean canPickEater(List<Pair<Cell, Cell>> pairs, Cell selectedCell) {
        for (int i = 0; i <= pairs.size() - 1; i++) {
            if (selectedCell.equals(pairs.get(i).second)) return true;
        }
        return false;
    }

    //Не позволяет выбрать шашки другого игрока в свой ход
    public boolean canPick(boolean whoseMove, Cell cell) {
        if (whoseMove) return cell.getChecker().getColor() == BLACK;
        else return cell.getChecker().getColor() == WHITE;
    }

    //Не позволяет перемещаться шашкой на ту клетку, которой нет в списке возможных вариантов
    public boolean canMove(List<Pair<Cell, Cell>> pairs, Cell cell) {
            for (int i = 0; i <= pairs.size() - 1; i++) {
                if (pairs.get(i).first.equals(cell)) return true;
            }
        return false;
    }

    //Не позволяет перемещаться шашкой на ту клетку, которой нет в списке возможных вариантов (для съедения)
    public boolean canEatThis(List<Pair<Cell, Cell>> pairs, Cell cell) {
        for (int i = 0; i <= pairs.size() - 1; i++) {
            if (pairs.get(i).second.equals(selectedCell) && pairs.get(i).first.equals(cell))
                return true;
        }
        return false;
    }

    //Процесс перемещения
    private void moving (Cell selectedCell, Cell cell1) {
        cells.get(cell1.getY()).get(cell1.getX()).setChecker(selectedCell.getChecker());
        cells.get(selectedCell.getY()).get(selectedCell.getX()).setChecker(null);
        onCheckerActionListener.onCheckerMoved(selectedCell, cell1);
        if (becomingQueen(cells.get(cell1.getY()).get(cell1.getX()))) {
            onCheckerActionListener.onCheckerRemoved(cells.get(cell1.getY()).get(cell1.getX()));
            onCheckerActionListener.onQueenAdded(cells.get(cell1.getY()).get(cell1.getX()));
        }
        count = 0;
    }

    public boolean startGame(View view) {
        if (count == 1) {
            Cell cell1 = new Cell(Integer.parseInt(((View) view.getParent()).getTag().toString()), Integer.parseInt(view.getTag().toString()),
                    cells.get(Integer.parseInt(((View) view.getParent()).getTag().toString())).get((Integer.parseInt(view.getTag().toString()))).getChecker());
            //позволяет перевыбрать шашку
            if (cell1.getChecker() != null) {
                if (cell1.getChecker().getColor() == selectedCell.getChecker().getColor())
                    count = 0;
            } else {
                if (canMove(pairs, cell1)) {
                    //Если возможно съесть
                    if (pairs.get(0).second != null) {
                        if (canEatThis(pairs, cell1)) {
                            Cell deleted = deletedChecker(selectedCell, cell1);
                            moving(selectedCell, cell1);
                            onCheckerActionListener.onCheckerRemoved(cells.get(deleted.getY()).get(deleted.getX()));
                            cells.get(deleted.getY()).get(deleted.getX()).setChecker(null);
                            //Если возможно съесть больше
                            if (!canEatMore(cells.get(cell1.getY()).get(cell1.getX())))
                                whoseMove = !whoseMove;
                        }
                        // Простой ход
                    } else {
                        moving(selectedCell, cell1);
                        whoseMove = !whoseMove;
                    }
                }
            }
            onCheckerActionListener.boardClear(viewPick);
            viewPick.clear();
        }
        if (count == 0) {
            Cell cell = new Cell(Integer.parseInt(((View) view.getParent()).getTag().toString()), Integer.parseInt(view.getTag().toString()),
                    cells.get(Integer.parseInt(((View) view.getParent()).getTag().toString())).get((Integer.parseInt(view.getTag().toString()))).getChecker());
            //не позволяет выбрать шашку оппонента
            if (cell.getChecker() != null)
                if (canPick(whoseMove, cell)) {
                    List<Pair<Cell, Cell>> requiredMoves = requiredMoves(whoseMove);
                    List<Pair<Cell, Cell>> pair = possibleWays(cell, whoseMove);
                    if (requiredMoves.size() != 0) {
                        pairs = requiredMoves;
                        viewPick = onCheckerActionListener.colorForRequiredMoves(pairs);
                        selectedCell = cell;
                        if (canPickEater(requiredMoves, selectedCell)) count = 1;
                    } else {
                        pairs = pair;
                        viewPick = onCheckerActionListener.colorForPossibleMoves(pairs, view, cells, cell);
                        selectedCell = cell;
                        count = 1;
                    }
                }
        }
        return finishGame(whoseMove);
    }
}





