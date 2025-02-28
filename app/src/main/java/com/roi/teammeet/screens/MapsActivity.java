package com.roi.teammeet.screens;

import android.content.Intent;
import android.graphics.Rect;
import android.location.GpsStatus;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.roi.teammeet.R;
import com.roi.teammeet.databinding.ActivityMapsBinding;
import com.roi.teammeet.utils.GeneralMapsActivity;

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

public class MapsActivity extends GeneralMapsActivity implements MapListener, GpsStatus.Listener, View.OnClickListener {

    private static final String TAG = "MapsActivity";

    EditText etStreet;
    EditText etStreetNumber;
    EditText etCity;
    String street;
    String streetNumber;
    String city;
    Button btnSetAddress;
    Button btnSetMarker;
    Button btnFinish;
    Intent mapsIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        mMap.getOverlays().add(mapEventsOverlay);

        initViews();
        btnSetAddress.setOnClickListener(this);
        btnSetMarker.setOnClickListener(this);
        btnFinish.setOnClickListener(this);

        mapsIntent = getIntent();
        String intentLatSt = mapsIntent.getStringExtra("lat");
        String intentLangSt = mapsIntent.getStringExtra("lang");
        double intentLat;
        double intentLang;
        if(intentLatSt != null && intentLangSt != null){
            intentLat = Double.parseDouble(intentLatSt);
            intentLang = Double.parseDouble(intentLangSt);

            if(intentLat != 0 && intentLang != 0){
                Log.e(TAG, "Lat: " + intentLat + "; Lang: " + intentLang);
                setMarker(intentLat, intentLang);
                reverseGeocode(intentLat, intentLang);
                //TODO: Fix zoom in not working issue
            }
        }
    }

    @Override
    protected void initializeMap() {
        ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mMap = binding.osmmap;
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
    public void onClick(View view) {
        if(view == btnSetAddress){
            setAddress();
        }
        if(view == btnSetMarker){
            onClickSetMarker();
        }
        if(view == btnFinish){
            if(!currentAddress.isEmpty()){
                Intent resultIntent = new Intent();
                resultIntent.putExtra("lat", String.valueOf(activeMarker.getPosition().getLatitude()));
                resultIntent.putExtra("lang", String.valueOf(activeMarker.getPosition().getLongitude()));
                resultIntent.putExtra("address", currentAddress);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        }
    }

    private void onClickSetMarker() {
        street = etStreet.getText().toString();
        streetNumber = etStreetNumber.getText().toString();
        city = etCity.getText().toString();

        setMarker(street, streetNumber, city);
    }
}