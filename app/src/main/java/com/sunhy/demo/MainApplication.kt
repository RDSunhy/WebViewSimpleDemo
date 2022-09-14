package com.sunhy.demo

import android.content.Intent
import android.os.Build
import android.webkit.WebView
import com.blankj.utilcode.util.ProcessUtils
import com.sunhy.demo.base.BaseApplication
import com.sunhy.demo.base.utils.LoginUtils
import com.sunhy.demo.web.broadcast.WebViewInitBoastcast

class MainApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        when(ProcessUtils.getCurrentProcessName()){
            "com.sunhy.demo" -> {
                // 主进程初始化...
                initWebViewPool()
                LoginUtils.login()
            }
            "com.sunhy.demo:web" -> {
                // :web 进程程初始化...
            }
        }
    }

    private fun initWebViewPool() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val packageName = this.packageName
            val processName = ProcessUtils.getCurrentProcessName()
            if (packageName != processName) {
                WebView.setDataDirectorySuffix(packageName)
            }
        }
        // 用广播提前拉起 :web进程
        val intent = Intent(this, WebViewInitBoastcast::class.java)
        sendBroadcast(intent)
    }
}