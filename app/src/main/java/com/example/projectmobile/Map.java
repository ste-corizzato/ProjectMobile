package com.example.projectmobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
// Classes needed to handle location permissions
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;

import java.util.ArrayList;
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

import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolLongClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;

import org.json.JSONException;
import org.json.JSONObject;

import static com.mapbox.mapboxsdk.style.layers.Property.ICON_ROTATION_ALIGNMENT_VIEWPORT;

public class Map extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener, LocationEngineCallback<LocationEngineResult>, OnSymbolLongClickListener{

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
    private final String MONSTER_MARKER_IMAGE_ID = "monster-image";
    private final String CANDY_MARKER_IMAGE_ID = "candy-image";
    private final String MONSTER = "MO";
    private final String CANDY= "CA";

    private ArrayList<MapObject> myMapObjectsModel = Model.getInstance().getMapObjectList();

    private boolean isLocationUpdateActive = false;
    private int idRequest =0;
    private String immagine ="";

    private Button PointDisplay;




    private SymbolManager symbolManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Mapbox.getInstance(this, "pk.eyJ1Ijoibmljb2xhc3JhZGljY2hpIiwiYSI6ImNrN3VoNWR0YzB6aGEzZnNmOWhudHMxcjgifQ.NLpLVz5Ji-0Afy3gqP5H9g");


        setContentView(R.layout.activity_map);
        PointDisplay = findViewById(R.id.PointDisplay);
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



                        Log.d("Map", "style");

                        style.addImage(MONSTER_MARKER_IMAGE_ID,getDrawable(R.drawable.dragon));
                        style.addImage(CANDY_MARKER_IMAGE_ID,getDrawable(R.drawable.candy));

                        symbolManager = new SymbolManager(mapView, mapboxMap, style);
                        symbolManager.addClickListener(new OnSymbolClickListener(){


                            public void onAnnotationClick(Symbol symbol) {
                                Log.d("Object_detail", ""+getObjectIdFromSymbol(symbol));
                                idRequest=getObjectIdFromSymbol(symbol);
                                getImageObjectRequest();
                                Intent intent2 = new Intent(getApplicationContext(), Object_detail.class);
                                intent2.putExtra("IdObject", Integer.toString(getObjectIdFromSymbol(symbol)));

                                startActivity(intent2);





                            }
                        });
                        symbolManager.setIconAllowOverlap(true);
                        symbolManager.setIconTranslate(new Float[]{-4f,5f});
                        symbolManager.setIconRotationAlignment(ICON_ROTATION_ALIGNMENT_VIEWPORT);

                        chiamataServerOggetti();




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
        isLocationUpdateActive = true;
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


    public void onNewMapObjectsAdded(MapObject mapObject) {
        Log.d("Map", "onNewObjectsAdded: "+mapObject.getIdObject()+" "+mapObject.getName()+" "+mapObject.getType()+" "+mapObject.getSize()+" "+mapObject.getLat()+" "+mapObject.getLon()+" ");

        //MOSTRA IL NUOVO OGGETTO SULLA MAPPA
        String imageId = null;
        if(mapObject.getType().equals(MONSTER)){
            imageId = MONSTER_MARKER_IMAGE_ID;
        } else if(mapObject.getType().equals(CANDY)){
            imageId = CANDY_MARKER_IMAGE_ID;
        }

        if(imageId == null){
            return;
        }

        symbolManager.create(new SymbolOptions()
                .withLatLng(new LatLng((mapObject.getLat()),mapObject.getLon()))
                .withIconImage(imageId)
                .withData(createIdJsonElement(mapObject.getIdObject()))
                .withIconSize(0.04f));
    }

    private void chiamataServerOggetti(){
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

        Log.d("oggetti", ""+ myMapObjectsModel.size());
        for(int i=0; i<myMapObjectsModel.size(); i++){
            onNewMapObjectsAdded(myMapObjectsModel.get(i));
        }


    }


    private JsonElement createIdJsonElement(int id) {
        return new Gson().fromJson("{\"id\":\""+id+"\"}", JsonElement.class);
    }


    @Override
    public void onFailure(@NonNull Exception exception) {

    }

    protected void onStart() {
        super.onStart();
        mapView.onStart();
        if(this.mapboxMap != null && mapboxMap.getStyle() != null){
            symbolManager.deleteAll();
            Log.d("Map", "onStart: map objects retrieved again");
        }
        chiamataServerOggetti();


    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d("Map", "onStop: "+locationEngine);
        if (locationEngine != null) {
            stopLocationEngine();
        }
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




    @Override
    public  void onAnnotationLongClick(Symbol symbol) {

    }

    private int getObjectIdFromSymbol(Symbol s){
        int objectId = s.getData().getAsJsonObject().get("id").getAsInt();
        Log.d("Map", "getObjectIdFromSymbol: "+objectId);
        return objectId;
    }

    private void stopLocationEngine() {
        locationEngine.removeLocationUpdates(this);
        isLocationUpdateActive = false;
    }


    private void getImageObjectRequest(){
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        final String url2 = "https://ewserver.di.unimi.it/mobicomp/mostri/getimage.php";
        Log.d("Map", "funziona richiesta immagine");

        JSONObject jsonRequest = new JSONObject();
        try {

                jsonRequest.put("session_id", Model.getInstance().getSessionID());
                jsonRequest.put("target_id", Integer.toString(idRequest));



        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest getMapRequest = new JsonObjectRequest(
                url2,
                jsonRequest,


                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Map", "Eseguito: " + response);
                        getImgResponse(response);







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

    private void getImgResponse(JSONObject response)  {

        try {
            immagine =response.getString("img");
            Model.getInstance().setImgObject(immagine);

        } catch (JSONException e) {
            e.printStackTrace();
        }




    }

    public void onClickButton(View view) {
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                .zoom(16.5)
                .build();
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1000);
    }


}


