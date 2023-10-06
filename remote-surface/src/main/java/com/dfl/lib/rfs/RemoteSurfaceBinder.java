package com.dfl.lib.rfs;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.SurfaceControlViewHost;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

public class RemoteSurfaceBinder {
    private static final String TAG = "RemoteSurfaceBinder";
    private Context context;
    private SurfaceView surface;
    private ComponentName component;
    private Messenger handleMessenger;
    private Messenger remoteMessenger;
    private MessageConnection connection;
    private InternalHandler handler;

    private SurfaceControlViewHost.SurfacePackage surfacePackage;

    public RemoteSurfaceBinder(Context context, SurfaceView surface, ComponentName component) {
        this.context = context;
        this.surface = surface;
        this.component = component;
        handler = new InternalHandler(Looper.getMainLooper());
        handleMessenger = new Messenger(handler);
        surface.setZOrderMediaOverlay(true);
        surface.setOnTouchListener(touchL);
        surface.addOnAttachStateChangeListener(attachL);
    }

    public void bind() {
        handler.post(bindRunnable);
    }

    public void clear() {
        if (connection != null) {
            context.unbindService(connection);
            connection = null;
        }
    }

    private void sendSurfaceToken() {
        handler.post(() -> {
            Message msg = Messages.obtainSurfaceDisplay(
                    surface.getHostToken(),
                    surface.getDisplay().getDisplayId(),
                    surface.getWidth(),
                    surface.getHeight());
            msg.replyTo = handleMessenger;
            try {
                remoteMessenger.send(msg);
            } catch (RemoteException e) {
                Log.e(TAG, "Error when send surface info");
                e.printStackTrace();
            }
        });
    }

    private class MessageConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            remoteMessenger = new Messenger(service);
            if (surface.isAttachedToWindow()) {
                sendSurfaceToken();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            remoteMessenger = null;
            handler.post(() -> {
               if (surfacePackage != null) {
                   surfacePackage.release();
                   surfacePackage = null;
               }
            });
        }

        @Override
        public void onBindingDied(ComponentName name) {
            handler.removeCallbacks(bindRunnable);
            // Try to re-connect
            handler.postDelayed(bindRunnable, 1000);
        }
    }

    private class InternalHandler extends Handler {
        public InternalHandler(@NonNull Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == Messages.MSG_SURFACE_PACKAGE) {
                handleSurfacePackage(msg);
            }
        }

        private void handleSurfacePackage(Message msg) {
            Bundle data = msg.getData();
            surfacePackage = data.getParcelable(Messages.KEY_SURFACE_PACKAGE);
            surface.setChildSurfacePackage(surfacePackage);
        }
    }

    private final Runnable bindRunnable = () -> {
        context.bindService(new Intent().setComponent(component),
                new MessageConnection(), Context.BIND_AUTO_CREATE);
    };

    private final View.OnTouchListener touchL = (v, event) -> {
        if (remoteMessenger != null) {
            try {
                remoteMessenger.send(Messages.obtainTouchEvent(event));
            } catch (RemoteException e) {
                Log.e(TAG, "Error when send touch event");
                e.printStackTrace();
            }
        }
        return true;
    };

    private final View.OnAttachStateChangeListener attachL = new View.OnAttachStateChangeListener() {
        @Override
        public void onViewAttachedToWindow(@NonNull View v) {
            if (remoteMessenger != null && surfacePackage == null) {
                sendSurfaceToken();
            }
        }

        @Override
        public void onViewDetachedFromWindow(@NonNull View v) {
            if (surfacePackage != null) {
                surfacePackage.release();
                surfacePackage = null;
            }
        }
    };
}
