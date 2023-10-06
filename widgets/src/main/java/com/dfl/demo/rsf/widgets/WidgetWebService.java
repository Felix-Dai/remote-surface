package com.dfl.demo.rsf.widgets;

import android.content.Context;
import android.view.View;

import com.dfl.lib.rfs.RemoteSurfaceService;

public class WidgetWebService extends RemoteSurfaceService {
    @Override
    public View onCreateView(Context context) {
        return new WidgetWeb(context).itemView;
    }
}
