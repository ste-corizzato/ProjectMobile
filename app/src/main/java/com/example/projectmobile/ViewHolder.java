package com.example.projectmobile;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    private Activity parentActivity;
    private TextView username;
    private TextView xp;
    private TextView lp;

    public ViewHolder(View itemView, Activity parentActivity) {
        super(itemView);
        this.parentActivity = parentActivity;
        username = itemView.findViewById(R.id.username);
        xp = itemView.findViewById(R.id.xp);
        lp = itemView.findViewById(R.id.lp);

    }

    public void setPlayer(Player player) {

        username.setText(player.getUsername());
        xp.setText(player.getXp());
        lp.setText(player.getLp());
    }

}
