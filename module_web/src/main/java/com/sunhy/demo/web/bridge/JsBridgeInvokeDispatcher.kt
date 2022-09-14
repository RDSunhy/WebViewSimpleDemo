package com.sunhy.demo.web.bridge

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ProcessUtils
import com.google.gson.JsonObject
import com.sunhy.demo.base.BaseApplication
import com.sunhy.demo.web.BaseWebView
import com.sunhy.demo.web.IBridgeInvokeMainProcess
import com.sunhy.demo.web.IBridgeInvokeWebProcess
import com.sunhy.demo.web.service.BridgeCommandService

class JsBridgeInvokeDispatcher : ServiceConnection{
    companion object {
        private const val TAG = "JsBridgeInvokeDispatcher"

        @Volatile
        private var sInstance: JsBridgeInvokeDispatcher? = null

        fun getInstance(): JsBridgeInvokeDispatcher {
            if (sInstance == null) {
                synchronized(this) {
                    if (sInstance == null) {
                        sInstance = JsBridgeInvokeDispatcher()
                    }
                }
            }
            return sInstance!!
        }
    }

    private var iBridgeInvokeMainProcess: IBridgeInvokeMainProcess? = null

    fun bindService() {
        LogUtils.d(TAG, "bindService()")
        if (iBridgeInvokeMainProcess == null) {
            val i = Intent(BaseApplication.getInstance(), BridgeCommandService::class.java)
            BaseApplication.getInstance().bindService(i, this, Context.BIND_AUTO_CREATE)
        }
    }

    fun unbindService() {
        LogUtils.d(TAG, "unbindService()")
        iBridgeInvokeMainProcess = null
        BaseApplication.getInstance().unbindService(this)
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        iBridgeInvokeMainProcess = IBridgeInvokeMainProcess.Stub.asInterface(service)
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        iBridgeInvokeMainProcess = null
    }

    fun sendCommand(view: BaseWebView, message: JsBridgeMessage?) {
        LogUtils.d(TAG, "sendCommand()", "message: $message")
        if (checkMessage(message)) {
            // 校验命令通过后 执行命令
            excuteCommand(view, message)
        }
    }

    // 校验命令、参数 合法性
    private fun checkMessage(message: JsBridgeMessage?): Boolean {
        if (message == null) {
            LogUtils.e(TAG, "send()", "jsBridgeParam is null")
            return false
        }
        if (message.command.isNullOrEmpty()) {
            LogUtils.e(TAG, "send()", "jsBridgeParam.commend is null")
            return false
        }
        return true
    }

    //执行命令
    private fun excuteCommand(view: BaseWebView, message: JsBridgeMessage?) {
        val callback = object : IBridgeInvokeWebProcess.Stub() {
            override fun handleBridgeCallback(callback: String, params: String) {
                LogUtils.e(TAG, "当前进程: ${ProcessUtils.getCurrentProcessName()}")
                view.postBridgeCallback(callback, params)
            }
        }
//        BridgeCommandHandler.getInstance()
//            .handleBridgeInvoke(message?.command, parseParams(message?.params), callback)
        if (iBridgeInvokeMainProcess != null){
            iBridgeInvokeMainProcess?.handleBridgeInvoke(message?.command, parseParams(message?.params), callback)
        }else{
            LogUtils.e(TAG, "excuteCommand()", "iBridgeInvokeMainProcess is null")
        }
    }

    private fun parseParams(params: JsonObject?): String {
        if (params == null) {
            return ""
        }

        return GsonUtils.toJson(params)
    }
}