# XFloatView
[![xfv][xfvsvg]][xfv]  [![api][apisvg]][api]

一个简易的悬浮窗实现方案

## 关于我

[![github](https://img.shields.io/badge/GitHub-xuexiangjys-blue.svg)](https://github.com/xuexiangjys)   [![csdn](https://img.shields.io/badge/CSDN-xuexiangjys-green.svg)](http://blog.csdn.net/xuexiangjys)

## 特征

* 支持自定义布局的悬浮窗。

* 支持自定义拖动事件、点击事件。

* 支持悬浮窗自动吸附效果。

* 支持初始化悬浮窗的位置。

* 支持悬浮窗翻转吸附。

--------

## 1、演示（请star支持）

### 1.1、Demo演示动画

![][demo-gif]

### 1.2、Demo下载

[![downloads][download-svg]][download-url]

![][download-img]

## 2、如何使用

目前支持主流开发工具AndroidStudio的使用，直接配置build.gradle，增加依赖即可.

### 2.1、Android Studio导入方法，添加Gradle依赖

1.先在项目根目录的 build.gradle 的 repositories 添加:

```
allprojects {
     repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

2.然后在dependencies添加:

```
dependencies {
  ...
  implementation 'com.github.xuexiangjys:XFloatView:1.0.1'
}
```

### 2.2、继承XFloatView,实现自定义窗体

主要需要实现如下抽象方法：

```
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
```

[点击查看示例代码](https://github.com/xuexiangjys/XFloatView/tree/master/app/src/main/java/com/xuexiang/xfloatviewdemo/widget)

### 2.3、悬浮窗的权限申请

```
FloatWindowPermission.getInstance().applyFloatWindowPermission(getContext());
```

## 联系方式

[![](https://img.shields.io/badge/点击一键加入QQ交流群-602082750-blue.svg)](http://shang.qq.com/wpa/qunwpa?idkey=9922861ef85c19f1575aecea0e8680f60d9386080a97ed310c971ae074998887)

![](https://github.com/xuexiangjys/XPage/blob/master/img/qq_group.jpg)


[xfvsvg]: https://img.shields.io/badge/XFloatView-v1.0.1-brightgreen.svg
[xfv]: https://github.com/xuexiangjys/XFloatView
[apisvg]: https://img.shields.io/badge/API-14+-brightgreen.svg
[api]: https://android-arsenal.com/api?level=14


[demo-gif]: https://github.com/xuexiangjys/XFloatView/blob/master/img/demo.gif
[download-svg]: https://img.shields.io/badge/downloads-1.5M-blue.svg
[download-url]: https://github.com/xuexiangjys/XFloatView/blob/master/apk/xfloatview_demo_1.0.apk?raw=true
[download-img]: https://github.com/xuexiangjys/XFloatView/blob/master/img/download.png