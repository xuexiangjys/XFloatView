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

import android.view.KeyEvent;
import android.view.View;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageContainerListFragment;
import com.xuexiang.xpage.utils.TitleBar;
import com.xuexiang.xutil.common.ClickUtils;

/**
 * @author xuexiang
 * @since 2018/9/13 上午2:30
 */
@Page(name = "XFloatView 悬浮窗")
public class MainFragment extends XPageContainerListFragment {

    @Override
    protected Class[] getPagesClasses() {
        return new Class[] {
                NetWorkMonitorFragment.class
        };
    }

    @Override
    protected TitleBar initTitleBar() {
        return super.initTitleBar().setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickUtils.exitBy2Click();
            }
        });
    }


    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ClickUtils.exitBy2Click();
        }
        return true;
    }
}
