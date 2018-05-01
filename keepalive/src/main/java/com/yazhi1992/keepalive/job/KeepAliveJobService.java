package com.yazhi1992.keepalive.job;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/1/29 0029.
 */

@SuppressLint("NewApi")
public class KeepAliveJobService extends JobService {

    public static void StartJob(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context
                .JOB_SCHEDULER_SERVICE);
        JobInfo.Builder builder = new JobInfo.Builder(10, new ComponentName(context
                .getPackageName(), KeepAliveJobService.class
                .getName()))
                .setPersisted(true); //在设备重启依然执行
        //小于7.0
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            // 每隔1s 执行一次 job
            builder.setPeriodic(1_000);
        } else {
            //延迟执行任务
            builder.setMinimumLatency(1_000);
        }
        jobScheduler.schedule(builder.build());
    }

    private static final String TAG = "zyz";

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e(TAG, "开启job");
//        Toast.makeText(getBaseContext(), "start", Toast.LENGTH_SHORT).show();
        //如果7.0以上 轮训
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StartJob(this);
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
