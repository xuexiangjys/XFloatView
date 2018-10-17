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
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;

import com.xuexiang.xfloatviewdemo.widget.smart.SmartView;

import java.util.Objects;

/**
 * 按钮控制盘的监听服务
 *
 * @author xuexiang
 * @since 2018/9/13 下午2:31
 */
public class SmartViewService extends Service {

    private SmartView mSmartView;

    private static boolean isRunning = false;

    public static final String CHANNEL_ID = "SmartView";

    /**
     * 开启
     *
     * @param context
     */
    public static void start(Context context) {
        if (!SmartViewService.isRunning()) {
            Intent intent = new Intent(context, SmartViewService.class);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                context.startForegroundService(intent);
            } else {
                context.startService(intent);
            }
        }
    }

    /**
     * 关闭
     *
     * @param context
     */
    public static void stop(Context context) {
        if (SmartViewService.isRunning()) {
            context.stopService(new Intent(context, SmartViewService.class));
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    /**
     * 判断服务是否运行
     */
    public static boolean isRunning() {
        return isRunning;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onCreate() {
        super.onCreate();
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void init() {
        if (mSmartView == null) {
            mSmartView = new SmartView(this);
        }
        isRunning = true;
        //此处为创建前台服务，但是通知栏消息为空，这样我们就可 以在不通知用户的情况下启动前台服务了
        Notification.Builder builder;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "按钮救星", NotificationManager.IMPORTANCE_HIGH);
            Objects.requireNonNull(manager).createNotificationChannel(channel);
            builder = new Notification.Builder(this, CHANNEL_ID);
        } else {
            builder = new Notification.Builder(this);
        }
        startForeground(101, builder.setContentTitle("按钮救星")
                .setContentText("按钮救星正在运行...")
                .setAutoCancel(false)
                .setOngoing(true)
                .build());
    }

    public void onDestroy() {
        if (mSmartView != null) {
            mSmartView.destroy();
            mSmartView = null;
        }
        isRunning = false;
        super.onDestroy();
    }

    /**
     * 屏幕旋转时调用此方法
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mSmartView != null) {
            mSmartView.refreshSmartView();
        }
    }

}
