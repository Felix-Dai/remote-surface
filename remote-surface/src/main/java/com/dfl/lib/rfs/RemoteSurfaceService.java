package com.dfl.lib.rfs;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceControlViewHost;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class RemoteSurfaceService extends Service {
    private static final String TAG = "RemoteSurfaceWidget";
    private Messenger messenger;
    private View view;
    private SurfaceControlViewHost viewHost;

    public abstract View onCreateView(Context context);

    @Override
    public void onCreate() {
        messenger = new Messenger(new InternalHandler(Looper.getMainLooper()));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (viewHost != null) {
            viewHost.release();
            viewHost = null;
        }
        return super.onUnbind(intent);
    }

    private class InternalHandler extends Handler {
        public InternalHandler(@NonNull Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == Messages.MSG_SURFACE_DISPLAY) {
                handleSurfaceDisplay(msg);
            } else if (msg.what == Messages.MSG_TOUCH_EVENT) {
                handleTouchEvent(msg);
            }
        }

        private void handleSurfaceDisplay(Message msg) {
            if (viewHost != null) {
                viewHost.release();
            }
            if (view == null) {
                view = onCreateView(RemoteSurfaceService.this);
            }
            Bundle bundle = msg.getData();
            IBinder hostToken = bundle.getBinder(Messages.KEY_HOST_TOKEN);
            int displayId = bundle.getInt(Messages.KEY_HOST_TOKEN);
            int width = bundle.getInt(Messages.KEY_WIDTH);
            int height = bundle.getInt(Messages.KEY_HEIGHT);
            Display display = getSystemService(DisplayManager.class).getDisplay(displayId);
            viewHost = new SurfaceControlViewHost(RemoteSurfaceService.this, display, hostToken);
            viewHost.setView(view, width, height);
            SurfaceControlViewHost.SurfacePackage pkg = viewHost.getSurfacePackage();
            Messenger sender = msg.replyTo;
            if (sender != null) {
                try {
                    msg.replyTo.send(Messages.obtainSurfacePackage(pkg));
                } catch (RemoteException e) {
                    Log.e(TAG, "Error when reply surface package");
                    e.printStackTrace();
                }
            }
        }

        private void handleTouchEvent(Message msg) {
            if (viewHost == null) {
                return;
            }
            Bundle bundle = msg.getData();
            MotionEvent event = bundle.getParcelable(Messages.KEY_TOUCH_EVENT);
            view.dispatchTouchEvent(event);
        }
    }
}
