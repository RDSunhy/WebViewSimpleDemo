package com.sunhy.demo.web.utils

import android.content.Context
import android.graphics.Color
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import com.sunhy.demo.web.R
import java.io.File

object WebUtil {

    /**
     * 获取 WebView 缓存文件目录
     */
    fun getWebViewCachePath(context: Context): String{
        return context.filesDir.absolutePath + "/webCache"
    }

    fun defaultSettings(context: Context, webView: WebView) {
        // 白色背景
        webView.setBackgroundColor(Color.TRANSPARENT)
        webView.setBackgroundResource(R.color.white)

        // 如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可
        webView.settings.javaScriptEnabled = true // 允许加载js
        webView.overScrollMode = View.OVER_SCROLL_NEVER
        webView.isNestedScrollingEnabled = false // 默认支持嵌套滑动

        // 设置自适应屏幕，两者合用
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true
        // 是否支持缩放，默认为true
        webView.settings.setSupportZoom(false)
        // 是否使用内置的缩放控件
        webView.settings.builtInZoomControls = false
        // 是否显示原生的缩放控件
        webView.settings.displayZoomControls = false
        // 设置文本缩放 默认 100
        webView.settings.textZoom = 100
        // 是否保存密码
        webView.settings.savePassword = false
        // 是否可以访问文件
        webView.settings.allowFileAccess = true
        // 是否支持通过js打开新窗口
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        // 是否支持自动加载图片
        webView.settings.loadsImagesAutomatically = true
        webView.settings.blockNetworkImage = false
        // 设置编码格式
        webView.settings.defaultTextEncodingName = "utf-8"
        webView.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
        // 是否启用 DOM storage API
        webView.settings.domStorageEnabled = true
        // 是否启用 database storage API 功能
        webView.settings.databaseEnabled = true
        // 配置当安全源试图从不安全源加载资源时WebView的行为
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        // 设置缓存模式
        webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
        // 开启 Application Caches 功能
        webView.settings.setAppCacheEnabled(true)
        // 设置 Application Caches 缓存目录
        val cachePath = getWebViewCachePath(context)
        val cacheDir = File(cachePath)
        // 设置缓存目录
        if (!cacheDir.exists() && !cacheDir.isDirectory) {
            cacheDir.mkdirs()
        }
        webView.settings.setAppCachePath(cachePath)
    }
}