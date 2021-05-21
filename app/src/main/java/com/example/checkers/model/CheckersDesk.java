package com.example.checkers.model;

import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.checkers.model.Cell.cellExist;
import static com.example.checkers.model.Checker.Colors.BLACK;
import static com.example.checkers.model.Checker.Colors.WHITE;

public class CheckersDesk {
    public static Boolean whoseMove = false;
    public static int count;
    public static Cell selectedCell;
    public static List<Map<Cell, Cell>> pairs;
    public static List<View> viewPick = new ArrayList<>();
    public static Cell eatingCell = null;

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
    static final int rows = 8;
    static final int columns = 8;

    public static List<List<Cell>> cells;

    public void checkersDesk() {
        cells = new ArrayList<>(rows);

        for (int i = 0; i < rows; i++) {
            cells.add(i, new ArrayList<>(columns));
            for (int j = 0; j < columns; j++) {
                cells.get(i).add(j, new Cell(i, j, null));
            }
        }
    }

    public void initDesk() {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++) {
                if ((i + j) % 2 != 0) {
                    Checker checker = null;
                    if (i < 3) checker = new Checker(BLACK, false);
                    else if (i > rows - 4) checker = new Checker(WHITE, false);
                    if (checker != null) {
                        cells.get(i).get(j).setChecker(checker);
                        if (onCheckerActionListener != null)
                            onCheckerActionListener.onCheckerAdded(new Cell(i, j, checker));
                    }
                }
            }
    }

    //Проверяет возможность стать дамкой
    public boolean becomingQueen(Cell cell) {
        if (cell.getChecker() != null)
            if ((cell.getChecker().getColor() == BLACK && cell.getY() == 7) || (cell.getChecker().getColor() == WHITE && cell.getY() == 0)) {
                cells.get(cell.getY()).get(cell.getX()).getChecker().setCondition(true);
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
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell cell = cells.get(i).get(j);
                if (cell.getChecker() != null)
                    if ((cell.getChecker().getColor() == BLACK && whoseMove) ||
                            (!whoseMove && cells.get(i).get(j).getChecker().getColor() == WHITE))
                        if (!cell.getChecker().getCondition()) {
                            for (Map.Entry<Integer, Coordinates> entry : coordinates.entrySet()) {
                                if (cellExist(new Cell(cell.getY() + 2 * entry.getValue().getY(), cell.getX() + 2 * entry.getValue().getX(), null)))
                                    if (cells.get(cell.getY() + entry.getValue().getY()).get(cell.getX() + entry.getValue().getX()).getChecker() != null)
                                        if (cells.get(cell.getY() + entry.getValue().getY()).get(cell.getX() + entry.getValue().getX()).getChecker().getColor() != cell.getChecker().getColor())
                                            if (cells.get(cell.getY() + 2 * entry.getValue().getY()).get(cell.getX() + 2 * entry.getValue().getX()).getChecker() == null) {
                                                Map<Cell, Cell> map = new HashMap<>();
                                                map.put(cells.get(cell.getY() + 2 * entry.getValue().getY()).get(cell.getX() + 2 * entry.getValue().getX()), cell);
                                                requiredMoves.add(map);
                                            }
                            }
                        } else {
                            for (Map.Entry<Integer, Coordinates> entry : coordinates.entrySet()) {
                                while (cellExist(new Cell(cell.getY() + (coefficient + 1) * entry.getValue().getY(), cell.getX() + (coefficient + 1) * entry.getValue().getX(), null))) {
                                    if (cells.get(cell.getY() + coefficient * entry.getValue().getY()).get(cell.getX() + coefficient * entry.getValue().getX()).getChecker() != null) {
                                        if (cells.get(cell.getY() + coefficient * entry.getValue().getY()).get(cell.getX() + coefficient * entry.getValue().getX()).getChecker().getColor() != cell.getChecker().getColor()) {
                                            while (cellExist(new Cell(cell.getY() + (coefficient + 1) * entry.getValue().getY(), cell.getX() + (coefficient + 1) * entry.getValue().getX(), null))) {
                                                if (cells.get(cell.getY() + (coefficient + 1) * entry.getValue().getY()).get(cell.getX() + (coefficient + 1) * entry.getValue().getX()).getChecker() == null) {
                                                    Map<Cell, Cell> map = new HashMap<>();
                                                    map.put(cells.get(cell.getY() + (coefficient + 1) * entry.getValue().getY()).get(cell.getX() + (coefficient + 1) * entry.getValue().getX()), cell);
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
                if (cellExist(new Cell(cell.getY() + entry.getValue().getY(), cell.getX() + entry.getValue().getX(), null)))
                    if (cells.get(cell.getY() + entry.getValue().getY()).get(cell.getX() + entry.getValue().getX()).getChecker() == null)
                        if ((cell.getChecker().getColor() == BLACK && entry.getValue().getY() == 1 && whoseMove) ||
                                (cell.getChecker().getColor() == WHITE && entry.getValue().getY() == -1 && !whoseMove)) {
                            Map<Cell, Cell> map = new HashMap<>();
                            map.put(cells.get(cell.getY() + entry.getValue().getY()).get(cell.getX() + entry.getValue().getX()), null);
                            possibleWays.add(map);
                        }
            }
        } else {
            for (Map.Entry<Integer, Coordinates> entry : coordinates.entrySet()) {
                while (cellExist(new Cell(cell.getY() + coefficient * entry.getValue().getY(), cell.getX() + coefficient * entry.getValue().getX(), null))) {
                    if (cells.get(cell.getY() + coefficient * entry.getValue().getY()).get(cell.getX() + coefficient * entry.getValue().getX()).getChecker() == null) {
                        if ((cell.getChecker().getColor() == BLACK && whoseMove) ||
                                (cell.getChecker().getColor() == WHITE && !whoseMove)) {
                            Map<Cell, Cell> map = new HashMap<>();
                            map.put(cells.get(cell.getY() + coefficient * entry.getValue().getY()).get(cell.getX() + coefficient * entry.getValue().getX()), null);
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
                if (cellExist(new Cell(cell.getY() + 2 * entry.getValue().getY(), cell.getX() + 2 * entry.getValue().getX(), null)))
                    if (cells.get(cell.getY() + entry.getValue().getY()).get(cell.getX() + entry.getValue().getX()).getChecker() != null)
                        if (cells.get(cell.getY() + entry.getValue().getY()).get(cell.getX() + entry.getValue().getX()).getChecker().getColor() != cell.getChecker().getColor())
                            if (cells.get(cell.getY() + 2 * entry.getValue().getY()).get(cell.getX() + 2 * entry.getValue().getX()).getChecker() == null)
                                return true;
            }
        } else {
            for (Map.Entry<Integer, Coordinates> entry : coordinates.entrySet()) {
                while (cellExist(new Cell(cell.getY() + (coefficient + 1) * entry.getValue().getY(), cell.getX() + (coefficient + 1) * entry.getValue().getX(), null))) {
                    if (cells.get(cell.getY() + coefficient * entry.getValue().getY()).get(cell.getX() + coefficient * entry.getValue().getX()).getChecker() != null) {
                        if (cells.get(cell.getY() + coefficient * entry.getValue().getY()).get(cell.getX() + coefficient * entry.getValue().getX()).getChecker().getColor() != cell.getChecker().getColor()) {
                            if (cells.get(cell.getY() + (coefficient + 1) * entry.getValue().getY()).get(cell.getX() + (coefficient + 1) * entry.getValue().getX()).getChecker() == null) {
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
                        if (cell.getChecker().getColor() == WHITE) return false;
                }
        } else {
            for (int i = 0; i < rows; i++)
                for (int j = 0; j < columns; j++) {
                    Cell cell = cells.get(i).get(j);
                    if (cell.getChecker() != null)
                        if (cell.getChecker().getColor() == BLACK) return false;
                }
        }
        return true;
    }

    //Проверяет, существуют ли вообще возможные ходы для любой шашки
    private boolean checkForMove(boolean whoseMove) {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++) {
                Cell cell = cells.get(i).get(j);
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

    //Не позволяет выбрать шашки другого игрока в свой ход
    private boolean canPick(boolean whoseMove, Cell cell) {
        if (whoseMove) return cell.getChecker().getColor() == BLACK;
        else return cell.getChecker().getColor() == WHITE;
    }

    //Не позволяет перемещаться шашкой на ту клетку, которой нет в списке возможных вариантов
    private boolean canMove(List<Map<Cell, Cell>> pairs, Cell cell) {
        for (int i = 0; i <= pairs.size() - 1; i++) {
            for (Map.Entry<Cell, Cell> entry : pairs.get(i).entrySet())
                if (entry.getKey().equals(cell)) return true;
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
    public void moving(Cell selectedCell, Cell cell1) {
        cells.get(cell1.getY()).get(cell1.getX()).setChecker(selectedCell.getChecker());
        cells.get(selectedCell.getY()).get(selectedCell.getX()).setChecker(null);
        if (onCheckerActionListener != null)
            onCheckerActionListener.onCheckerMoved(selectedCell, cell1);
        if (becomingQueen(cells.get(cell1.getY()).get(cell1.getX()))) {
            if (onCheckerActionListener != null) {
                onCheckerActionListener.onCheckerRemoved(cells.get(cell1.getY()).get(cell1.getX()));
                onCheckerActionListener.onQueenAdded(cells.get(cell1.getY()).get(cell1.getX()));
            }
        }
        count = 0;
    }

    //Процесс поедания
    public void consumption(Cell selectedCell, Cell cell1) {
        Cell deleted = deletedChecker(selectedCell, cell1);
        moving(selectedCell, cell1);
        if (onCheckerActionListener != null)
            onCheckerActionListener.onCheckerRemoved(cells.get(deleted.getY()).get(deleted.getX()));
        cells.get(deleted.getY()).get(deleted.getX()).setChecker(null);
        //Если возможно съесть больше
        if (!canEatMore(cells.get(cell1.getY()).get(cell1.getX()))) {
            eatingCell = null;
            whoseMove = !whoseMove;
        } else eatingCell = cells.get(cell1.getY()).get(cell1.getX());
        count = 0;
    }

    //Находит обязательный ход для шашки
    private List<Map<Cell, Cell>> filterForRequiredMoves(List<Map<Cell, Cell>> pair, Cell cell) {
        List<Map<Cell, Cell>> newPair = new ArrayList<>();
        for (int i = 0; i < pair.size(); i++)
            for (Map.Entry<Cell, Cell> entry : pair.get(i).entrySet())
                if (eatingCell != null) {
                    if (cell.equals(eatingCell)) {
                        if (cell.equals(entry.getValue())) {
                            Map<Cell, Cell> map = new HashMap<>();
                            map.put(entry.getKey(), entry.getValue());
                            newPair.add(map);
                        }
                    }
                } else {
                    if (cell.equals(entry.getValue())) {
                        Map<Cell, Cell> map = new HashMap<>();
                        map.put(entry.getKey(), entry.getValue());
                        newPair.add(map);
                    }
                }
        return newPair;
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
                    for (Map.Entry<Cell, Cell> entry : pairs.get(0).entrySet())
                        if (entry.getValue() != null) {
                            if (eatingCell != null) {
                                selectedCell = eatingCell;
                                if (canEatThis(pairs, cell1))
                                    consumption(eatingCell, cell1);
                            } else {
                                if (canEatThis(pairs, cell1))
                                    consumption(selectedCell, cell1);
                            }
                            count = 0;

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
                    List<Map<Cell, Cell>> requiredMoves = requiredMoves(whoseMove);
                    List<Map<Cell, Cell>> pair = possibleWays(cell, whoseMove);
                    if (requiredMoves.size() != 0) {
                        pairs = filterForRequiredMoves(requiredMoves, cell);
                    } else {
                        pairs = pair;
                    }
                    viewPick = onCheckerActionListener.colorForMoves(pairs, view, cells, cell);
                    selectedCell = cell;
                    count = 1;
                }
        }
        return finishGame(whoseMove);
    }
}





