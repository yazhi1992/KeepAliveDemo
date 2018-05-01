package com.yazhi1992.keepalive;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

/**
 * Created by zengyazhi on 2018/4/29.
 */

public class RemoteService extends Service {

    private MyBinder binder;
    private ServiceConnection conn;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("zyz", "remote service start");
        Intent intents = new Intent();
        intents.setClass(this, LocalService.class);
        bindService(intents, conn, Context.BIND_IMPORTANT);
        return START_STICKY;
    }

    private void init() {
        if (conn == null) {
            conn = new MyConnection();
        }
        binder = new MyBinder();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    static class MyBinder extends IKeepAlive.Stub {
        @Override
        public String getName() throws RemoteException {
            return "远程连接";
        }
    }

    class MyConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
        }

        @Override
        public void onServiceDisconnected(ComponentName nme) {
            RemoteService.this.startService(new Intent(RemoteService.this,
                    LocalService.class));
            RemoteService.this.bindService(new Intent(RemoteService.this,
                    LocalService.class), conn, Context.BIND_IMPORTANT);
        }
    }
}
