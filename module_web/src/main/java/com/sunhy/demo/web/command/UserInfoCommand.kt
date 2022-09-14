package com.sunhy.demo.web.command

import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ProcessUtils
import com.google.gson.JsonObject
import com.sunhy.demo.apt.annotations.JsBridgeCommand
import com.sunhy.demo.base.utils.LoginUtils
import com.sunhy.demo.web.IBridgeInvokeWebProcess
import com.sunhy.demo.web.bridge.IBridgeCommand

@JsBridgeCommand(name = "getUserInfo")
class UserInfoCommand : IBridgeCommand{

    override fun exec(params: JsonObject?, callback: IBridgeInvokeWebProcess?) {
        val userInfoJson = LoginUtils.getUserInfo()
        LogUtils.e("UserInfoCommand", "当前进程: ${ProcessUtils.getCurrentProcessName()}", userInfoJson)
        val key = getCallbackKey(params)
        if (!key.isNullOrEmpty()) {
            callback?.handleBridgeCallback(key, userInfoJson)
        }
    }
}