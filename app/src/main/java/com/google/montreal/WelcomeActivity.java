package com.google.montreal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class WelcomeActivity extends AppCompatActivity {
    private Button startGameBtn;
    private ImageView welcomeImageIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        setViews();
        setOnStartGameBtnClick();
    }

    private void setViews(){
        startGameBtn = findViewById(R.id.startGameBtn);
        welcomeImageIv = findViewById(R.id.welcomeImageIv);
    }

    private void setOnStartGameBtnClick(){
        startGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, Home.class);
                startActivity(intent);
            }
        });
    }
}