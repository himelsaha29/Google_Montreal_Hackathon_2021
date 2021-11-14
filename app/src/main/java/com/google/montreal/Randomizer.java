package com.google.montreal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;


public class Randomizer extends AppCompatActivity {

    private Button generate;
    private ImageView imageView;
    private Button answerBtn;
    private static double latitude = 0.0;
    private static double longitude = 0.0;

    private double latTemp = latitude;
    private double lonTemp = longitude;
    private static JSONObject selectedObject;
    private LinearLayout linear;
    private LinearLayout linear2;
    private Button next;
    private Button revealBtn;
    private TextView answer;
    private Dialog dialog;

    private TextView artist;
    private TextView organization;
    private TextView address;
    private TextView arrondissement;
    private TextView year;
    private TextView program;
    private TextView latitudeInfo;
    private TextView longitudeInfo;
    private TextView globalHighScore;

    private int score = 0;
    private int scoreTemp = score;

    private TextView scoreTV;

    DatabaseReference firebase;
    private int highScore = 0;
    private static double lastLat=0;
    private static double lastLong=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randomizer);
        generate = findViewById(R.id.shuffle);
        imageView = findViewById(R.id.image);
        answerBtn = findViewById(R.id.answer);
        revealBtn = findViewById(R.id.view_card);
        next = findViewById(R.id.next);
        linear = findViewById(R.id.linear);
        linear2 = findViewById(R.id.linear2);
        answer = findViewById(R.id.correct);
        randomize();
        setOnAnswerButtonListener();
        setNextButtonListener();
        firebase = FirebaseDatabase.getInstance().getReference().child("Global score");

        latTemp = latitude;
        lonTemp = longitude;

        dialog = new Dialog(Randomizer.this);
        dialog.setContentView(R.layout.mural_dialog);
        dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.95), ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        artist = dialog.findViewById(R.id.artist);
        arrondissement = dialog.findViewById(R.id.arrondissement);
        year = dialog.findViewById(R.id.year);
        program = dialog.findViewById(R.id.program);
        latitudeInfo = dialog.findViewById(R.id.latitude);
        longitudeInfo = dialog.findViewById(R.id.longitude);
        address = dialog.findViewById(R.id.address);
        organization = dialog.findViewById(R.id.organization);
        globalHighScore = findViewById(R.id.highscore);
        getDatabaseData();

        scoreTV = findViewById(R.id.score);


        revealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                try {
                    artist.setText((String) selectedObject.get("artiste"));
                    arrondissement.setText((String) selectedObject.get("arrondissement"));
                    year.setText((String) selectedObject.get("annee"));
                    program.setText((String) selectedObject.get("programme_entente"));
                    latitudeInfo.setText(String.valueOf((double) selectedObject.get("latitude")));
                    longitudeInfo.setText(String.valueOf((double) selectedObject.get("longitude")));
                    address.setText((String) selectedObject.get("adresse"));
                    organization.setText((String) selectedObject.get("organisme"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randomize();
                getDatabaseData();
            }
        });
        getDatabaseData();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            double latitude = data.getExtras().getDouble("latitude");
            double longitude = data.getExtras().getDouble("longitude");

            System.out.println("Latitude : " + latitude + ", longitude : " + longitude);
        }
    }

    public static void chosenCoordinates(double lat, double lon) {
        latitude = lat;
        longitude = lon;

        try {
            lastLat = (double) selectedObject.get("latitude");

            lastLong = (double) selectedObject.get("longitude");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        lastLat=latitude;
        lastLong=longitude;
        System.out.println("Latitude : " + latitude + ", longitude : " + longitude);
    }
    public static double getLastActualLong() {
        return lastLong;
    }
    public static double getLastActualLat() {
        return lastLat;
    }

    private void randomize() {
        int randomNum = (int)Math.floor(Math.random()*(233-0+1)+0);
        String image = null;
        try {
            selectedObject = WelcomeActivity.muralJSONObjects.get(randomNum).getJSONObject("properties");
            image = (String) selectedObject.get("image");
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Random number : " + randomNum);

        Picasso.get().load(image).into(imageView);
    }

    public void setOnAnswerButtonListener(){
        answerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Randomizer.this, MapsActivity.class);
                startActivityForResult(intent, 1);
                scoreTemp = score;
            }
        });
    }

    public void setNextButtonListener(){
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                randomize();
                linear.setVisibility(View.VISIBLE);
                linear2.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (latTemp != latitude || lonTemp != longitude) {
            double actualLat = 0;
            double actualLon = 0;
            try {
                actualLat = (double) selectedObject.get("latitude");
                lastLat = actualLat;
                actualLon = (double) selectedObject.get("longitude");
                lastLong=actualLon;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            double latDifference = Math.abs(latitude - actualLat);
            double lonDifference = Math.abs(longitude - actualLon);

            if ((latDifference < 0.001) && (lonDifference < 0.001)) {
                // Toast.makeText(this, "Correct answer",Toast.LENGTH_SHORT).show();
                answer.setText("Correct answer");
                score++;
                scoreTV.setText("Score : " + score);
            } else {
                answer.setText("Wrong answer. Try again");
                score--;

                if (scoreTemp > score) {
                    score = 0;
                    scoreTemp = score;
                }

                scoreTV.setText("Score : " + score);
                GlobalScore gs = new GlobalScore(score);
                if(highScore != 0 && highScore < score) {
                    firebase.push().setValue(gs);
                }
            }

            linear.setVisibility(View.GONE);
            linear2.setVisibility(View.VISIBLE);

            latTemp = latitude;
            lonTemp = longitude;
            getDatabaseData();
        }
        getDatabaseData();
    }

    private int getDatabaseData() {
        firebase = FirebaseDatabase.getInstance().getReference().child("Global score");
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            int highScoreTemp = 0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String scoreString = ds.child("score").getValue().toString();
                    int score = Integer.valueOf(scoreString);
                    if (score > highScoreTemp) {
                        highScoreTemp = score;
                    }
                }
                highScore = highScoreTemp;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        globalHighScore.setText("Global high score : " + highScore);
        return highScore;
    }

}