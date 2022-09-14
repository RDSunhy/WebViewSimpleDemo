package com.sunhy.demo.base

import android.app.Application
import android.util.Log
import kotlin.properties.Delegates

open class BaseApplication: Application() {

    companion object {
        private var mApplication: BaseApplication by Delegates.notNull()

        fun getInstance(): BaseApplication{
            Log.e("BaseApplication", getProcessName())
            return mApplication
        }
    }

    override fun onCreate() {
        super.onCreate()
        mApplication = this
    }

    fun getPN(): String = getProcessName()
}