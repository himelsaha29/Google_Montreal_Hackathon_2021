package com.google.montreal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.concurrent.ThreadLocalRandom;

public class Randomizer extends AppCompatActivity {

    private Button generate;
    private ImageView imageView;
    private Button answerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randomizer);
        generate = findViewById(R.id.shuffle);
        imageView = findViewById(R.id.image);
        answerBtn = findViewById(R.id.answer);
        randomize();
        setOnAnswerButtonListener();

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randomize();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            double latitude = data.getExtras().getDouble("latitude");
            double longitude = data.getExtras().getDouble("longitude");

            System.out.println(latitude + "l:" + longitude);
        }
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

    public void setOnAnswerButtonListener(){
        answerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Randomizer.this, MapsActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }
}