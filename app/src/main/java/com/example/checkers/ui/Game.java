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

import static com.example.checkers.model.CheckersDesk.whoseMove;

public class Game extends AppCompatActivity implements CheckersDesk.OnCheckerActionListener {
    public LinearLayout table;
    private final CheckersDesk desk = new CheckersDesk();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        table = findViewById(R.id.desk);

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

    public LinearLayout getCheckerLayout(Cell cell) {
        LinearLayout row = (LinearLayout) findWithTag(table, String.valueOf(cell.getY()));
        return Objects.requireNonNull(row).findViewWithTag(String.valueOf(cell.getX()));
    }

    //Добавляет картинку шашки на клетку
    @Override
    public void onCheckerAdded(Cell cell) {
        ImageView checkerImage = new ImageView(this);
        checkerImage.setImageResource(cell.getChecker().getColor() == CheckersDesk.Colors.WHITE ? R.drawable.white : R.drawable.black);
        getCheckerLayout(cell).addView(checkerImage);
    }

    //Добавляет картинку дамки на клетку
    @Override
    public void onQueenAdded(Cell cell) {
        ImageView checkerImage = new ImageView(this);
        checkerImage.setImageResource(cell.getChecker().getColor() == CheckersDesk.Colors.WHITE ? R.drawable.queen1 : R.drawable.queen0);
        getCheckerLayout(cell).addView(checkerImage);
    }

    //Перемещает картинку с одной клетки на другую
    @Override
    public void onCheckerMoved(Cell from, Cell to) {
        View checkerImage = getCheckerLayout(from).getChildAt(0);
        getCheckerLayout(from).removeView(checkerImage);
        getCheckerLayout(to).addView(checkerImage);
    }

    //Удаляет картинку с клетки
    @Override
    public void onCheckerRemoved(Cell cell) {
        View checkerImage = getCheckerLayout(cell).getChildAt(0);
        getCheckerLayout(cell).removeView(checkerImage);
    }

    //Подсвечивает клетки для обязательных ходов
    @Override
    public List<View> colorForRequiredMoves(List<Pair<Cell, Cell>> pairs) {
        List<View> views = new ArrayList<>();
        for (int i = 0; i <= pairs.size() - 1; i++) {
            getCheckerLayout(pairs.get(i).second).setBackgroundColor(ContextCompat.getColor(this, R.color.pick));
            getCheckerLayout(pairs.get(i).first).setBackgroundColor(ContextCompat.getColor(this, R.color.variants));
            views.add(getCheckerLayout(pairs.get(i).second));
            views.add(getCheckerLayout(pairs.get(i).first));
        }
        return views;
    }

    //Подсвечивает клетки для возможных ходов
    @Override
    public List<View> colorForPossibleMoves(List<Pair<Cell, Cell>> pairs, View view,List<List<Cell>> cells, Cell cell) {
        List<View> views = new ArrayList<>();
        if (cells.get(cell.getY()).get(cell.getX()).getChecker() != null) {
            view.setBackgroundColor(ContextCompat.getColor(this, R.color.pick));
            views.add(view);
        }
        for (int i = 0; i <= pairs.size() - 1; i++) {
            getCheckerLayout(pairs.get(i).first).setBackgroundColor(ContextCompat.getColor(this, R.color.variants));
            views.add(getCheckerLayout(pairs.get(i).first));
        }
        return views;
    }

    //Очищает доску от подсвеченных клеток
    @Override
    public void boardClear(List<View> views) {
        for (int i = 0; i <= views.size() - 1; i++) {
            views.get(i).setBackgroundColor(ContextCompat.getColor(this, R.color.brown));
        }
    }

    //Осуществляет переход на другую активити, если игра завершена
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
        if (desk.startGame(view)) toFinish(whoseMove);
    }
}
