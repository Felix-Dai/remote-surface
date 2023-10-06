package com.dfl.demo.rsf.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class WidgetCommon extends RecyclerView.ViewHolder {

    private TextView titleView;
    private ImageView imageView;
    private TextView countView;
    private int count;

    public WidgetCommon(@Nullable Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.widget_common, null));
        titleView = itemView.findViewById(R.id.title);
        imageView = itemView.findViewById(R.id.image);
        countView = itemView.findViewById(R.id.count);
        imageView.setOnClickListener(v -> {
            count++;
            countView.setText(String.valueOf(count));
        });
    }
}
