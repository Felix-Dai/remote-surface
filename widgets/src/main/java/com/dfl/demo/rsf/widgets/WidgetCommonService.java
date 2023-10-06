package com.dfl.demo.rsf.widgets;

import android.content.Context;
import android.view.View;

import com.dfl.lib.rfs.RemoteSurfaceService;

public class WidgetCommonService extends RemoteSurfaceService {
    @Override
    public View onCreateView(Context context) {
        return new WidgetCommon(context).itemView;
    }
}
