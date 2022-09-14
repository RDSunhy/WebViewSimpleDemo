package com.sunhy.demo.web

import android.content.Context
import android.content.MutableContextWrapper
import android.util.Log
import com.sunhy.demo.web.core.BaseWebChromeClient
import com.sunhy.demo.web.core.BaseWebViewClient
import java.util.*

class TemplateWebViewPool private constructor() {

    companion object {

        private const val TAG = "TemplateWebViewPool"

        @Volatile
        private var instance: TemplateWebViewPool? = null

        fun getInstance(): TemplateWebViewPool {
            return instance ?: synchronized(this) {
                instance ?: TemplateWebViewPool().also { instance = it }
            }
        }
    }

    private val sPool = Stack<TemplateWebView>()
    private val lock = byteArrayOf()
    private var maxSize = 1

    /**
     * 设置 webview 池容量
     */
    fun setMaxPoolSize(size: Int) {
        synchronized(lock) { maxSize = size }
    }

    /**
     * 初始化webview 放在list中
     */
    fun init(context: Context, initSize: Int = maxSize) {
        for (i in 0 until initSize) {
            val view = TemplateWebView(MutableContextWrapper(context))
            // 初始化时就加载模板
            view.loadUrl("file:///android_asset/template_news.html")
            sPool.push(view)
        }
    }

    /**
     * 获取webview
     */
    fun getWebView(context: Context): TemplateWebView {
        synchronized(lock) {
            val webView: TemplateWebView
            if (sPool.size > 0) {
                webView = sPool.pop()
                Log.d(TAG, "getWebView from pool")
            } else {
                webView = TemplateWebView(MutableContextWrapper(context))
                // 初始化时就加载模板
                webView.loadUrl("file:///android_asset/template_news.html")
                Log.d(TAG, "getWebView from create")
            }

            val contextWrapper = webView.context as MutableContextWrapper
            contextWrapper.baseContext = context

            // 默认设置
            webView.webChromeClient = BaseWebChromeClient()
            webView.webViewClient = BaseWebViewClient()
            return webView
        }
    }

    /**
     * 回收 WebView
     */
    fun recycle(webView: TemplateWebView) {
        // 释放资源
        webView.release()

        // 根据池容量判断是否销毁 【也可以增加其他条件 如手机低内存等等】
        val contextWrapper = webView.context as MutableContextWrapper
        contextWrapper.baseContext = webView.context.applicationContext
        synchronized(lock) {
            if (sPool.size < maxSize) {
                sPool.push(webView)
            } else {
                webView.destroy()
            }
        }
    }
}