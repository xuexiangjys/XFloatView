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
import android.widget.ImageView;

import com.xuexiang.xfloatview.XFloatView;
import com.xuexiang.xfloatviewdemo.R;
import com.xuexiang.xfloatviewdemo.util.SystemKeyboard;
import com.xuexiang.xutil.display.DensityUtils;
import com.xuexiang.xutil.display.ScreenUtils;

/**
 * 悬浮控制盘
 *
 * @author xuexiang
 * @since 2018/9/13 下午2:22
 */
public class SmartPanel extends XFloatView implements OnClickListener {

    private ImageView mClose;
    private ImageView mMore;

    private ImageView mBack;
    private ImageView mHome;
    private ImageView mRecent;

    private ImageView mMenu;
    private ImageView mVoiceUp;
    private ImageView mVoiceDown;

    private boolean isFirstPage = true;

    private onCloseListener mListener;

    public SmartPanel(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_smartpanel;
    }

    @Override
    protected boolean canMoveOrTouch() {
        return false;
    }

    @Override
    public void initFloatView() {
        mClose = findViewById(R.id.ic_close);
        mMore = findViewById(R.id.ic_more);

        mBack = findViewById(R.id.ic_back);
        mHome = findViewById(R.id.ic_home);
        mRecent = findViewById(R.id.ic_recent);

        mMenu = findViewById(R.id.ic_menu);
        mVoiceUp = findViewById(R.id.ic_voiceup);
        mVoiceDown = findViewById(R.id.ic_voicedown);

        initFloatViewPosition(ScreenUtils.getScreenWidth() - DensityUtils.dip2px(55), (ScreenUtils.getScreenHeight() - getFloatRootView().getMeasuredHeight()) / 2 - getStatusBarHeight());
    }

    @Override
    protected void initListener() {
        mClose.setOnClickListener(this);
        mMore.setOnClickListener(this);

        mBack.setOnClickListener(this);
        mHome.setOnClickListener(this);
        mRecent.setOnClickListener(this);

        mMenu.setOnClickListener(this);
        mVoiceUp.setOnClickListener(this);
        mVoiceDown.setOnClickListener(this);
    }

    @Override
    protected boolean isAdsorbView() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_close:
                if (mListener != null) {
                    mListener.onClose();
                }
                break;
            case R.id.ic_more:
                if (isFirstPage) {
                    mHome.setVisibility(View.GONE);
                    mBack.setVisibility(View.GONE);
                    mRecent.setVisibility(View.GONE);

                    mMenu.setVisibility(View.VISIBLE);
                    mVoiceUp.setVisibility(View.VISIBLE);
                    mVoiceDown.setVisibility(View.VISIBLE);
                    isFirstPage = false;
                } else {
                    mHome.setVisibility(View.VISIBLE);
                    mBack.setVisibility(View.VISIBLE);
                    mRecent.setVisibility(View.VISIBLE);

                    mMenu.setVisibility(View.GONE);
                    mVoiceUp.setVisibility(View.GONE);
                    mVoiceDown.setVisibility(View.GONE);
                    isFirstPage = true;
                }
                break;
            case R.id.ic_back:
                SystemKeyboard.toBack();
                break;
            case R.id.ic_home:
                SystemKeyboard.toHome(getContext());
                break;
            case R.id.ic_recent:
                SystemKeyboard.toRecent();
                break;
            case R.id.ic_menu:
                SystemKeyboard.toMenu();
                break;
            case R.id.ic_voiceup:
                SystemKeyboard.volumeAdjustment(getContext(), false);
                break;
            case R.id.ic_voicedown:
                SystemKeyboard.volumeAdjustment(getContext(), true);
                break;

            default:
                break;
        }
    }

    public void setOnCloseListener(onCloseListener listener) {
        mListener = listener;
    }

    /**
     * 关闭的监听
     */
    public interface onCloseListener {
        void onClose();
    }

}
