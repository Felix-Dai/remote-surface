package com.dfl.demo.rsf.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WidgetRecycler extends RecyclerView.ViewHolder {
    private final RecyclerView recycler;

    public WidgetRecycler(@NonNull Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.widget_recycler, null));
        recycler = itemView.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        recycler.setAdapter(new Adapter(context));
    }

    private static class Adapter extends RecyclerView.Adapter<Item> {
        private Context context;

        public Adapter(@NonNull Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Item(context, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull Item holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return 100;
        }
    }

    private static class Item extends RecyclerView.ViewHolder {
        private TextView count;

        public Item(Context context, ViewGroup parent) {
            super(LayoutInflater.from(context).inflate(R.layout.widget_recycler_item, parent, false));
            count = itemView.findViewById(R.id.count);
        }

        public void bind(int position) {
            count.setText(String.valueOf(position));
        }
    }
}
