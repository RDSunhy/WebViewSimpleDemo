package com.sunhy.demo.web.bridge

@Deprecated("已经改写为AIDL文件 IBridgeInvokeWebProcess.aidl")
interface IBridgeCallbackInterface {
    /**
     * callback 回调key
     * params   参数 json 格式
     */
    fun handleBridgeCallback(callback: String, params: String)
}