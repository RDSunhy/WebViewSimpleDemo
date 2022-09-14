package com.sunhy.demo.web.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.sunhy.demo.web.bridge.BridgeCommandHandler

class BridgeCommandService: Service() {
    override fun onBind(intent: Intent?): IBinder {
        return BridgeCommandHandler.getInstance()
    }
}