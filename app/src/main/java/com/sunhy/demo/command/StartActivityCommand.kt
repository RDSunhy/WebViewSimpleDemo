package com.sunhy.demo.command

import android.content.Intent
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ProcessUtils
import com.google.gson.JsonObject
import com.sunhy.demo.activity.NewsListActivity
import com.sunhy.demo.apt.annotations.JsBridgeCommand
import com.sunhy.demo.base.BaseApplication
import com.sunhy.demo.web.IBridgeInvokeWebProcess
import com.sunhy.demo.web.bridge.IBridgeCommand

@JsBridgeCommand(name = "startActivity")
class StartActivityCommand : IBridgeCommand {
    override fun exec(params: JsonObject?, callback: IBridgeInvokeWebProcess?) {
        LogUtils.e("StartActivityCommand", "当前执行进程：${ProcessUtils.getCurrentProcessName()}")
        if (params != null && params["pageName"] != null) {
            when (params["pageName"].asString) {
                "TestActivity" -> {
                    val intent = Intent(BaseApplication.getInstance(), NewsListActivity::class.java)
                    intent.putExtra("data", "我是一个好人")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    BaseApplication.getInstance().startActivity(intent)

                    val key = getCallbackKey(params)
                    if (!key.isNullOrEmpty()) {
                        val data = mapOf("message" to "startActivity success!!")
                        callback?.handleBridgeCallback(key, GsonUtils.toJson(data))
                    }
                }
            }
        }
    }
}