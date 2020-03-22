package com.example.projectmobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.annotation.NonNull;
// Classes needed to initialize the map
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
// Classes needed to handle location permissions
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import java.util.List;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import java.lang.ref.WeakReference;

import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;

import org.json.JSONException;
import org.json.JSONObject;

import static com.mapbox.mapboxsdk.style.layers.Property.ICON_ROTATION_ALIGNMENT_VIEWPORT;

public class Map extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener, LocationEngineCallback<LocationEngineResult>{

    // Variabili per inizializzare la mappa
    private MapboxMap mapboxMap;
    private MapView mapView;
    // Variabili per i permessi
    private PermissionsManager permissionsManager;

    private LocationEngine locationEngine;
    private long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
    private long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;
    private Location currentLocation;
    private final String GPS_ERROR = " Si è verificato un errore nel calcolo della posizione.\nPer favore, controlla di aver attivato il GPS e riprova più tardi.";
    public RequestQueue mRequestQueue = null;

    private final String MONSTER = "MO";
    private final String CANDY= "CA";



    private SymbolManager symbolManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Mapbox.getInstance(this, "pk.eyJ1Ijoibmljb2xhc3JhZGljY2hpIiwiYSI6ImNrN3VoNWR0YzB6aGEzZnNmOWhudHMxcjgifQ.NLpLVz5Ji-0Afy3gqP5H9g");


        setContentView(R.layout.activity_map);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }


    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {

    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(Style.TRAFFIC_NIGHT,
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);


                    }
                });
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {

        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            LocationComponent locationComponent = mapboxMap.getLocationComponent();


            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(this, loadedMapStyle)
                            .useDefaultLocationEngine(false)
                            .build();


            locationComponent.activateLocationComponent(locationComponentActivationOptions);


            locationComponent.setLocationComponentEnabled(true);


            locationComponent.setCameraMode(CameraMode.TRACKING);

            locationComponent.setRenderMode(RenderMode.COMPASS);

            initLocationEngine();
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }


    @SuppressLint("MissingPermission")
    private void initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this);

        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();

        locationEngine.requestLocationUpdates(request, this, getMainLooper());
        locationEngine.getLastLocation(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }






    @Override
    public void onSuccess(LocationEngineResult result) {
        currentLocation = result.getLastLocation();
        Log.d("Map", "onSuccess: "+currentLocation);
        if (currentLocation == null) {
            Toast.makeText(this, GPS_ERROR, Toast.LENGTH_LONG).show();
            Intent backHomeIntent = new Intent(this, MainActivity.class);
            startActivity(backHomeIntent);
            return;
        }
        if (this.mapboxMap != null) {
            this.mapboxMap.getLocationComponent().forceLocationUpdate(currentLocation);
        }
    }


    public void chiamataServerOggetti(){
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        final String url = "https://ewserver.di.unimi.it/mobicomp/mostri/getmap.php";
        Log.d("Map", "funziona");

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("session_id", Model.getInstance().getSessionID());
            Log.d("Map", "Eseguito: "+Model.getInstance().getSessionID());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest getMapRequest = new JsonObjectRequest(
                url,
                jsonRequest,


                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Model.getInstance().MapObject(response);
                        Log.d("Map", "Eseguito: " + response);

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Map", "Error: " + error.toString());
                    }
                });


        mRequestQueue.add(getMapRequest);

    }




    @Override
    public void onFailure(@NonNull Exception exception) {

    }

    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        chiamataServerOggetti();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
// Prevent leaks
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates(this);
        }
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}


