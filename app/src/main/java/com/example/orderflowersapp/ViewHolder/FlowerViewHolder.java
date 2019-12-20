package com.example.orderflowersapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orderflowersapp.Interface.ItemClickListener;
import com.example.orderflowersapp.R;

public class FlowerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView flower_name;
    public ImageView flower_image;

    private ItemClickListener itemClickListener;

    public FlowerViewHolder(@NonNull View itemView) {
        super(itemView);

        flower_name = itemView.findViewById(R.id.flower_name);
        flower_image = itemView.findViewById(R.id.flower_image);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.OnClick(view, getAdapterPosition(),false);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
