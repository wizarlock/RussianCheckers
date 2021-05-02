package com.example.checkers.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.checkers.R;

public class GameRules extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rules);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Button back= findViewById(R.id.back);
        back.setOnClickListener(v -> {
            Intent intent =  new Intent (GameRules.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}