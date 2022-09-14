package com.sunhy.demo.web.command

import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.JsonObject
import com.sunhy.demo.apt.annotations.JsBridgeCommand
import com.sunhy.demo.web.IBridgeInvokeWebProcess
import com.sunhy.demo.web.bridge.IBridgeCallbackInterface
import com.sunhy.demo.web.bridge.IBridgeCommand

@JsBridgeCommand(name = "showToast")
class ToastCommand : IBridgeCommand {
    override fun exec(params: JsonObject?, callback: IBridgeInvokeWebProcess?) {
        if (params != null && params["message"] != null) {
            ToastUtils.showShort(params["message"].asString)
            //回调 测试 返回一个 message 给 web 端
            val key = getCallbackKey(params)
            if (!key.isNullOrEmpty()) {
                val data = mapOf("message" to "showToast is success!!")
                callback?.handleBridgeCallback(key, GsonUtils.toJson(data))
            }
        }
    }
}