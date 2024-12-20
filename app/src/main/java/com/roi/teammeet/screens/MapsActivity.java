package com.roi.teammeet.screens;

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

public class MapsActivity extends AppCompatActivity implements MapListener, GpsStatus.Listener {

    private MapView mMap;
    private IMapController controller;
    private MyLocationNewOverlay mMyLocationOverlay;

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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        controller.setCenter(mMyLocationOverlay.getMyLocation());
                        controller.animateTo(mMyLocationOverlay.getMyLocation());
                    }
                });
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


}
