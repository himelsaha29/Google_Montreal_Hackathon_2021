package com.google.montreal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.concurrent.ThreadLocalRandom;

public class Randomizer extends AppCompatActivity {

    private Button generate;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randomizer);
        generate = findViewById(R.id.shuffle);
        imageView = findViewById(R.id.image);

        randomize();

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randomize();
            }
        });
    }


    private void randomize() {
        int randomNum = ThreadLocalRandom.current().nextInt(0, 233 + 1);
        String image = null;
        try {
            image = (String) WelcomeActivity.muralJSONObjects.get(randomNum).getJSONObject("properties").get("image");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Picasso.get().load(image).into(imageView);
    }
}