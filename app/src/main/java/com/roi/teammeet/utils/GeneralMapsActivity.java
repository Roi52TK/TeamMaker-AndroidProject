package com.roi.teammeet.utils;

import android.graphics.Rect;
import android.location.GpsStatus;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.roi.teammeet.R;
import com.roi.teammeet.screens.BaseActivity;

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
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

public abstract class GeneralMapsActivity extends BaseActivity implements MapListener, GpsStatus.Listener {

    private static final String TAG = "GeneralMapsActivity";

    protected MapView mMap;
    protected IMapController controller;
    protected MyLocationNewOverlay mMyLocationOverlay;
    protected Marker activeMarker;
    protected String currentAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeMap();

        // Load settings
        Configuration.getInstance().load(
                getApplicationContext(),
                getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
        );

        // Initialize the map
        mMap.setTileSource(TileSourceFactory.MAPNIK);
        mMap.setMultiTouchControls(true);
        mMap.getLocalVisibleRect(new Rect());

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
        Log.e(TAG, "onCreate: in " + controller.zoomIn());
        Log.e(TAG, "onCreate: out " + controller.zoomOut());

        // Add location overlay to the map
        mMap.getOverlays().add(mMyLocationOverlay);

        // Add map listener
        mMap.addMapListener(this);
    }

    protected abstract void initializeMap();

    @Override
    public boolean onScroll(ScrollEvent event) {
        Log.e(TAG, "onScroll: la " + event.getSource().getMapCenter().getLatitude());
        Log.e(TAG, "onScroll: lo " + event.getSource().getMapCenter().getLongitude());
        return true;
    }

    @Override
    public boolean onZoom(ZoomEvent event) {
        Log.e(TAG, "onZoom zoom level: " + event.getZoomLevel() + " source: " + event.getSource());
        return false;
    }

    @Override
    public void onGpsStatusChanged(int event) {
        // Not yet implemented
    }

    // Method to move the marker to a new position
    protected void moveMarkerTo(GeoPoint geoPoint) {
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


    protected void reverseGeocode(double latitude, double longitude) {
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
                 currentAddress = addressText.toString();
                Toast.makeText(this, currentAddress, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Address not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Geocoder service not available", Toast.LENGTH_SHORT).show();
        }
    }

    protected void geocodeAndCenterMap(String address) {
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

    protected void setAddress() {
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

    protected void setMarker(String street, String streetNumber, String city) {
        String address = street + " St " + streetNumber + "," + city;
        geocodeAndCenterMap(address);
    }

    protected void setMarker(double lat, double lang) {
        GeoPoint geoPoint = new GeoPoint(lat, lang);

        // Move the map to the geocoded coordinates
        mMap.getController().setCenter(geoPoint);
        mMap.getController().animateTo(geoPoint);

        // Set zoom level to focus closer
        mMap.getController().setZoom(20.0);

        //Add a marker at the location
        moveMarkerTo(geoPoint);
    }
}