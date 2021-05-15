package com.example.checkers.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.checkers.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void start(View view) {
        Intent intent = new Intent(MainActivity.this, Game.class);
        startActivity(intent);
        finish();
    }

    public void rules(View view) {
        Intent intent = new Intent(MainActivity.this, GameRules.class);
        startActivity(intent);
        finish();
    }

    public void exit(View view) {
        finishAffinity();
    }
}