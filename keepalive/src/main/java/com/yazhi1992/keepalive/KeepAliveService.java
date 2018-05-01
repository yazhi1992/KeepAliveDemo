package com.yazhi1992.keepalive;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.yazhi1992.keepalive.job.KeepAliveJobService;

import java.util.List;

/**
 * Created by zengyazhi on 2018/4/29.
 */

@SuppressLint("NewApi")
public class KeepAliveService extends JobService {

    private JobScheduler mJobScheduler;

    public static void StartJob(Context context) {
        JobScheduler systemService = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            systemService = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

            JobInfo build = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // 同时需要设置 jobFinished needsRescheduled = true

                JobInfo.Builder builder = new JobInfo.Builder(1,
                        new ComponentName(context.getPackageName(), KeepAliveService.class.getName()));
                builder.setMinimumLatency(1_000);

//                builder.setMinimumLatency(15 * 60 * 60);
//                builder.setPeriodic(15*60*60); //每隔5秒运行一次
                builder.setRequiresCharging(true);
                builder.setPersisted(true);  //设置设备重启后，是否重新执行任务
                builder.setRequiresDeviceIdle(true); //android8 导致无法开启任务

                build = builder.build();
            } else {
                JobInfo.Builder builder = new JobInfo.Builder(1,
                        new ComponentName(context.getPackageName(), KeepAliveService.class.getName()));

//                builder.setMinimumLatency(15 * 60 * 60);
                builder.setPeriodic(5000); //每隔5秒运行一次
                builder.setRequiresCharging(true);
                builder.setPersisted(true);  //设置设备重启后，是否重新执行任务
                builder.setRequiresDeviceIdle(true);

                build = builder.build();
            }

            Log.e("zyz", "KeepAliveService onStartCommand");

            if (systemService.schedule(build) <= 0) {
                //If something goes wrong
                Log.e("zyz", "KeepAliveService schedule wrong");
            } else {
                Log.e("zyz", "KeepAliveService schedule suc");
            }
        }
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e("zyz", "keep alive start");
        Toast.makeText(getBaseContext(), "start", Toast.LENGTH_SHORT).show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StartJob(this);
        }

//        jobFinished(params, true);
        return false; //true 表示系统执行的是耗时任务，当给定的任务完成时，需要手动调用jobFinished来停止该任务。
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.e("zyz", "keep alive stop");
        if (!isServiceRunning(this, "com.yazhi1992.moon.keepalive.LocalService") || !isServiceRunning(this, "com.yazhi1992.moon.keepalive.RemoteService")) {
            startService(new Intent(this, LocalService.class));
            startService(new Intent(this, RemoteService.class));
        }
        return false;
    }

    // 服务是否运行
    public boolean isServiceRunning(Context context, String serviceName) {
        boolean isRunning = false;
        ActivityManager am = (ActivityManager) this
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();


        for (ActivityManager.RunningAppProcessInfo info : lists) {// 获取运行服务再启动
            System.out.println(info.processName);
            if (info.processName.equals(serviceName)) {
                isRunning = true;
            }
        }
        return isRunning;
    }
}
