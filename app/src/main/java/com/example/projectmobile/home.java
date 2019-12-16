package com.example.projectmobile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;


public class home extends Fragment {

    ImageButton button_setting;
    Button play;
    Button classifica;
    
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View w= inflater.inflate(R.layout.fragment_home, container, false);

        button_setting = w.findViewById(R.id.button_imp);
        button_setting.setOnClickListener((View.OnClickListener) this);


        play = w.findViewById(R.id.button_map);
        classifica = w.findViewById(R.id.button_leaderboards);

        return w;

    }


}