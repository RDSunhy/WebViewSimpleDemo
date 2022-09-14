package com.sunhy.demo.web.bridge

import android.os.Handler
import android.os.Looper
import android.util.ArrayMap
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ProcessUtils
import com.google.gson.JsonObject
import com.sunhy.demo.apt.JsBridgeUtil
import com.sunhy.demo.web.IBridgeInvokeMainProcess
import com.sunhy.demo.web.IBridgeInvokeWebProcess

class BridgeCommandHandler: IBridgeInvokeMainProcess.Stub() {

    companion object {
        private const val TAG = "BridgeCommandHandler"

        @Volatile
        private var sInstance: BridgeCommandHandler? = null

        fun getInstance(): BridgeCommandHandler {
            if (sInstance == null) {
                synchronized(this) {
                    if (sInstance == null) {
                        LogUtils.e(
                            "BridgeCommandHandler",
                            "初始化 当前进程：${ProcessUtils.getCurrentProcessName()}"
                        )
                        sInstance = BridgeCommandHandler()
                    }
                }
            }
            return sInstance!!
        }
    }

    // 用于切线程
    private val mHandle = Handler(Looper.getMainLooper())

    // 命令注册 暂时用 map 手动添加 后续修改
//    private val mCommandMap by lazy {
//        val map = ArrayMap<String, IBridgeCommand>().apply {
//            put("showToast", ToastCommand())
//        }
//        return@lazy map
//    }
    private val mCommandMap: ArrayMap<String, IBridgeCommand> by lazy { JsBridgeUtil.autoRegist() }

    // 暴露给外部方法 分发调用
    override fun handleBridgeInvoke(command: String?, params: String?, bridgeCallback: IBridgeInvokeWebProcess?) {
        LogUtils.e(TAG, "handleBridgeInvoke()", "当前进程：${ProcessUtils.getCurrentProcessName()}")
        // map 中存在命令 则执行
        if (mCommandMap.contains(command)) {
            mHandle.post { // 切换到主线程 获取命令 执行
                mCommandMap[command]!!.exec(parseParams(params), bridgeCallback)
            }
        } else {
            LogUtils.e(TAG, "handleBridgeInvoke()", "command[${command}] is not register!")
        }
    }

    private fun parseParams(params: String?): JsonObject? {
        if (params.isNullOrEmpty()) {
            return null
        }
        return GsonUtils.fromJson(params, JsonObject::class.java)
    }
}