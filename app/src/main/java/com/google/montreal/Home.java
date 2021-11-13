package com.google.montreal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.cocoahero.android.geojson.GeoJSON;
import com.cocoahero.android.geojson.GeoJSONObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        String murals = loadJSONFromAsset();

        TextView tv = findViewById(R.id.textview);

        try {
            GeoJSONObject geoJSON = GeoJSON.parse(murals);
            JSONArray jsarray = geoJSON.toJSON().getJSONArray("features");

            String jsobject = (String) jsarray.getJSONObject(1).getJSONObject("properties").get("artiste");

            System.out.println(jsobject);
            tv.setText(jsobject.toString());
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