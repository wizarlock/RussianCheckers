package com.example.checkers.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.example.checkers.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Button rules= findViewById(R.id.rules_button);
        rules.setOnClickListener(v -> {
        Intent intent =  new Intent (MainActivity.this, GameRules.class);
         startActivity(intent);
         finish();
        });

        Button start = findViewById(R.id.start_button);
        start.setOnClickListener(v -> {
            Intent intent =  new Intent (MainActivity.this, Game.class);
            startActivity(intent);
            finish();
        });

        Button exit= findViewById(R.id.exit_button);
        exit.setOnClickListener(v -> {
            finishAffinity();
        });
    }
}