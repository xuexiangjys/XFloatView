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

package com.xuexiang.xfloatviewdemo.entity;

import android.content.pm.ApplicationInfo;

/**
 * 应用流量信息
 *
 * @author xuexiang
 * @since 2018/9/13 下午3:12
 */
public class AppTrafficInfo {

    /**
     * 应用信息
     */
    private ApplicationInfo mApplicationInfo;

    /**
     * 上传流量
     */
    private long mUpLoadBytes;
    /**
     * 下载流量
     */
    private long mDownLoadBytes;

    public AppTrafficInfo(ApplicationInfo applicationInfo, long upLoadBytes, long downLoadBytes) {
        mApplicationInfo = applicationInfo;
        mUpLoadBytes = upLoadBytes;
        mDownLoadBytes = downLoadBytes;
    }

    public ApplicationInfo getApplicationInfo() {
        return mApplicationInfo;
    }

    public AppTrafficInfo setApplicationInfo(ApplicationInfo applicationInfo) {
        mApplicationInfo = applicationInfo;
        return this;
    }


    public long getUpLoadBytes() {
        return mUpLoadBytes;
    }

    public AppTrafficInfo setUpLoadBytes(long upLoadBytes) {
        mUpLoadBytes = upLoadBytes;
        return this;
    }

    public long getDownLoadBytes() {
        return mDownLoadBytes;
    }

    public AppTrafficInfo setDownLoadBytes(long downLoadBytes) {
        mDownLoadBytes = downLoadBytes;
        return this;
    }
}
