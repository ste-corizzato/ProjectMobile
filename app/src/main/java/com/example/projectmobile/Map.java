package com.example.projectmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;


import com.google.android.gms.tasks.OnSuccessListener;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

public class Map extends AppCompatActivity implements OnMapReadyCallback, Style.OnStyleLoaded, LocationEngineCallback<LocationEngineResult> {


    private static final String TAG = "";
    private static final int ERROR = 1 ;
    private LocationComponent locationComponent;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private final long DEFAULT_INTERVAL_IN_MILLISECONDS = 200L; //1000L
    private final long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 1; //5
    private LocationEngine locationEngine;
    private boolean isLocationUpdateActive;
    private Location currentLocation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1Ijoibmljb2xhc3JhZGljY2hpIiwiYSI6ImNrN3VoNWR0YzB6aGEzZnNmOWhudHMxcjgifQ.NLpLVz5Ji-0Afy3gqP5H9g");
        setContentView(R.layout.activity_map);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync( this);

       if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

        } else {


        }


    }

    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, this);

    }







    @Override
    public void onStyleLoaded(@NonNull Style style) {

        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(45.4773,9.1815))
                .zoom(11)
                .tilt(20)
                .build();
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1000);
        //enableLocationComponent(style);
        //initLocationEngine();

    }




    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) { //Attiva il componente che mostra la posizione dell'utente sulla mappa
        locationComponent = mapboxMap.getLocationComponent();

        LocationComponentOptions locationComponentOptions = LocationComponentOptions.builder(this)
                .bearingTintColor(getColor(R.color.colorAccent))
                .backgroundTintColor(getColor(R.color.colorAccent))
                .accuracyAlpha(0f)
                .build();

        LocationComponentActivationOptions locationComponentActivationOptions =
                LocationComponentActivationOptions.builder(this, loadedMapStyle)
                        .useDefaultLocationEngine(false)
                        .locationComponentOptions(locationComponentOptions)
                        .build();

        locationComponent.activateLocationComponent(locationComponentActivationOptions);

        locationComponent.setLocationComponentEnabled(true);

        locationComponent.setCameraMode(CameraMode.TRACKING);
        locationComponent.setRenderMode(RenderMode.COMPASS);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void stopLocationEngine() {
        locationEngine.removeLocationUpdates(this);
        isLocationUpdateActive = false;
    }

        @SuppressLint("MissingPermission")
        private void initLocationEngine() {//Avvia il location engine con i vari parametri



        locationEngine = LocationEngineProvider.getBestLocationEngine(this);
            LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                    .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                    .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();
            locationEngine.requestLocationUpdates(request, this, getMainLooper());
            locationEngine.getLastLocation( this);
            isLocationUpdateActive = true;

        }


    @Override
    public void onSuccess(LocationEngineResult result) {
        currentLocation = result.getLastLocation();
        Log.d(TAG, "onSuccess: "+currentLocation);
        if (currentLocation == null) {
            Toast.makeText(this, ERROR, Toast.LENGTH_LONG).show();
            Intent backHomeIntent = new Intent(this, MainActivity.class);
            startActivity(backHomeIntent);
            return;
        }
        if (this.mapboxMap!= null) {
            this.mapboxMap.getLocationComponent().forceLocationUpdate(currentLocation);
        }
    }

    @Override
    public void onFailure(@NonNull Exception exception) {

    }


}
