package com.sunhy.demo.web

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.google.gson.JsonSyntaxException
import com.sunhy.demo.web.bridge.JsBridgeInvokeDispatcher
import com.sunhy.demo.web.bridge.JsBridgeMessage
import com.sunhy.demo.web.core.BaseWebChromeClient
import com.sunhy.demo.web.core.BaseWebViewClient
import com.sunhy.demo.web.utils.WebUtil

open class BaseWebView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : WebView(context, attrs), LifecycleEventObserver {

    private val TAG = "BaseWebView"

    init {
        // WebView 调试模式开关
        setWebContentsDebuggingEnabled(true)
        // 不显示滚动条
        isVerticalScrollBarEnabled = false
        isHorizontalScrollBarEnabled = false
        // 初始化设置
        WebUtil.defaultSettings(context, this)

        JsBridgeInvokeDispatcher.getInstance().bindService()
        addJavascriptInterface(this, "bridge")
    }

    /**
     * 桥接
     */
    @JavascriptInterface
    fun sendCommand(json: String?) {
        LogUtils.d(TAG, "sendCommand()", "json: $json")
        if (json.isNullOrEmpty()) {
            LogUtils.e(TAG, "sendCommand json is null or empty")
            return
        }
        try {
            val message = GsonUtils.fromJson(json, JsBridgeMessage::class.java)
            JsBridgeInvokeDispatcher.getInstance().sendCommand(this, message)
        } catch (e: JsonSyntaxException) {
            LogUtils.e(TAG, "sendCommand() catch. json: $json", e.message)
            e.printStackTrace()
        }
    }

    /**
     * 桥接回调
     */
    fun postBridgeCallback(key: String?, data: String?) {
        post {
            evaluateJavascript("javascript:window.jsBridge.postBridgeCallback(`$key`, `$data`)") {}
        }
    }

    @JavascriptInterface
    fun showToast(message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * 获取当前url
     */
    override fun getUrl(): String? {
        return super.getOriginalUrl() ?: return super.getUrl()
    }

    override fun canGoBack(): Boolean {
        val backForwardList = copyBackForwardList()
        val currentIndex = backForwardList.currentIndex - 1
        if (currentIndex >= 0) {
            val item = backForwardList.getItemAtIndex(currentIndex)
            if (item?.url == "about:blank") {
                return false
            }
        }
        return super.canGoBack()
    }

    /**
     * 设置 WebView 生命管控（自动回调生命周期方法）
     */
    fun setLifecycleOwner(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }

    /**
     * 生命周期回调
     */
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {

        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                Log.d(TAG, "onStateChanged -> ON_RESUME")
                onResume()
            }
            Lifecycle.Event.ON_STOP -> {
                Log.d(TAG, "onStateChanged -> ON_STOP")
                onPause()
            }
            Lifecycle.Event.ON_DESTROY -> {
                Log.d(TAG, "onStateChanged -> ON_DESTROY")
                source.lifecycle.removeObserver(this)
                onDestroy()
            }
        }
    }

    /**
     * 生命周期 onResume()
     */
    @SuppressLint("SetJavaScriptEnabled")
    override fun onResume() {
        super.onResume()
        settings.javaScriptEnabled = true
    }

    /**
     * 生命周期 onPause()
     */
    override fun onPause() {
        super.onPause()
    }

    /**
     * 生命周期 onDestroy()
     * 父类没有 需要自己写
     */
    open fun onDestroy() {
        settings.javaScriptEnabled = false
        WebViewPool.getInstance().recycle(this)
    }

    /**
     * 释放资源操作
     */
    open fun release() {
        (parent as ViewGroup?)?.removeView(this)
        removeAllViews()
        stopLoading()
        setCustomWebViewClient(null)
        setCustomWebChromeClient(null)
        loadUrl("about:blank")
        clearHistory()
    }

    fun setCustomWebViewClient(client: BaseWebViewClient?) {
        if (client == null) {
            super.setWebViewClient(WebViewClient())
        } else {
            super.setWebViewClient(client)
        }
    }

    fun setCustomWebChromeClient(client: BaseWebChromeClient?) {
        super.setWebChromeClient(client)
    }

    interface BlankMonitorCallback {
        fun onBlank()
    }

    private var mBlankMonitorCallback: BlankMonitorCallback? = null

    fun setBlankMonitorCallback(callback: BlankMonitorCallback){
        this.mBlankMonitorCallback = callback
    }

    private val mBlankMonitorRunnable by lazy { BlankMonitorRunnable() }

    /**
     * 调用后
     * 5s 后开始执行白屏检测任务 时间可以适当修改
     */
    fun postBlankMonitorRunnable() {
        Log.d(TAG, "白屏检测任务 5s 后执行")
        removeCallbacks(mBlankMonitorRunnable)
        postDelayed(mBlankMonitorRunnable, 5000)
    }

    /**
     * 取消白屏检测任务
     */
    fun removeBlankMonitorRunnable() {
        Log.d(TAG, "白屏检测任务取消执行")
        removeCallbacks(mBlankMonitorRunnable)
    }

    inner class BlankMonitorRunnable : Runnable {

        private val TAG = "BlankMonitorRunnable"

        override fun run() {
            val task = Thread {
                // 根据宽高的 1/6 创建 bitmap
                val dstWidth = measuredWidth / 6
                val dstHeight = measuredHeight / 6
                val snapshot = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ARGB_8888)
                // 绘制 view 到 bitmap
                val canvas = Canvas(snapshot)
                draw(canvas)

                // 像素点总数
                val pixelCount = (snapshot.width * snapshot.height).toFloat()
                var whitePixelCount = 0 // 白色像素点计数
                var otherPixelCount = 0 // 其他颜色像素点计数
                // 遍历 bitmap 像素点
                for (x in 0 until snapshot.width) {
                    for (y in 0 until snapshot.height) {
                        // 计数 其实记录一种就可以
                        if (snapshot.getPixel(x, y) == -1) {
                            whitePixelCount++
                        }else{
                            otherPixelCount++
                        }
                    }
                }

                Log.d(TAG, "白屏检测任务：像素点总数为 $pixelCount")
                Log.d(TAG, "白屏检测任务：白色像素点计数为 $whitePixelCount")
                Log.d(TAG, "白屏检测任务：其他颜色像素点计数为 $otherPixelCount")

                // 回收 bitmap
                snapshot.recycle()

                if (whitePixelCount == 0) {
                    return@Thread
                }

                // 计算白色像素点占比 （计算其他颜色也一样）
                val percentage: Float = whitePixelCount / pixelCount * 100
                // 如果超过阈值 触发白屏提醒
                if (percentage > 95) {
                    post {
                        mBlankMonitorCallback?.onBlank()
                    }
                }
            }
            task.start()
        }
    }
}