package com.example.checkers.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.checkers.R;
import com.example.checkers.model.Cell;
import com.example.checkers.model.CheckersDesk;

import java.util.Objects;


public class Game extends AppCompatActivity implements CheckersDesk.OnCheckerActionListener {

    CheckersDesk desk;
    LinearLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        table = ((LinearLayout) findViewById(R.id.desk));

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
        Cell position = new Cell (Integer.parseInt(view.getTag().toString()),Integer.parseInt(((View) view.getParent()).getTag().toString()),null);


    }
}
