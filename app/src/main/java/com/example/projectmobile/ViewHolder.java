package com.example.projectmobile;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    private TextView myTextView;
    public ViewHolder(View itemView) {
        super(itemView);
        myTextView = itemView.findViewById(R.id.textView);
    }
    public void setText(Player text) {
        myTextView.setText(text);
    }
}
