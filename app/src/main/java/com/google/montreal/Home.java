package com.google.montreal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.cocoahero.android.geojson.GeoJSON;
import com.cocoahero.android.geojson.GeoJSONObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class Home extends AppCompatActivity {

    private int noOfMurals = 0;
    public static HashMap<Integer, JSONObject> muralJSONObjects = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        String murals = loadJSONFromAsset();

        TextView tv = findViewById(R.id.textview);

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
                if(image == null || image.length() < 5) {
                    continue;
                }
                muralJSONObjects.put(noOfMurals, mural);
                noOfMurals++;
            }

            tv.setText(String.valueOf(noOfMurals));

            Intent showContent = new Intent(this, Temp.class);
            startActivity(showContent);

        }

        catch (JSONException e) {
            e.printStackTrace();
        }
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