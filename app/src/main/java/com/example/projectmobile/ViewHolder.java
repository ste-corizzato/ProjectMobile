package com.example.projectmobile;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

public class ViewHolder extends RecyclerView.ViewHolder {

    private Activity parentActivity;
    private TextView username;
    private TextView xp;
    private TextView lp;
    private ImageView img;

    public ViewHolder(View itemView, Activity parentActivity) {
        super(itemView);
        this.parentActivity = parentActivity;
        username = itemView.findViewById(R.id.username);
        xp = itemView.findViewById(R.id.xp);
        lp = itemView.findViewById(R.id.lp);
        img =itemView.findViewById(R.id.img);

    }

    public void setPlayer(Player player) {

        username.setText(player.getUsername());
        xp.setText(player.getXp());
        lp.setText(player.getLp());



            Bitmap bm = StringToBitMap(player.getImg());


            img.setImageBitmap(bm);   //MyPhoto is image control.

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

}
