package com.roi.teammeet.screens;

import android.content.Intent;
import android.graphics.Rect;
import android.location.GpsStatus;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.roi.teammeet.R;
import com.roi.teammeet.databinding.ActivityMapsBinding;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;

import android.location.Address;
import android.location.Geocoder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.events.MapEventsReceiver;

public class MapsActivity extends AppCompatActivity implements MapListener, GpsStatus.Listener, View.OnClickListener {

    private MapView mMap;
    private IMapController controller;
    private MyLocationNewOverlay mMyLocationOverlay;
    private Marker activeMarker;
    EditText etStreet;
    EditText etStreetNumber;
    EditText etCity;
    String street;
    String streetNumber;
    String city;
    String finalAddress;
    Button btnSetAddress;
    Button btnSetMarker;
    Button btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Load settings
        Configuration.getInstance().load(
                getApplicationContext(),
                getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
        );

        // Initialize the map
        mMap = binding.osmmap;
        mMap.setTileSource(TileSourceFactory.MAPNIK);
        mMap.setMultiTouchControls(true);
        mMap.getLocalVisibleRect(new Rect());

        // Add MapEventsOverlay to listen for taps
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint geoPoint) {
                // Reposition the marker at the new tap location
                moveMarkerTo(geoPoint);
                return true;
            }

            @Override
            public boolean longPressHelper(GeoPoint geoPoint) {
                return false; // Handle long press if needed
            }
        });

        // Add the overlay to the map
        mMap.getOverlays().add(mapEventsOverlay);

        // Initialize location overlay
        mMyLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mMap);
        controller = mMap.getController();

        // Enable my location overlay
        mMyLocationOverlay.enableMyLocation();
        mMyLocationOverlay.enableFollowLocation();
        mMyLocationOverlay.setDrawAccuracyEnabled(true);

        // Center map on first location fix
        mMyLocationOverlay.runOnFirstFix(new Runnable() {
            @Override
            public void run() {
                GeoPoint location = mMyLocationOverlay.getMyLocation();
                if (location != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            controller.setCenter(location);
                            controller.animateTo(location);
                        }
                    });
                } else {
                    Log.e("Location", "My location is null");
                }
            }
        });


        controller.setZoom(6.0);

        // Log zoom levels
        Log.e("TAG", "onCreate: in " + controller.zoomIn());
        Log.e("TAG", "onCreate: out " + controller.zoomOut());

        // Add location overlay to the map
        mMap.getOverlays().add(mMyLocationOverlay);

        // Add map listener
        mMap.addMapListener(this);

        // Initial Views
        initViews();

        // Set OnClickListeners
        btnSetAddress.setOnClickListener(this);
        btnSetMarker.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
    }

    private void initViews(){
        etStreet = findViewById(R.id.etStreet_maps);
        etStreetNumber = findViewById(R.id.etStreetNumber_maps);
        etCity = findViewById(R.id.etCity_maps);
        btnSetAddress = findViewById(R.id.btnGetAddress_maps);
        btnSetMarker = findViewById(R.id.btnSetMarker_maps);
        btnFinish = findViewById(R.id.btnFinish_maps);
    }

    @Override
    public boolean onScroll(ScrollEvent event) {
        Log.e("TAG", "onScroll: la " + event.getSource().getMapCenter().getLatitude());
        Log.e("TAG", "onScroll: lo " + event.getSource().getMapCenter().getLongitude());
        return true;
    }

    @Override
    public boolean onZoom(ZoomEvent event) {
        Log.e("TAG", "onZoom zoom level: " + event.getZoomLevel() + " source: " + event.getSource());
        return false;
    }

    @Override
    public void onGpsStatusChanged(int event) {
        // Not yet implemented
    }

    // Method to move the marker to a new position
    private void moveMarkerTo(GeoPoint geoPoint) {
        if (activeMarker == null) {
            // Create a new marker if it doesn't exist
            activeMarker = new Marker(mMap);
            activeMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            mMap.getOverlays().add(activeMarker);
        }

        // Update the marker's position
        activeMarker.setPosition(geoPoint);
        // Marker's title
        activeMarker.setTitle("Lat: " + geoPoint.getLatitude() + ", Lon: " + geoPoint.getLongitude());

        // Refresh the map to display changes
        mMap.invalidate();
    }


    public void reverseGeocode(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                StringBuilder addressText = new StringBuilder();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressText.append(address.getAddressLine(i)).append("\n");
                }
                //Set the address
                finalAddress = addressText.toString();
                Toast.makeText(this, finalAddress, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Address not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Geocoder service not available", Toast.LENGTH_SHORT).show();
        }
    }

    public void geocodeAndCenterMap(String address) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            // Get the list of addresses for the input address
            List<Address> addresses = geocoder.getFromLocationName(address, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address location = addresses.get(0);
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                // Log the coordinates for debugging
                Log.d("Geocode", "Lat: " + latitude + ", Lon: " + longitude);

                // Create a GeoPoint from the coordinates
                GeoPoint geoPoint = new GeoPoint(latitude, longitude);

                // Move the map to the geocoded coordinates
                mMap.getController().setCenter(geoPoint);
                mMap.getController().animateTo(geoPoint);

                // Set zoom level to focus closer
                mMap.getController().setZoom(20.0);

                //Add a marker at the location
                moveMarkerTo(geoPoint);

            } else {
                Toast.makeText(this, "Address not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Geocoder service not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        if(view == btnSetAddress){
            setAddress();
        }
        if(view == btnSetMarker){
            setMarker();
        }
        if(view == btnFinish){
            if(!finalAddress.isEmpty()){
                Intent resultIntent = new Intent();
                resultIntent.putExtra("lang", String.valueOf(activeMarker.getPosition().getLongitude()));
                resultIntent.putExtra("lat", String.valueOf(activeMarker.getPosition().getLatitude()));
                resultIntent.putExtra("address", finalAddress);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        }
    }



    private void setAddress() {
        if (activeMarker != null) {
            // Get the marker's coordinates
            double latitude = activeMarker.getPosition().getLatitude();
            double longitude = activeMarker.getPosition().getLongitude();

            GeoPoint geoPoint = new GeoPoint(latitude, longitude);

            // Move the map to the geocoded coordinates
            mMap.getController().setCenter(geoPoint);
            mMap.getController().animateTo(geoPoint);

            // Set zoom level to focus closer
            mMap.getController().setZoom(20.0);

            // Reverse geocode the marker's location
            reverseGeocode(latitude, longitude);
        } else {
            Toast.makeText(this, "No marker is placed on the map!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setMarker() {
        street = etStreet.getText().toString();
        streetNumber = etStreetNumber.getText().toString();
        city = etCity.getText().toString();

        String address = street + " St " + streetNumber + "," + city;
        geocodeAndCenterMap(address);
    }
}