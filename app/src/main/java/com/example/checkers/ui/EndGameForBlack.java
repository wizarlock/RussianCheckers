package com.example.checkers.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.checkers.R;

public class EndGameForBlack extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game_for_black);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void rematch(View view) {
        Intent intent = new Intent(EndGameForBlack.this, Game.class);
        startActivity(intent);
        finish();
    }

    public void mainMenu(View view) {
        Intent intent = new Intent(EndGameForBlack.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}