package com.example.checkers.ui;

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
import static com.example.checkers.model.CheckersDesk.cells;
import static java.lang.Math.abs;


public class Game extends AppCompatActivity implements CheckersDesk.OnCheckerActionListener {
    Boolean whoseMove = true;
    CheckersDesk desk;
    LinearLayout table;
    int count;
    Cell selectedCell;
    List<Pair<Cell, Cell>> pairs;
    List<View> viewPick = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        table = findViewById(R.id.desk);

        desk = new CheckersDesk();
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
    public void boardClear(List<Pair<Cell, Cell>> pairs, List<View> views) {
        for (int i = 0; i <= pairs.size() - 1; i++) {
            LinearLayout row = (LinearLayout) findWithTag(table, String.valueOf(pairs.get(i).first.getY()));
            Objects.requireNonNull(row).findViewWithTag(String.valueOf(pairs.get(i).first.getX())).setBackgroundColor(ContextCompat.getColor(this, R.color.brown));
        }
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
    public boolean canPick (boolean whoseMove, Cell cell) {
        if (whoseMove) return cell.getChecker().getColor() == BLACK;
        else return cell.getChecker().getColor() == WHITE;
    }
    //не позволяет любой шашке есть, есть может только та, которая есть в списке
    public boolean canPickEater (List<Pair<Cell, Cell>> requiredMoves,Cell selectedCell) {
        for (int i = 0; i <= requiredMoves.size() - 1; i++) {
            if (selectedCell.equals(requiredMoves.get(i).second)) return true;
        }
        return false;
    }

    public void onClick(View view) {
        if (count % 2 == 1) {
            Cell cell1 = new Cell(Integer.parseInt(((View) view.getParent()).getTag().toString()), Integer.parseInt(view.getTag().toString()),
                    cells.get(Integer.parseInt(((View) view.getParent()).getTag().toString())).get((Integer.parseInt(view.getTag().toString()))).getChecker());
            //позволяет перевыбрать шашку
            if (cell1.getChecker() != null) {
                if (cell1.getChecker().getColor() == selectedCell.getChecker().getColor()) count++;
            } else {
                if (canMove(pairs, cell1)) {
                    cells.get(cell1.getY()).get(cell1.getX()).setChecker(selectedCell.getChecker());
                    cells.get(selectedCell.getY()).get(selectedCell.getX()).setChecker(null);
                    onCheckerMoved(selectedCell, cell1);
                    //если можно съесть
                    if (pairs.get(0).second != null) {
                        onCheckerRemoved(cells.get(abs(cell1.getY() + selectedCell.getY()) / 2).get((abs(cell1.getX() + selectedCell.getX())) / 2));
                        cells.get(abs(cell1.getY() + selectedCell.getY()) / 2).get(abs(cell1.getX() + selectedCell.getX()) / 2).setChecker(null);
                    }
                    whoseMove = !whoseMove;
                    count++;
                }
            }
            boardClear(pairs, viewPick);
            viewPick.clear();
            }
        if (count % 2 == 0) {
            Cell cell = new Cell(Integer.parseInt(((View) view.getParent()).getTag().toString()), Integer.parseInt(view.getTag().toString()),
                    cells.get(Integer.parseInt(((View) view.getParent()).getTag().toString())).get((Integer.parseInt(view.getTag().toString()))).getChecker());
            //не позволяет выбрать шашку оппонента
            if (cell.getChecker() != null)
            if (canPick(whoseMove, cell)) {
                List<Pair<Cell, Cell>> requiredMoves = CheckersDesk.requiredMoves(whoseMove);
                List<Pair<Cell, Cell>> pair = CheckersDesk.possibleWays(cell, whoseMove);
                if (requiredMoves.size() != 0) {
                    pairs = requiredMoves;
                    for (int i = 0; i <= requiredMoves.size() - 1; i++) {
                        LinearLayout row = (LinearLayout) findWithTag(table, String.valueOf(requiredMoves.get(i).second.getY()));
                        Objects.requireNonNull(row).findViewWithTag(String.valueOf(requiredMoves.get(i).second.getX())).setBackgroundColor(ContextCompat.getColor(this, R.color.pick));
                        LinearLayout row1 = (LinearLayout) findWithTag(table, String.valueOf(requiredMoves.get(i).first.getY()));
                        Objects.requireNonNull(row1).findViewWithTag(String.valueOf(requiredMoves.get(i).first.getX())).setBackgroundColor(ContextCompat.getColor(this, R.color.variants));
                        viewPick.add(Objects.requireNonNull(row).findViewWithTag(String.valueOf(requiredMoves.get(i).second.getX())));
                    }
                    selectedCell = cell;
                    if (canPickEater(requiredMoves, selectedCell)) count++;
                } else {
                    pairs = pair;
                    if (cells.get(cell.getY()).get(cell.getX()).getChecker() != null) {
                        view.setBackgroundColor(ContextCompat.getColor(this, R.color.pick));
                        viewPick.add(view);
                    }
                    for (int i = 0; i <= pair.size() - 1; i++) {
                        LinearLayout row = (LinearLayout) findWithTag(table, String.valueOf(pair.get(i).first.getY()));
                        Objects.requireNonNull(row).findViewWithTag(String.valueOf(pair.get(i).first.getX())).setBackgroundColor(ContextCompat.getColor(this, R.color.variants));
                    }
                    selectedCell = cell;
                    count++;
                }
            }
        }
        }
    }
    //осталось сделать дамки и есть пока возможно

