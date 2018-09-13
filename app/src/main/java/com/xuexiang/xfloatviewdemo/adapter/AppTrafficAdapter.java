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

package com.xuexiang.xfloatviewdemo.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.format.Formatter;

import com.xuexiang.xfloatviewdemo.R;
import com.xuexiang.xfloatviewdemo.entity.AppTrafficInfo;

import java.util.List;

/**
 * 流量信息适配器
 *
 * @author xuexiang
 * @since 2018/9/13 下午3:12
 */
public class AppTrafficAdapter extends BaseRecyclerAdapter<AppTrafficInfo> {
    private PackageManager mPackageManager;

    public AppTrafficAdapter(Context context, List<AppTrafficInfo> list) {
        super(context, list);
        mPackageManager = context.getPackageManager();
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.adapter_recycler_app_traffic_item;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, AppTrafficInfo item) {
        holder.getImageView(R.id.iv_app_launcher).setImageDrawable(item.getApplicationInfo().loadIcon(mPackageManager));
        holder.getTextView(R.id.tv_app_name).setText(mPackageManager.getApplicationLabel(item.getApplicationInfo()));
        holder.getTextView(R.id.tv_app_download).setText(String.format("下载：%s", Formatter.formatFileSize(getContext(), item.getDownLoadBytes())));
        holder.getTextView(R.id.tv_app_upload).setText(String.format("上传：%s", Formatter.formatFileSize(getContext(), item.getUpLoadBytes())));
    }
}
