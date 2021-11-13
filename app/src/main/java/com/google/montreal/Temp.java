package com.google.montreal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.concurrent.ThreadLocalRandom;

public class Temp extends AppCompatActivity {

    private Button generate;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        generate = findViewById(R.id.button);
        imageView = findViewById(R.id.image);

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int randomNum = ThreadLocalRandom.current().nextInt(0, 233 + 1);
                String image = null;
                try {
                    image = (String) WelcomeActivity.muralJSONObjects.get(randomNum).getJSONObject("properties").get("image");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Picasso.get().load(image).into(imageView);

            }
        });
    }
}