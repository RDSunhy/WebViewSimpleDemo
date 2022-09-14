package com.sunhy.demo.web.bridge

import com.google.gson.JsonObject
import com.sunhy.demo.web.IBridgeInvokeWebProcess

interface IBridgeCommand {
    fun exec(params: JsonObject?, callback: IBridgeInvokeWebProcess?)

    fun getCallbackKey(params: JsonObject?): String? {
        if (params == null) {
            return null
        }
        if (params["bridgeCallback"] == null) {
            return null
        }
        return params["bridgeCallback"].asString
    }
}