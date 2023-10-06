package com.dfl.demo.rsf.widgets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recycler.setAdapter(new WidgetAdapter(this));
    }

    private static class WidgetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final Context context;

        private WidgetAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getItemCount() {
            return 3;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {}

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            switch (viewType) {
                case 0:
                    return new Item(context, parent, new WidgetCommon(context));
                case 1:
                    return new Item(context, parent, new WidgetRecycler(context));
                case 2:
                    return new Item(context, parent, new WidgetWeb(context));
            }
            throw new RuntimeException("Unsupported widget type: " + viewType);
        }
    }

    private static class Item extends RecyclerView.ViewHolder {

        public Item(@NonNull Context context, ViewGroup parent, @NonNull RecyclerView.ViewHolder holder) {
            super(LayoutInflater.from(context).inflate(R.layout.activity_main_item, parent, false));
            ((ViewGroup) itemView.findViewById(R.id.container)).addView(holder.itemView);
        }
    }
}