package com.example.projectmobile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


public class home extends Fragment implements View.OnClickListener {

    Button play;
    Button classifica;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View w= inflater.inflate(R.layout.fragment_home, container, false);


        play = w.findViewById(R.id.button_map);
        play.setOnClickListener((this));

        classifica = w.findViewById(R.id.button_leaderboards);
        classifica.setOnClickListener(this);

        return w;

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){

            case R.id.button_leaderboards:
                Intent intent =new Intent(getActivity(),Leaderboards.class);
                startActivity(intent);

                break;

            case R.id.button_map:
                Intent intent2 =new Intent(getActivity(),Map.class);
                startActivity(intent2);
        }
    }
}
