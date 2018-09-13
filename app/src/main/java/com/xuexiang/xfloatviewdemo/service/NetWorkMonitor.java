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

import android.content.Context;
import android.content.Intent;
import android.net.TrafficStats;

import static com.xuexiang.xfloatviewdemo.service.NetWorkMonitorService.ACTION_NETWORK_SPEED_INFO;
import static com.xuexiang.xfloatviewdemo.service.NetWorkMonitorService.KEY_DOWNLOAD_SPEED;
import static com.xuexiang.xfloatviewdemo.service.NetWorkMonitorService.KEY_UPLOAD_SPEED;

/**
 * 网络监听线程
 *
 * @author xuexiang
 * @since 2018/9/13 上午11:41
 */
public class NetWorkMonitor extends Thread {

    private Context mContext;
    /**
     * 是否在运行
     */
    private boolean mIsRunning;

    /**
     * 检测间隔（单位：秒）
     */
    private int mKeepAliveInterval = 1;

    private OnNetWorkListener mOnNetWorkListener;

    /**
     * 监听类型
     */
    private int mMonitorType;

    /**
     * 应用id
     */
    private int mUid;

    /**
     * 总的发送字节数（上行）
     */
    private long mTotalUpLoadBytes = 0;
    /**
     * 总的接收字节数（下行）
     */
    private long mTotalDownLoadBytes = 0;
    /**
     * 上行速度
     */
    private double mUpLoadSpeed = 0;
    /**
     * 下行速度
     */
    private double mDownLoadSpeed = 0;
    /**
     * 构造器
     */
    public NetWorkMonitor(Context context, int keepAliveInterval, OnNetWorkListener listener) {
        mIsRunning = true;
        mContext = context.getApplicationContext();
        mKeepAliveInterval = keepAliveInterval;
        mOnNetWorkListener = listener;
    }

    public NetWorkMonitor updateMonitorType(int monitorType, int uid) {
        mMonitorType = monitorType;
        mUid = uid;
        if (mMonitorType == NetWorkMonitorService.MONITOR_TYPE_ALL_APP) {
            mTotalUpLoadBytes = TrafficStats.getTotalTxBytes();
            mTotalDownLoadBytes = TrafficStats.getTotalRxBytes();
        } else {
            mTotalUpLoadBytes = TrafficStats.getUidTxBytes(mUid);
            mTotalDownLoadBytes = TrafficStats.getUidRxBytes(mUid);
        }
        return this;
    }

    public void run() {
        while (mIsRunning) {
            try {
                // 休息一段时间
                Thread.sleep(mKeepAliveInterval * 1000);
                doNetWorkCheck();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 进行网络状态的检测
     */
    private void doNetWorkCheck() {
        long tempTotalUpLoadBytes, tempTotalDownLoadBytes;

        if (mMonitorType == NetWorkMonitorService.MONITOR_TYPE_ALL_APP) {
            tempTotalUpLoadBytes = TrafficStats.getTotalTxBytes();
            tempTotalDownLoadBytes = TrafficStats.getTotalRxBytes();
        } else {
            tempTotalUpLoadBytes = TrafficStats.getUidTxBytes(mUid);
            tempTotalDownLoadBytes = TrafficStats.getUidRxBytes(mUid);
        }

        mUpLoadSpeed = (tempTotalUpLoadBytes - mTotalUpLoadBytes) / mKeepAliveInterval;
        mDownLoadSpeed = (tempTotalDownLoadBytes - mTotalDownLoadBytes) / mKeepAliveInterval;

        sendNetWorkInfo(mUpLoadSpeed, mDownLoadSpeed);

        mTotalUpLoadBytes = tempTotalUpLoadBytes;
        mTotalDownLoadBytes = tempTotalDownLoadBytes;
    }

    /**
     * 发送网络信息
     * @param upLoadSpeed
     * @param downLoadSpeed
     */
    private void sendNetWorkInfo(double upLoadSpeed, double downLoadSpeed) {
        if (mOnNetWorkListener != null) {
            mOnNetWorkListener.onUpdateSpeed(upLoadSpeed, downLoadSpeed);
        } else {
            Intent intent = new Intent(ACTION_NETWORK_SPEED_INFO);
            intent.putExtra(KEY_UPLOAD_SPEED, upLoadSpeed);
            intent.putExtra(KEY_DOWNLOAD_SPEED, downLoadSpeed);
            mContext.sendBroadcast(intent);
        }
    }

    /**
     * 关闭线程
     */
    public void closeThread() {
        mIsRunning = false;
        mOnNetWorkListener = null;
        interrupt();
    }

    public NetWorkMonitor setOnNetWorkListener(OnNetWorkListener onNetWorkListener) {
        mOnNetWorkListener = onNetWorkListener;
        return this;
    }

    /**
     * 网络监听
     */
    public interface OnNetWorkListener {
        /**
         * 更新速度
         * @param upLoadSpeed 上行速度
         * @param downLoadSpeed 下行速度
         */
        void onUpdateSpeed(double upLoadSpeed, double downLoadSpeed);
    }
}
