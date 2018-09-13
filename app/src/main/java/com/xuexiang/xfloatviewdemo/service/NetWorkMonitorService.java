/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xuexiang.xfloatviewdemo.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import com.xuexiang.xfloatviewdemo.widget.MonitorView;
import com.xuexiang.xutil.data.SPUtils;

/**
 * 网络状态监听服务
 *
 * @author xuexiang
 * @since 2018/9/13 上午11:39
 */
public class NetWorkMonitorService extends Service {

    public final static String ACTION_NETWORK_SPEED_INFO = "com.xuexiang.devicemonitor.ACTION_NETWORK_SPEED_INFO";

    public final static String KEY_UPLOAD_SPEED = "com.xuexiang.devicemonitor.key_upload_speed";

    public final static String KEY_DOWNLOAD_SPEED = "com.xuexiang.devicemonitor.key_download_speed";

    public final static String KEY_MONITOR_TYPE = "com.xuexiang.devicemonitor.key_monitor_type";
    public final static String KEY_APP_UID = "com.xuexiang.devicemonitor.key_app_uid";
    public final static String KEY_APP_NAME = "com.xuexiang.devicemonitor.key_app_name";

    public final static int MONITOR_TYPE_ALL_APP = 0;
    public final static int MONITOR_TYPE_SINGLE_APP = 1;

    private MonitorView mMonitorView;

    private NetWorkMonitor mNetWorkMonitor;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences sp = SPUtils.getDefaultSharedPreferences();
        int monitorType, uid;
        String appName;
        if (intent != null) {
            monitorType = intent.getIntExtra(KEY_MONITOR_TYPE, MONITOR_TYPE_ALL_APP);
            uid = intent.getIntExtra(KEY_APP_UID, 0);
            appName = intent.getStringExtra(KEY_APP_NAME);

            SPUtils.putInt(sp, KEY_MONITOR_TYPE, monitorType);
            SPUtils.putInt(sp, KEY_APP_UID, uid);
            SPUtils.putString(sp, KEY_APP_NAME, appName);
        } else {
            monitorType = SPUtils.getInt(sp, KEY_MONITOR_TYPE, MONITOR_TYPE_ALL_APP);
            uid = SPUtils.getInt(sp, KEY_APP_UID, 0);
            appName = SPUtils.getString(sp, KEY_APP_NAME, "null");
        }
        init(monitorType, uid, appName);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 开启监测
     * @param info
     */
    public static void start(Context context, ApplicationInfo info) {
        Intent intent = new Intent(context, NetWorkMonitorService.class);
        if (info == null) {
            intent.putExtra(KEY_MONITOR_TYPE, MONITOR_TYPE_ALL_APP);
        } else {
            intent.putExtra(KEY_MONITOR_TYPE, MONITOR_TYPE_SINGLE_APP);
            intent.putExtra(KEY_APP_UID, info.uid);
            intent.putExtra(KEY_APP_NAME, context.getPackageManager().getApplicationLabel(info));
        }
        context.startService(intent);
    }

    /**
     * 停止监测
     * @param context
     */
    public static void stop(Context context) {
        SharedPreferences sp = SPUtils.getDefaultSharedPreferences();
        SPUtils.clear(sp);
        context.stopService(new Intent(context, NetWorkMonitorService.class));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        super.onCreate();
        //此处为创建前台服务，但是通知栏消息为空，这样我们就可 以在不通知用户的情况下启动前台服务了
        Notification notification = new Notification.Builder(this)
                .setContentTitle("流量监测")
                .setContentText("应用正在监测手机的流量")
                .setAutoCancel(false)
                .setOngoing(true)
                .build();
        startForeground(100, notification);
    }

    private void init(int monitorType, int uid, String appName) {
        if (mMonitorView == null) {
            mMonitorView = new MonitorView(this);
            mMonitorView.show();
        }
        mMonitorView.updateMonitorAppName(appName);

        if (mNetWorkMonitor == null) {
            mNetWorkMonitor = new NetWorkMonitor(this, 1, new NetWorkMonitor.OnNetWorkListener() {
                /**
                 * 更新速度
                 *
                 * @param upLoadSpeed   上行速度
                 * @param downLoadSpeed 下行速度
                 */
                @Override
                public void onUpdateSpeed(double upLoadSpeed, double downLoadSpeed) {
                    if (mMonitorView != null) {
                        mMonitorView.updateNetWorkInfo(upLoadSpeed, downLoadSpeed);
                    }
                }
            });
            mNetWorkMonitor.updateMonitorType(monitorType, uid).start();
        } else {
            mNetWorkMonitor.updateMonitorType(monitorType, uid);
        }

    }

    @Override
    public void onDestroy() {
        if (mNetWorkMonitor != null) {
            mNetWorkMonitor.closeThread();
            mNetWorkMonitor = null;
        }
        if (mMonitorView != null) {
            mMonitorView.clear();
            mMonitorView = null;
        }
        super.onDestroy();
    }

}
