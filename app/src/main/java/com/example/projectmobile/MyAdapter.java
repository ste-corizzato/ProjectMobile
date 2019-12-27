package com.example.projectmobile;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<ViewHolder> {

    private LayoutInflater mInflater;
    private Activity parentActivity;
    private ArrayList<Player> players;

    public MyAdapter(Context context, Activity parentActivity, ArrayList<Player> players) {
        this.mInflater = LayoutInflater.from(context);
        this.parentActivity= parentActivity;
        this.players=players;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.single_row, parent, false);
            return new ViewHolder(view, parentActivity);
        }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Player player = players.get(position);
        holder.setPlayer(player);
        }

    @Override
    public int getItemCount() {
        return Model.getInstance().getSize();
    }
}
