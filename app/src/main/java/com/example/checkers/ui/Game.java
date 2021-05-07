package com.example.checkers.ui;

import android.content.Context;
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

import java.util.List;
import java.util.Objects;


public class Game extends AppCompatActivity implements CheckersDesk.OnCheckerActionListener {
    Boolean whoseMove = true;
    CheckersDesk desk;
    LinearLayout table;

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
    public void onCheckerMoved(Cell from, Cell to, CheckersDesk.Checker checker) {

    }


    public void onCheckerRemoved(Cell cell) {
        LinearLayout row = (LinearLayout) findWithTag(table, String.valueOf(cell.getY()));
        ImageView checkerImage = new ImageView(this);
        checkerImage.setImageDrawable(null);
        LinearLayout layout = Objects.requireNonNull(row).findViewWithTag(String.valueOf(cell.getX()));
        layout.addView(checkerImage);
    }

    public void onClick(View view) {
        List<Pair<Cell, Cell>> requiredMoves = CheckersDesk.requiredMoves(whoseMove);
        Cell cell = new Cell(Integer.parseInt(view.getTag().toString()), Integer.parseInt(((View) view.getParent()).getTag().toString()),
                CheckersDesk.cells.get(Integer.parseInt(((View) view.getParent()).getTag().toString())).get((Integer.parseInt(view.getTag().toString()))).getChecker());
        if (requiredMoves.size() != 0) {
            for (int i = 0; i <= requiredMoves.size() - 1; i++) {
                LinearLayout row = (LinearLayout) findWithTag(table, String.valueOf(requiredMoves.get(i).second.getX()));
                Objects.requireNonNull(row).findViewWithTag(String.valueOf(requiredMoves.get(i).second.getY())).setBackgroundColor(ContextCompat.getColor(this, R.color.pick));
                LinearLayout row1 = (LinearLayout) findWithTag(table, String.valueOf(requiredMoves.get(i).first.getX()));
                Objects.requireNonNull(row1).findViewWithTag(String.valueOf(requiredMoves.get(i).first.getY())).setBackgroundColor(ContextCompat.getColor(this, R.color.variants));
            }
        } else {
            if (CheckersDesk.cells.get(cell.getX()).get(cell.getY()).getChecker() != null)
                view.setBackgroundColor(ContextCompat.getColor(this, R.color.pick));
            List<Pair<Cell, Cell>> pair = CheckersDesk.possibleWays(cell, whoseMove);
            for (int i = 0; i <= pair.size() - 1; i++) {
                LinearLayout row = (LinearLayout) findWithTag(table, String.valueOf(pair.get(i).first.getX()));
                Objects.requireNonNull(row).findViewWithTag(String.valueOf(pair.get(i).first.getY())).setBackgroundColor(ContextCompat.getColor(this, R.color.variants));
            }
        }
    }
}

