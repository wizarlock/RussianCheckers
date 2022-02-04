package com.example.checkers.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.checkers.R;
import com.example.checkers.data.OnCheckerActionListener;
import com.example.checkers.databinding.FragmentGameBinding;
import com.example.checkers.model.Cell;
import com.example.checkers.model.Checker;
import com.example.checkers.model.CheckersDesk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Game extends Fragment implements OnCheckerActionListener {
    private FragmentGameBinding binding;
    public final CheckersDesk gameDesk = new CheckersDesk();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGameBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        animationAppearance(binding.desk, () -> {
            gameDesk.checkersDesk();
            gameDesk.setOnCheckerActionListener(Game.this);
            gameDesk.initDesk();
            for (int i = 0; i <= 7; i++)
                for (int j = 0; j <= 7; j++) {
                    ViewGroup gr = (ViewGroup) binding.desk.getChildAt(i);
                    gr.getChildAt(j).setOnClickListener(gameDesk::startGame);

                }
        });
    }

    private void animationAppearance(View view, Runnable onAnimationEnd) {
        view.animate()
                .alpha(0f)
                .setDuration(0)
                .withEndAction(() -> view.animate().alpha(1f).setDuration(2000).withEndAction(onAnimationEnd));
    }

    private View findWithTag(ViewGroup parent, Object tag) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            if (parent.getChildAt(i).getTag().equals(tag))
                return parent.getChildAt(i);
        }
        return null;
    }

    public LinearLayout getCheckerLayout(Cell cell) {
        LinearLayout row = (LinearLayout) findWithTag(binding.desk, String.valueOf(cell.getY()));
        return Objects.requireNonNull(row).findViewWithTag(String.valueOf(cell.getX()));
    }

    @Override
    public void onCheckerAdded(Cell cell) {
        ImageView checkerImage = new ImageView(requireContext());
        checkerImage.setImageResource(cell.getChecker().getColor() == Checker.Colors.WHITE ? R.drawable.white : R.drawable.black);
        getCheckerLayout(cell).addView(checkerImage);
    }

    @Override
    public void onQueenAdded(Cell cell) {
        ImageView checkerImage = new ImageView(requireContext());
        checkerImage.setImageResource(cell.getChecker().getColor() == Checker.Colors.WHITE ? R.drawable.queen1 : R.drawable.queen0);
        getCheckerLayout(cell).addView(checkerImage);
    }

    @Override
    public void onCheckerMoved(Cell from, Cell to) {
        View checkerImage = getCheckerLayout(from).getChildAt(0);
        getCheckerLayout(from).removeView(checkerImage);
        getCheckerLayout(to).addView(checkerImage);
    }

    @Override
    public void onCheckerRemoved(Cell cell) {
        View checkerImage = getCheckerLayout(cell).getChildAt(0);
        getCheckerLayout(cell).removeView(checkerImage);
    }

    @Override
    public List<View> colorForMoves(List<Map<Cell, Cell>> pairs, View view, List<List<Cell>> cells, Cell cell) {
        List<View> views = new ArrayList<>();
        if (cells.get(cell.getY()).get(cell.getX()).getChecker() != null) {
            view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.pick));
            views.add(view);
        }
        for (int i = 0; i <= pairs.size() - 1; i++) {
            for (Map.Entry<Cell, Cell> entry : pairs.get(i).entrySet()) {
                getCheckerLayout(entry.getKey()).setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.variants));
                views.add(getCheckerLayout(entry.getKey()));
            }
        }
        return views;
    }

    @Override
    public void boardClear(List<View> views) {
        for (int i = 0; i <= views.size() - 1; i++) {
            views.get(i).setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.brown));
        }
    }
}
