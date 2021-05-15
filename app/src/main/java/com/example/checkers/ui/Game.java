package com.example.checkers.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.checkers.R;
import com.example.checkers.model.Cell;
import com.example.checkers.model.CheckersDesk;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.checkers.model.CheckersDesk.Colors.BLACK;
import static com.example.checkers.model.CheckersDesk.Colors.WHITE;
import static com.example.checkers.model.CheckersDesk.becomingQueen;
import static com.example.checkers.model.CheckersDesk.canEatMore;
import static com.example.checkers.model.CheckersDesk.cells;
import static com.example.checkers.model.CheckersDesk.checkForMove;
import static com.example.checkers.model.CheckersDesk.finishGame;


public class Game extends AppCompatActivity implements CheckersDesk.OnCheckerActionListener {
    public Boolean whoseMove = false;
    public LinearLayout table;
    public int count;
    public Cell selectedCell;
    public List<Pair<Cell, Cell>> pairs;
    public List<View> viewPick = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        table = findViewById(R.id.desk);

        CheckersDesk desk = new CheckersDesk();
        desk.setOnCheckerActionListener(this);
        desk.initDesk();

    }

    private View findWithTag(ViewGroup parent, Object tag) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            if (parent.getChildAt(i).getTag().equals(tag))
                return parent.getChildAt(i);
        }
        return null;
    }

    @Override
    public void onCheckerAdded(Cell cell) {
        LinearLayout row = (LinearLayout) findWithTag(table, String.valueOf(cell.getY()));
        ImageView checkerImage = new ImageView(this);
        checkerImage.setImageResource(cell.getChecker().getColor() == CheckersDesk.Colors.WHITE ? R.drawable.white : R.drawable.black);
        LinearLayout layout = Objects.requireNonNull(row).findViewWithTag(String.valueOf(cell.getX()));
        layout.addView(checkerImage);
    }

    @Override
    public void onQueenAdded(Cell cell) {
        LinearLayout row = (LinearLayout) findWithTag(table, String.valueOf(cell.getY()));
        ImageView checkerImage = new ImageView(this);
        checkerImage.setImageResource(cell.getChecker().getColor() == CheckersDesk.Colors.WHITE ? R.drawable.queen1 : R.drawable.queen0);
        LinearLayout layout = Objects.requireNonNull(row).findViewWithTag(String.valueOf(cell.getX()));
        layout.addView(checkerImage);
    }

    @Override
    public void onCheckerMoved(Cell from, Cell to) {
        View checkerImage = getCheckerLayout(from).getChildAt(0);
        getCheckerLayout(from).removeView(checkerImage);
        getCheckerLayout(to).addView(checkerImage);
    }

    public void onCheckerRemoved(Cell cell) {
        View checkerImage = getCheckerLayout(cell).getChildAt(0);
        getCheckerLayout(cell).removeView(checkerImage);
    }

    public LinearLayout getCheckerLayout(Cell cell) {
        LinearLayout row = (LinearLayout) findWithTag(table, String.valueOf(cell.getY()));
        return Objects.requireNonNull(row).findViewWithTag(String.valueOf(cell.getX()));
    }

    //очищает доску от подсвеченных клеток
    public void boardClear(List<View> views) {
        for (int i = 0; i <= views.size() - 1; i++) {
            views.get(i).setBackgroundColor(ContextCompat.getColor(this, R.color.brown));
        }
    }

    //не позволяет перемещаться на клетку, которой нет в списке вариантов
    public boolean canMove(List<Pair<Cell, Cell>> pair, Cell cell) {
        for (int i = 0; i <= pair.size() - 1; i++) {
            if (pair.get(i).first.equals(cell)) return true;
        }
        return false;
    }

    //не позволяет выбрать шашки другого игрока в свой ход
    public boolean canPick(boolean whoseMove, Cell cell) {
        if (whoseMove) return cell.getChecker().getColor() == BLACK;
        else return cell.getChecker().getColor() == WHITE;
    }

    public boolean canEatThis(List<Pair<Cell, Cell>> requiredMoves, Cell cell) {
        for (int i = 0; i <= requiredMoves.size() - 1; i++) {
            if (requiredMoves.get(i).second.equals(selectedCell) && requiredMoves.get(i).first.equals(cell))
                return true;
        }
        return false;
    }

    //не позволяет любой шашке есть, есть может только та, которая есть в списке
    public boolean canPickEater(List<Pair<Cell, Cell>> requiredMoves, Cell selectedCell) {
        for (int i = 0; i <= requiredMoves.size() - 1; i++) {
            if (selectedCell.equals(requiredMoves.get(i).second)) return true;
        }
        return false;
    }

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

    public void toFinish(boolean whoseMove) {
        Intent intent;
        if (whoseMove) {
            intent = new Intent(Game.this, EndGameForWhite.class);
        } else {
            intent = new Intent(Game.this, EndGameForBlack.class);
        }
        startActivity(intent);
        finish();
    }

    public void onClick(View view) {
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
                            cells.get(cell1.getY()).get(cell1.getX()).setChecker(selectedCell.getChecker());
                            cells.get(selectedCell.getY()).get(selectedCell.getX()).setChecker(null);
                            onCheckerMoved(selectedCell, cell1);
                            onCheckerRemoved(cells.get(deleted.getY()).get(deleted.getX()));
                            cells.get(deleted.getY()).get(deleted.getX()).setChecker(null);
                            if (becomingQueen(cells.get(cell1.getY()).get(cell1.getX()))) {
                                onCheckerRemoved(cells.get(cell1.getY()).get(cell1.getX()));
                                onQueenAdded(cells.get(cell1.getY()).get(cell1.getX()));
                            }
                            //Если возможно съесть больше
                            if (canEatMore(cells.get(cell1.getY()).get(cell1.getX())).size() != 0)
                                count = 0;
                            else {
                                whoseMove = !whoseMove;
                                count = 0;
                            }
                            if (finishGame(whoseMove)) toFinish(whoseMove);
                        }
                        // Простой ход
                    } else {
                        cells.get(cell1.getY()).get(cell1.getX()).setChecker(selectedCell.getChecker());
                        cells.get(selectedCell.getY()).get(selectedCell.getX()).setChecker(null);
                        onCheckerMoved(selectedCell, cell1);
                        whoseMove = !whoseMove;
                        count = 0;
                        if (becomingQueen(cells.get(cell1.getY()).get(cell1.getX()))) {
                            onCheckerRemoved(cells.get(cell1.getY()).get(cell1.getX()));
                            onQueenAdded(cells.get(cell1.getY()).get(cell1.getX()));
                        }
                    }
                }
            }
            boardClear(viewPick);
            viewPick.clear();
        }
        if (count == 0) {
            Cell cell = new Cell(Integer.parseInt(((View) view.getParent()).getTag().toString()), Integer.parseInt(view.getTag().toString()),
                    cells.get(Integer.parseInt(((View) view.getParent()).getTag().toString())).get((Integer.parseInt(view.getTag().toString()))).getChecker());
            //не позволяет выбрать шашку оппонента
            if (cell.getChecker() != null)
                if (canPick(whoseMove, cell)) {
                    List<Pair<Cell, Cell>> requiredMoves = CheckersDesk.requiredMoves(whoseMove);
                    List<Pair<Cell, Cell>> pair = CheckersDesk.possibleWays(cell, whoseMove);
                    if (requiredMoves.size() == 0 && checkForMove(whoseMove)) toFinish(whoseMove);
                    if (requiredMoves.size() != 0) {
                        pairs = requiredMoves;
                        for (int i = 0; i <= requiredMoves.size() - 1; i++) {
                            LinearLayout row = (LinearLayout) findWithTag(table, String.valueOf(requiredMoves.get(i).second.getY()));
                            Objects.requireNonNull(row).findViewWithTag(String.valueOf(requiredMoves.get(i).second.getX())).setBackgroundColor(ContextCompat.getColor(this, R.color.pick));
                            LinearLayout row1 = (LinearLayout) findWithTag(table, String.valueOf(requiredMoves.get(i).first.getY()));
                            Objects.requireNonNull(row1).findViewWithTag(String.valueOf(requiredMoves.get(i).first.getX())).setBackgroundColor(ContextCompat.getColor(this, R.color.variants));
                            viewPick.add(Objects.requireNonNull(row).findViewWithTag(String.valueOf(requiredMoves.get(i).second.getX())));
                            viewPick.add(Objects.requireNonNull(row1).findViewWithTag(String.valueOf(requiredMoves.get(i).first.getX())));
                        }
                        selectedCell = cell;
                        if (canPickEater(requiredMoves, selectedCell)) count = 1;
                    } else {
                        pairs = pair;
                        if (cells.get(cell.getY()).get(cell.getX()).getChecker() != null) {
                            view.setBackgroundColor(ContextCompat.getColor(this, R.color.pick));
                            viewPick.add(view);
                        }
                        for (int i = 0; i <= pair.size() - 1; i++) {
                            LinearLayout row = (LinearLayout) findWithTag(table, String.valueOf(pair.get(i).first.getY()));
                            Objects.requireNonNull(row).findViewWithTag(String.valueOf(pair.get(i).first.getX())).setBackgroundColor(ContextCompat.getColor(this, R.color.variants));
                            viewPick.add(Objects.requireNonNull(row).findViewWithTag(String.valueOf(pair.get(i).first.getX())));
                        }
                        selectedCell = cell;
                        count = 1;
                    }
                }
        }
    }
}


