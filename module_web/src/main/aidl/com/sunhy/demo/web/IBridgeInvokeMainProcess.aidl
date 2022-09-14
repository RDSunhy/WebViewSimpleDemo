// IBridgeInvokeMainProcess.aidl
package com.sunhy.demo.web;

// Declare any non-default types here with import statements
import com.sunhy.demo.web.IBridgeInvokeWebProcess;

interface IBridgeInvokeMainProcess {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void handleBridgeInvoke(String command, String params, IBridgeInvokeWebProcess bridgeCallback);
}