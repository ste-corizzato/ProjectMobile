package com.example.projectmobile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.Collator;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class fragment_profile extends Fragment implements View.OnClickListener {

    public RequestQueue mRequestQueue = null;
    String username_text = null;
    String img = null;
    Button modifica;
    Button cambiafoto;
    ImageView fotoprofilo;


    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    public String imgString;
    Bitmap bm = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View w= inflater.inflate(R.layout.fragment_fragment_profile, container, false);


        modifica= (Button) w.findViewById(R.id.Modifica);;
        modifica.setOnClickListener(this);

        fotoprofilo = w.findViewById(R.id.imageViewPicture);


        cambiafoto = w.findViewById(R.id.button_change_img);
        cambiafoto.setOnClickListener(this);



        return w;

    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onResume() {
        super.onResume();

        String text =Model.getInstance().getUsername();
        TextView tv = getActivity().findViewById(R.id.text_nome);
        tv.setText(text);

        ImageView i=getActivity().findViewById(R.id.imageViewPicture);
        String imgUser=Model.getInstance().getImgUser();
        Bitmap bm= StringToBitMap(imgUser);
        i.setImageBitmap(bm);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Modifica:

                Log.d("fragment_profile", "modifica");
                modificaNome();
                TextView tv = getActivity().findViewById(R.id.textName);
                tv.setText(username_text);


                ImageView image=getActivity().findViewById(R.id.imageView);

                
                if (imgString != null){
                    bm = StringToBitMap(imgString);
                    Model.getInstance().setImgUser(imgString);
                }


                if(bm==null) {
                    if(Model.getInstance().getImgUser()== null){
                        image.setImageDrawable(getResources().getDrawable(R.drawable.knight_2));
                    }else{
                        image.setImageBitmap(StringToBitMap(Model.getInstance().getImgUser()));
                    }


                }else {
                    image.setImageBitmap(bm);   //MyPhoto is image control.
                }

                Log.d("fragment_profile", "indietro");
                home newFragment2 = new home();

                FragmentTransaction transaction2 = getFragmentManager().beginTransaction();

                transaction2.replace(R.id.fragment_container, newFragment2);
                transaction2.addToBackStack(null);

                transaction2.commit();

                break;

                case R.id.button_change_img:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED) {
                        //permission not granted, request it.
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        //show popup for runtime permission
                        requestPermissions(permissions, PERMISSION_CODE);
                    }
                    else {
                        //permission already granted
                        pickImageFromGallery();
                    }
                }
                else {
                    //system on is less then marshamallow
                    pickImageFromGallery();

                }



        }

    }
    //

    private void pickImageFromGallery() {
        //intent to pick image
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    //handle result of runtime permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission was granted
                    pickImageFromGallery();
                }
                else{
                    //permission was denied
                    Toast.makeText(getActivity(),"Permesso negato", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //handle result of picked image
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            //Set image to image view
            fotoprofilo.setImageURI(data.getData());
            fotoprofilo.buildDrawingCache();
            Bitmap bmap = fotoprofilo.getDrawingCache();
            getEncoded64ImageStringFromBitmap(bmap);
            Model.getInstance().setImgUser(imgString);

        }
    }



    public void modificaNome(){
            mRequestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());
            final String url= " https://ewserver.di.unimi.it/mobicomp/mostri/setprofile.php";

            TextView tv = getView().findViewById(R.id.text_nome);
            username_text = tv.getText().toString();




            JSONObject jsonRequest = new JSONObject();
            try {

                jsonRequest.put("session_id",Model.getInstance().getSessionID());
                jsonRequest.put("username", username_text);

                if(imgString==null) {
                    jsonRequest.put("img", Model.getInstance().getImgUser());
                }else{
                    jsonRequest.put("img", imgString);
                    
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest getProfileRequest = new JsonObjectRequest(
                    url,
                    jsonRequest,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Model.getInstance().setUsername(username_text);
                            Log.d("MainActivity", "Eseguito modifica: "+response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("MainActivity", "Error: " + error.toString());
                        }});


            mRequestQueue.add(getProfileRequest);




    }

    public Bitmap StringToBitMap (String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }

    }


    public void getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

    }

}








