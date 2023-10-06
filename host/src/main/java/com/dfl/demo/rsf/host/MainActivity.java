package com.dfl.demo.rsf.host;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.ViewGroup;

import com.dfl.lib.rfs.RemoteSurfaceBinder;

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
                    return new Item(context, parent, new ComponentName("com.dfl.demo.rsf.widgets", "com.dfl.demo.rsf.widgets.WidgetCommonService"));
                case 1:
                    return new Item(context, parent, new ComponentName("com.dfl.demo.rsf.widgets", "com.dfl.demo.rsf.widgets.WidgetRecyclerService"));
                case 2:
                    return new Item(context, parent, new ComponentName("com.dfl.demo.rsf.widgets", "com.dfl.demo.rsf.widgets.WidgetWebService"));
            }
            throw new RuntimeException("Unsupported widget type: " + viewType);
        }
    }

    private static class Item extends RecyclerView.ViewHolder {

        public Item(@NonNull Context context, ViewGroup parent, @NonNull ComponentName component) {
            super(LayoutInflater.from(context).inflate(R.layout.activity_main_item, parent, false));
            SurfaceView surface = itemView.findViewById(R.id.surface);
            RemoteSurfaceBinder binder = new RemoteSurfaceBinder(context, surface, component);
            binder.bind();
        }
    }
}