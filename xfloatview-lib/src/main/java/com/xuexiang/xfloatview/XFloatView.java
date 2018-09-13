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

package com.xuexiang.xfloatview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

/**
 * 悬浮窗基类,实现悬浮窗只需继承该类即可
 * <p>
 * <p>需添加权限
 * {@code <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />}</p>
 *
 * @author xuexiang
 * @since 2018/9/13 上午2:19
 */
public abstract class XFloatView implements OnTouchListener {

    private Context mContext;
    /**
     * 悬浮窗的布局参数
     */
    private LayoutParams mWmParams;
    /**
     * 创建浮动窗口设置布局参数的对象
     */
    private WindowManager mWindowManager;
    /**
     * 浮动窗口的父布局
     */
    private View mFloatRootView;
    /**
     * 系统状态栏的高度
     */
    private int mStatusBarHeight;

    private Location mLocation;
    private OnClickListener mOnClickListener;
    private OnFloatViewMoveListener mOnFloatViewMoveListener;
    /**
     * 吸附旋转的控件
     */
    private ImageView mRotateView;
    private Bitmap mBitmap;
    /**
     * 悬浮窗口是否显示
     */
    private boolean mIsShow;

    /**
     * 构造器
     */
    public XFloatView(Context context) {
        init(context);

        initFloatRootView(getLayoutId());

        initFloatView();

        initFloatViewPosition();

        initListener();
    }

    /**
     * @return 获取根布局的ID
     */
    protected abstract int getLayoutId();

    /**
     * @return 能否移动或者触摸响应
     */
    protected abstract boolean canMoveOrTouch();

    /**
     * 初始化悬浮控件
     */
    protected abstract void initFloatView();

    /**
     * 初始化监听
     */
    protected abstract void initListener();

    /**
     * @return 设置悬浮框是否吸附在屏幕边缘
     */
    protected abstract boolean isAdsorbView();

    /**
     * 初始化悬浮窗Window
     *
     * @param context
     */
    private void init(Context context) {
        mContext = context;
        initFloatViewLayoutParams();

        setWindowManagerParams(0, 0, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    /**
     * 初始化悬浮窗的LayoutParams
     */
    private void initFloatViewLayoutParams() {
        // 获取WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mWmParams = getFloatViewLayoutParams();
        mStatusBarHeight = Utils.getStatusBarHeight();
    }

    /**
     * 设置悬浮窗的LayoutParams（可重写）
     *
     * @return 悬浮窗的LayoutParams
     */
    protected LayoutParams getFloatViewLayoutParams() {
        LayoutParams params = new LayoutParams();
        // 设置window type
        params.type = LayoutParams.TYPE_PHONE;
        // 设置图片格式，效果为背景透明
        params.format = PixelFormat.RGBA_8888;
        // 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        params.flags =
                // LayoutParams.FLAG_NOT_TOUCH_MODAL |
                LayoutParams.FLAG_NOT_FOCUSABLE
        // LayoutParams.FLAG_NOT_TOUCHABLE
        ;
        // 调整悬浮窗显示的停靠位置为左侧置顶
        params.gravity = Gravity.LEFT | Gravity.TOP;
        return params;
    }

    /**
     * 设置悬浮框的初始位置、尺寸参数
     *
     * @param PosX
     * @param PosY
     * @param width
     * @param height
     */
    public void setWindowManagerParams(int PosX, int PosY, int width, int height) {
        mWmParams.x = PosX;
        mWmParams.y = PosY;
        // 设置悬浮窗口长宽数据
        mWmParams.width = width;
        mWmParams.height = height;
    }

    /**
     * 初始化父布局
     *
     * @param layoutId 布局的资源ID（最好是LinearLayout)
     * @return
     */
    private View initFloatRootView(int layoutId) {
        //获取浮动窗口视图所在布局
        mFloatRootView = LayoutInflater.from(mContext).inflate(layoutId, null);
        mFloatRootView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        if (canMoveOrTouch()) {
            mFloatRootView.setOnTouchListener(this);
        }
        return mFloatRootView;
    }

    /**
     * 设置悬浮框的初始位置【屏幕最右边，中间的位置】
     */
    public void initFloatViewPosition() {
        int x = Utils.getScreenWidth(getContext());
        int y = (Utils.getScreenHeight(getContext()) - mFloatRootView.getMeasuredHeight()) / 2 - mStatusBarHeight;
        initFloatViewPosition(x, y);
    }

    /**
     * 设置悬浮框的初始位置
     */
    public void initFloatViewPosition(int PosX, int PosY) {
        mWmParams.x = PosX;
        mWmParams.y = PosY;
        mLocation = new Location();
    }

    /**
     * 更新悬浮框的位置参数
     *
     * @param PosX
     * @param PosY
     */
    public void updateViewPosition(int PosX, int PosY) {
        mWmParams.x = PosX;
        mWmParams.y = PosY;
        mWindowManager.updateViewLayout(mFloatRootView, mWmParams);
    }

    //==========================show/dismiss=============================//

    /**
     * 显示悬浮框
     */
    public void show() {
        if (mFloatRootView != null && mWmParams != null && !mIsShow) {
            mWindowManager.addView(mFloatRootView, mWmParams);
            mIsShow = true;
        }
    }

    /**
     * 隐藏悬浮框
     */
    public void dismiss() {
        if (mFloatRootView != null && mIsShow) {
            mWindowManager.removeView(mFloatRootView);
            mIsShow = false;
        }
    }

    /**
     * 销毁悬浮框
     */
    public void clear() {
        dismiss();
        if (mRotateView != null) {
            mRotateView = null;
            if (mBitmap != null) {
                mBitmap.recycle();
                mBitmap = null;
            }
        }
        if (mFloatRootView != null) {
            mFloatRootView = null;
            mWmParams = null;
            mWindowManager = null;
            mLocation = null;
        }
    }

    //==========================api=============================//

    /**
     * 设置悬浮窗的点击监听
     *
     * @param onClickListener
     * @return
     */
    public XFloatView setOnFloatViewClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
        return this;
    }

    /**
     * 设置悬浮窗的移动监听
     *
     * @param onFloatViewMoveListener
     * @return
     */
    public XFloatView setOnFloatViewMoveListener(OnFloatViewMoveListener onFloatViewMoveListener) {
        mOnFloatViewMoveListener = onFloatViewMoveListener;
        return this;
    }

    /**
     * 设置需要旋转的ImageView控件
     *
     * @param rotateView
     * @param resId      旋转图片资源的id
     */
    public void setRotateView(ImageView rotateView, int resId) {
        mRotateView = rotateView;
        mBitmap = Utils.drawable2Bitmap(mContext.getResources().getDrawable(resId));
        mRotateView.setImageBitmap(mBitmap);
    }

    //=======================触摸事件===========================//

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            // 手指按下时记录必要的数据,纵坐标的值都减去状态栏的高度
            case MotionEvent.ACTION_DOWN:
                // 获取相对与小悬浮窗的坐标
                mLocation.mXInView = event.getX();
                mLocation.mYInView = event.getY();
                // 按下时的坐标位置，只记录一次
                mLocation.mXDownInScreen = event.getRawX();
                mLocation.mYDownInScreen = event.getRawY() - mStatusBarHeight;
                break;
            case MotionEvent.ACTION_MOVE:
                // 时时的更新当前手指在屏幕上的位置
                mLocation.mXInScreen = event.getRawX();
                mLocation.mYInScreen = event.getRawY() - mStatusBarHeight;
                // 手指移动的时候更新小悬浮窗的位置
                if (mOnFloatViewMoveListener != null) {
                    mOnFloatViewMoveListener.onMove(mLocation);
                } else {
                    updateViewPosition((int) (mLocation.mXInScreen - mLocation.mXInView), (int) (mLocation.mYInScreen - mLocation.mYInView));
                }
                break;
            case MotionEvent.ACTION_UP:
                // 如果手指离开屏幕时，按下坐标与当前坐标相等，则视为触发了单击事件
                if (Math.abs(mLocation.getXDownInScreen() - event.getRawX()) < 10 && Math.abs(mLocation.getYDownInScreen() - (event.getRawY() - mStatusBarHeight)) < 10) {
                    if (mOnClickListener != null) {
                        mOnClickListener.onClick(v);
                    }
                } else {
                    if (isAdsorbView()) {
                        updateGravity(event);
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 获取控件的位置类型
     *
     * @param event
     * @return
     */
    private PositionType getPositionType(MotionEvent event) {
        PositionType type;
        int height = Utils.getScreenHeight(getContext()) / 5;
        int width = Utils.getScreenWidth(getContext()) / 2;
        if (event.getRawY() < height) {
            type = PositionType.TOP;
        } else if (event.getRawY() > (height * 4)) {
            type = PositionType.BOTTOM;
        } else {
            if (event.getRawX() > width) {
                type = PositionType.RIGHT;
            } else {
                type = PositionType.LEFT;
            }
        }
        return type;
    }

    /**
     * 更新重心
     *
     * @param event
     */
    private void updateGravity(MotionEvent event) {
        PositionType type = getPositionType(event);
        switch (type) {
            case TOP:
                updateRotateView(-90);
                updateViewPosition((int) (event.getRawX() - event.getX()), 0);
                break;
            case BOTTOM:
                updateRotateView(90);
                updateViewPosition((int) (event.getRawX() - event.getX()), Utils.getScreenHeight(getContext()));
                break;
            case RIGHT:
                updateRotateView(0);
                updateViewPosition(Utils.getScreenWidth(getContext()), (int) (event.getRawY() - event.getY()) - mStatusBarHeight);
                break;
            case LEFT:
                updateRotateView(180);
                updateViewPosition(0, (int) (event.getRawY() - event.getY()) - mStatusBarHeight);
                break;
            default:
                break;
        }
    }

    /**
     * 旋转悬浮框图标
     *
     * @param degree 角度
     */
    private void updateRotateView(int degree) {
        if (mRotateView != null) {
            if (degree != 0) {
                mRotateView.setImageBitmap(Utils.rotate(mBitmap, degree));
            } else {
                mRotateView.setImageBitmap(mBitmap);
            }
        }
    }

    //==========================get=============================//

    public Context getContext() {
        return mContext;
    }

    public <T extends View> T findViewById(int resId) {
        return mFloatRootView != null ? (T) mFloatRootView.findViewById(resId) : null;
    }

    public View getFloatRootView() {
        return mFloatRootView;
    }

    public LayoutParams getWmParams() {
        return mWmParams;
    }

    public void setWmParams(LayoutParams wmParams) {
        this.mWmParams = wmParams;
    }

    public WindowManager getWindowManager() {
        return mWindowManager;
    }

    public int getStatusBarHeight() {
        return mStatusBarHeight;
    }

    //============================================================//

    /**
     * 悬浮框移动监听
     *
     * @author xx
     */
    public interface OnFloatViewMoveListener {
        /**
         * 移动
         *
         * @param location 位置信息
         */
        void onMove(Location location);
    }

    /**
     * 控件位置类型
     *
     * @author xx
     */
    public enum PositionType {
        LEFT, RIGHT, TOP, BOTTOM
    }

    /**
     * 悬浮控件的位置信息
     *
     * @author xuexiang
     * @since 2018/9/13 上午2:22
     */
    public final class Location {
        /**
         * 记录当前手指位置在屏幕上的横坐标
         */
        private float mXInScreen;
        /**
         * 记录当前手指位置在屏幕上的纵坐标
         */
        private float mYInScreen;
        /**
         * 记录手指按下时在屏幕上的横坐标,用来判断单击事件
         */
        private float mXDownInScreen;
        /**
         * 记录手指按下时在屏幕上的纵坐标,用来判断单击事件
         */
        private float mYDownInScreen;
        /**
         * 记录手指按下时在小悬浮窗的View上的横坐标
         */
        private float mXInView;
        /**
         * 记录手指按下时在小悬浮窗的View上的纵坐标
         */
        private float mYInView;

        public float getXInScreen() {
            return mXInScreen;
        }

        public void setXInScreen(float xInScreen) {
            mXInScreen = xInScreen;
        }

        public float getYInScreen() {
            return mYInScreen;
        }

        public void setYInScreen(float yInScreen) {
            mYInScreen = yInScreen;
        }

        public float getXDownInScreen() {
            return mXDownInScreen;
        }

        public void setXDownInScreen(float xDownInScreen) {
            mXDownInScreen = xDownInScreen;
        }

        public float getYDownInScreen() {
            return mYDownInScreen;
        }

        public void setYDownInScreen(float yDownInScreen) {
            mYDownInScreen = yDownInScreen;
        }

        public float getXInView() {
            return mXInView;
        }

        public void setXInView(float xInView) {
            mXInView = xInView;
        }

        public float getYInView() {
            return mYInView;
        }

        public void setYInView(float yInView) {
            mYInView = yInView;
        }
    }

}
