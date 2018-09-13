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

package com.xuexiang.xfloatviewdemo.fragment;

import android.Manifest;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xuexiang.xfloatviewdemo.R;
import com.xuexiang.xfloatviewdemo.adapter.AppTrafficAdapter;
import com.xuexiang.xfloatviewdemo.adapter.BaseRecyclerAdapter;
import com.xuexiang.xfloatviewdemo.entity.AppTrafficInfo;
import com.xuexiang.xfloatviewdemo.service.NetWorkMonitorService;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xutil.app.AppUtils;
import com.xuexiang.xutil.app.PackageUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2018/9/13 下午3:14
 */
@Page(name = "应用流量详情列表")
public class AppTrafficFragment extends XPageFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private AppTrafficAdapter mAppTrafficAdapter;

    private List<AppTrafficInfo> mAppInfos = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_app_traffic;
    }

    @Override
    protected void initViews() {
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAppTrafficAdapter = new AppTrafficAdapter(getContext(), mAppInfos);
        mRecyclerView.setAdapter(mAppTrafficAdapter);

        updateAppTrafficInfo();
    }

    @Override
    protected void initListeners() {
        mAppTrafficAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                ApplicationInfo applicationInfo = mAppTrafficAdapter.getItem(pos).getApplicationInfo();
                NetWorkMonitorService.start(getContext(), applicationInfo);
                PackageUtils.openApp(getContext(), applicationInfo.packageName);
            }
        });
    }

    private void updateAppTrafficInfo() {
        PackageManager pm = AppUtils.getPackageManager();
        List<PackageInfo> packageInfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_PERMISSIONS);
        for (PackageInfo info : packageInfos) {
            String[] permissions = info.requestedPermissions;
            if (permissions != null && permissions.length > 0) {
                for (String permission : permissions) {
                    if (Manifest.permission.INTERNET.equals(permission)) {
                        int uid = info.applicationInfo.uid;
                        long upLoadBytes = TrafficStats.getUidTxBytes(uid);
                        long downLoadBytes = TrafficStats.getUidRxBytes(uid);

                        mAppInfos.add(new AppTrafficInfo(info.applicationInfo, upLoadBytes, downLoadBytes));

//                        /** 获取手机通过 2G/3G 接收的字节流量总数 */
//                        TrafficStats.getMobileRxBytes();
//                        /** 获取手机通过 2G/3G 接收的数据包总数 */
//                        TrafficStats.getMobileRxPackets();
//                        /** 获取手机通过 2G/3G 发出的字节流量总数 */
//                        TrafficStats.getMobileTxBytes();
//                        /** 获取手机通过 2G/3G 发出的数据包总数 */
//                        TrafficStats.getMobileTxPackets();
//                        /** 获取手机通过所有网络方式接收的字节流量总数(包括 wifi) */
//                        TrafficStats.getTotalRxBytes();
//                        /** 获取手机通过所有网络方式接收的数据包总数(包括 wifi) */
//                        TrafficStats.getTotalRxPackets();
//                        /** 获取手机通过所有网络方式发送的字节流量总数(包括 wifi) */
//                        TrafficStats.getTotalTxBytes();
//                        /** 获取手机通过所有网络方式发送的数据包总数(包括 wifi) */
//                        TrafficStats.getTotalTxPackets();
//                        /** 获取手机指定 UID 对应的应程序用通过所有网络方式接收的字节流量总数(包括 wifi) */
//                        TrafficStats.getUidRxBytes(uid);
//                        /** 获取手机指定 UID 对应的应用程序通过所有网络方式发送的字节流量总数(包括 wifi) */
//                        TrafficStats.getUidTxBytes(uid);
                    }
                }
            }
        }
        mAppTrafficAdapter.notifyDataSetChanged();
    }


}
