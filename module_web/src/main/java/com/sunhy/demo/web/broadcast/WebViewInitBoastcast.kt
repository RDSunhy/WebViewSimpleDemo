package com.sunhy.demo.web.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.blankj.utilcode.util.LogUtils
import com.sunhy.demo.web.TemplateWebViewPool
import com.sunhy.demo.web.WebViewPool
import kotlin.math.min

class WebViewInitBoastcast: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        LogUtils.d("WebViewInitBoastcast", "initWebViewPool")
        initWebViewPool(context)
    }

    private fun initWebViewPool(context: Context) {
        // 根据手机 CPU 核心数（或者手机内存）设置缓存池容量
        WebViewPool.getInstance().setMaxPoolSize(min(Runtime.getRuntime().availableProcessors(), 3))
        WebViewPool.getInstance().init(context)

        // 加载本地模板用的 WebView 复用池
        TemplateWebViewPool.getInstance().setMaxPoolSize(min(Runtime.getRuntime().availableProcessors(), 3))
        TemplateWebViewPool.getInstance().init(context)
    }
}