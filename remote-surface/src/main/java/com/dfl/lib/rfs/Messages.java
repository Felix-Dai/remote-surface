package com.dfl.lib.rfs;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceControlViewHost;

class Messages {
    static final String KEY_HOST_TOKEN = "key_host_token";
    static final String KEY_DISPLAY_ID = "key_display_id";
    static final String KEY_WIDTH = "key_width";
    static final String KEY_HEIGHT = "key_height";
    static final String KEY_SURFACE_PACKAGE = "key_surface_package";
    static final String KEY_TOUCH_EVENT = "key_touch_event";

    static final int MSG_SURFACE_DISPLAY = 1;
    static final int MSG_SURFACE_PACKAGE = 2;
    static final int MSG_TOUCH_EVENT = 1 << 8;

    static Message obtainSurfaceDisplay(IBinder hostToken, int displayId, int width, int height) {
        Message msg = Message.obtain();
        msg.what = MSG_SURFACE_DISPLAY;
        Bundle bundle = msg.getData();
        bundle.putBinder(KEY_HOST_TOKEN, hostToken);
        bundle.putInt(KEY_DISPLAY_ID, displayId);
        bundle.putInt(KEY_WIDTH, width);
        bundle.putInt(KEY_HEIGHT, height);
        return msg;
    }

    static Message obtainSurfacePackage(SurfaceControlViewHost.SurfacePackage pkg) {
        Message msg = Message.obtain();
        msg.what = MSG_SURFACE_PACKAGE;
        if (pkg != null) {
            Bundle bundle = msg.getData();
            bundle.putParcelable(KEY_SURFACE_PACKAGE, pkg);
        }
        return msg;
    }

    static Message obtainTouchEvent(MotionEvent event) {
        Message msg = Message.obtain();
        msg.what = MSG_TOUCH_EVENT;
        Bundle bundle = msg.getData();
        bundle.putParcelable(KEY_TOUCH_EVENT, event);
        return msg;
    }
}
