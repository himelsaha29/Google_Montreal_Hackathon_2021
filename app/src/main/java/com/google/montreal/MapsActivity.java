package com.google.montreal;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    private Marker marker;
    private LatLng markerPosition;
    private Dialog dialog;
    private Marker correctAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String dialogShown = prefs.getString("Dialog", "No");

        if (!dialogShown.equalsIgnoreCase("Done")) {

            dialog = new Dialog(MapsActivity.this);
            dialog.setContentView(R.layout.maps_dialog);
            dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.95), ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(true);
            dialog.show();

            SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
            editor.putString("Dialog", "Done");
            editor.apply();
        }

        mMap = googleMap;
        mMap.setMinZoomPreference(11.5f);
        markerPosition = new LatLng(45.5017, -73.5673);
        marker = mMap.addMarker(new MarkerOptions().position(markerPosition).draggable(true).title("Marker in Montreal"));
        double lastActualLat = Randomizer.getLastActualLat();
        double lastActualLong = Randomizer.getLastActualLong();
        if(lastActualLat!=0 && lastActualLong !=0){
            correctAnswer = mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    .position(new LatLng(lastActualLat, lastActualLong))
                    .draggable(false)
                    .title("last correct"));
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(markerPosition));
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMarkerDragListener(this);
    }
    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        markerPosition = marker.getPosition();
        // Check if a click count was set, then display the click count.
        if (markerPosition != null) {
            marker.setTag(markerPosition);
            //Toast.makeText(this, "position selected" + markerPosition,Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            Double latitude = markerPosition.latitude;
            Double longitude = markerPosition.longitude;
            Randomizer.chosenCoordinates(latitude, longitude);
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            setResult(RESULT_OK, intent);
            finish();
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {

    }

    @Override
    public void onMarkerDrag(@NonNull Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {
        markerPosition = marker.getPosition();
    }
}