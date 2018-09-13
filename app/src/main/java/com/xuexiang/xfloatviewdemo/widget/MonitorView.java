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

package com.xuexiang.xfloatviewdemo.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import com.xuexiang.xfloatview.XFloatView;
import com.xuexiang.xfloatviewdemo.R;
import com.xuexiang.xutil.app.PackageUtils;

import java.text.DecimalFormat;

/**
 * 监控悬浮控件
 *
 * @author xuexiang
 * @since 2018/9/13 上午11:35
 */
public class MonitorView extends XFloatView {

    private TextView mTvAppName;

    private TextView mTvUpLoadInfo;

    private TextView mTvDownLoadInfo;

    private DecimalFormat mSpeedFormat = new DecimalFormat("0.00");

    private Handler mMainHandler = new Handler(Looper.getMainLooper());
    /**
     * 构造器
     *
     * @param context
     */
    public MonitorView(Context context) {
        super(context);
    }

    /**
     * 获取根布局的ID
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.layout_monitor;
    }

    @Override
    protected boolean canMoveOrTouch() {
        return true;
    }

    /**
     * 初始化悬浮控件
     */
    @Override
    protected void initFloatView() {
        mTvAppName = findViewById(R.id.tv_app_name);
        mTvUpLoadInfo = findViewById(R.id.tv_upload_speed_info);
        mTvDownLoadInfo = findViewById(R.id.tv_download_speed_info);
    }

    /**
     * 初始化监听
     */
    @Override
    protected void initListener() {
        setOnFloatViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageUtils.openApp(getContext());
            }
        });
    }

    @Override
    protected boolean isAdsorbView() {
        return true;
    }

    /**
     * 更新监测应用名
     * @param appName
     */
    public void updateMonitorAppName(final String appName) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            mTvAppName.setText(String.format("应用：%s", appName));
        } else {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    mTvAppName.setText(String.format("应用：%s", appName));
                }
            });
        }
    }
    /**
     * 更新网络速度
     * @param upLoadSpeed 上行速度
     * @param downLoadSpeed 下行速度
     */
    public void updateNetWorkInfo(final double upLoadSpeed, final double downLoadSpeed) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            showSpeedInfo(upLoadSpeed, downLoadSpeed);
        } else {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    showSpeedInfo(upLoadSpeed, downLoadSpeed);
                }
            });
        }
    }

    /**
     * 显示网络速度
     * @param upLoadSpeed
     * @param downLoadSpeed
     */
    private void showSpeedInfo(double upLoadSpeed, double downLoadSpeed) {
        mTvUpLoadInfo.setText(String.format("上传：%skb/s", mSpeedFormat.format(upLoadSpeed / 1024D)));
        mTvDownLoadInfo.setText(String.format("下载：%skb/s", mSpeedFormat.format(downLoadSpeed / 1024D)));
    }


    @Override
    public void clear() {
        super.clear();
        mMainHandler.removeCallbacksAndMessages(null);
    }
}
