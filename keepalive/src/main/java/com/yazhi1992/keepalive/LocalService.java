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

public class LocalService extends Service {

    private ServiceConnection conn;
    private MyService myService;

    @Override
    public IBinder onBind(Intent intent) {
        return myService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();

    }

    private void init() {
        if (conn == null) {
            conn = new MyServiceConnection();
        }
        myService = new MyService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("zyz", "local service start");
        Intent intents = new Intent();
        intents.setClass(this, RemoteService.class);
        bindService(intents, conn, Context.BIND_IMPORTANT);
        return START_STICKY;
    }

    class MyService extends IKeepAlive.Stub{
        @Override
        public String getName() throws RemoteException {
            return null;
        }
    }

    class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LocalService.this.startService(new Intent(LocalService.this,
                    RemoteService.class));
            LocalService.this.bindService(new Intent(LocalService.this,
                    RemoteService.class), conn, Context.BIND_IMPORTANT);

        }

    }
}
