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

package com.xuexiang.xfloatviewdemo.widget.smart;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 按钮控制盘控件
 *
 * @author xuexiang
 * @since 2018/9/13 下午2:26
 */
public class SmartView {
    /**
     * 悬浮图标
     */
    private SmartIcon mSmartIcon;
    /**
     * 悬浮控制盘
     */
    private SmartPanel mSmartPanel;
    private Context mContext;

    public SmartView(Context context) {
        mContext = context;
        initView();
    }

    public void initView() {
        mSmartIcon = new SmartIcon(mContext);
        mSmartPanel = new SmartPanel(mContext);
        mSmartIcon.setOnFloatViewClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mSmartPanel.show();
                mSmartIcon.dismiss();
            }
        });
        mSmartPanel.setOnCloseListener(new SmartPanel.onCloseListener() {
            @Override
            public void onClose() {
                mSmartPanel.dismiss();
                mSmartIcon.show();
            }
        });
        mSmartIcon.show();
    }

    public void refreshSmartView() {
        if (mSmartIcon != null) {
            mSmartIcon.clear();
        }
        if (mSmartPanel != null) {
            mSmartPanel.clear();
        }
        initView();
    }

    public void destroy() {
        if (mSmartIcon != null) {
            mSmartIcon.clear();
            mSmartIcon = null;
        }
        if (mSmartPanel != null) {
            mSmartPanel.clear();
            mSmartPanel = null;
        }

    }
}
