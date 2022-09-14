package com.sunhy.demo.web.bridge

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

/**
 * 桥接通信数据格式
 */
data class JsBridgeMessage(
    @SerializedName("command")
    val command: String?, // 命令
    @SerializedName("params")
    val params: JsonObject?, // 参数
)