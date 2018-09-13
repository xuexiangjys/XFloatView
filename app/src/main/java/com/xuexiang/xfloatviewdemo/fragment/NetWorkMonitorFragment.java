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

import com.xuexiang.xfloatviewdemo.service.NetWorkMonitorService;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageSimpleListFragment;

import java.util.List;

/**
 * @author xuexiang
 * @since 2018/9/13 上午11:28
 */
@Page(name = "网速监测服务")
public class NetWorkMonitorFragment extends XPageSimpleListFragment {

    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("服务启动");
        lists.add("服务关闭");
        return lists;
    }

    @Override
    protected void onItemClick(int position) {
        switch(position) {
            case 0:
                NetWorkMonitorService.start(getContext(), null);
                break;
            case 1:
                NetWorkMonitorService.stop(getContext());
                break;
            default:
                break;
        }
    }
}
