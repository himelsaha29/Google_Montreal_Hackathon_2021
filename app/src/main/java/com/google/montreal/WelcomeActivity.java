package com.google.montreal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.cocoahero.android.geojson.GeoJSON;
import com.cocoahero.android.geojson.GeoJSONObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class WelcomeActivity extends AppCompatActivity {
    public static HashMap<Integer, JSONObject> muralJSONObjects = new HashMap<>();
    private Button startGameBtn;
    private ImageView welcomeImageIv;
    private int noOfMurals = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        setViews();
        setOnStartGameBtnClick();

        String murals = loadJSONFromAsset();
        try {
            GeoJSONObject geoJSON = GeoJSON.parse(murals);
            JSONArray jsarray = geoJSON.toJSON().getJSONArray("features");

            for (int i = 0; i < jsarray.length(); i++) {
                JSONObject mural = jsarray.getJSONObject(i);
                String image = null;
                try {
                    double latitude = Double.valueOf((double) mural.getJSONObject("properties").get("latitude"));
                    double longitude = Double.valueOf((double) mural.getJSONObject("properties").get("longitude"));
                    image = (String) mural.getJSONObject("properties").get("image");
                } catch (Exception e) {
                    System.out.println("Error : " + e.getMessage());
                    continue;
                }
                if (image == null || image.length() < 5) {
                    continue;
                }
                muralJSONObjects.put(noOfMurals, mural);
                noOfMurals++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setViews() {
        startGameBtn = findViewById(R.id.startGameBtn);
        welcomeImageIv = findViewById(R.id.welcomeImageIv);
    }

    private void setOnStartGameBtnClick() {
        startGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, Randomizer.class);
                startActivity(intent);
            }
        });
    }

    private String loadJSONFromAsset() {
        String json = null;
        InputStream is = null;
        try {
            is = this.getAssets().open("murales_geojson.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}