package com.example.projectmobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<ViewHolder> {

    private LayoutInflater mInflater;

    public MyAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.single_row, parent, false);
            return new ViewHolder(view);
        }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Player text = Model.getInstance().get(position);
        holder.setText(text);
        }

    @Override
    public int getItemCount() {
        return Model.getInstance().getSize();
    }
}
