package com.example.checkers.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.checkers.R;

public class EndGameForWhite extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game_for_white);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void rematch(View view) {
        Intent intent = new Intent(EndGameForWhite.this, Game.class);
        startActivity(intent);
        finish();
    }

    public void mainMenu(View view) {
        Intent intent = new Intent(EndGameForWhite.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}