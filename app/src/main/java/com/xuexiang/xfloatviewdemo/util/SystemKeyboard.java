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

package com.xuexiang.xfloatviewdemo.util;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.view.KeyEvent;

import com.xuexiang.xutil.common.ShellUtils;
import com.xuexiang.xutil.system.AppExecutors;

/**
 * 系统快捷键
 *
 * @author xuexiang
 * @since 2018/9/13 下午2:21
 */
public class SystemKeyboard {

	/**
	 * 回到主界面
	 * 
	 * @param context
	 */
	public static void toHome(Context context) {
		Intent i = new Intent();
		i.setAction(Intent.ACTION_MAIN);
		i.addCategory(Intent.CATEGORY_HOME);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);

	}

	/**
	 * 返回按钮
	 */
	public static void toBack() {
		AppExecutors.get().poolIO().execute(new Runnable() {
			@Override
			public void run() {
				sendKeyevent(KeyEvent.KEYCODE_BACK);
			}
		});
	}

	/**
	 * 发送按钮事件
	 * 
	 * @param eventCode
	 */
	public static void sendKeyevent(int eventCode) {
		ShellUtils.execCommand("input keyevent " + eventCode, true, false);
	}

	/**
	 * 菜单按钮
	 */
	public static void toMenu() {
		AppExecutors.get().poolIO().execute(new Runnable() {
			@Override
			public void run() {
				sendKeyevent(KeyEvent.KEYCODE_MENU);
			}
		});
	}

	/**
	 * 最近运行应用列表
	 */
	public static void toRecent() {
		AppExecutors.get().poolIO().execute(new Runnable() {
			@Override
			public void run() {
				sendKeyevent(KeyEvent.KEYCODE_APP_SWITCH);
			}
		});
	}

	/**
	 * 音量调节
	 * 
	 * @param context
	 * @param isAdjustLower
	 *            是否是调低音量
	 */
	public static void volumeAdjustment(Context context, boolean isAdjustLower) {
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		if (audioManager == null) return;
		if (isAdjustLower) { // 降低音量，调出系统音量控制
			audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FX_FOCUS_NAVIGATION_UP);
		} else { // 增加音量，调出系统音量控制
			audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FX_FOCUS_NAVIGATION_UP);
		}
	}

}
