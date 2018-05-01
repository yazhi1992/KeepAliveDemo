package com.yazhi1992.keepalive;

import android.content.Context;

import com.yazhi1992.keepalive.job.KeepAliveJobService;

/**
 * Created by zengyazhi on 2018/5/1.
 */

public class KeepAliveManager {

    private KeepAliveManager() {
    }

    private static class KeepAliveManagerHolder {
        private static KeepAliveManager INSTANCE = new KeepAliveManager();
    }

    public static KeepAliveManager getInstance() {
        return KeepAliveManagerHolder.INSTANCE;
    }

    public void keepAlive(Context context) {
        KeepAliveJobService.StartJob(context);
    }
}
