package com.sunhy.demo.web

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup

class TemplateWebView(context: Context, attrs: AttributeSet? = null) : BaseWebView(context, attrs) {

    override fun release() {
        evaluateJavascript("javascript:clearData()") {}
        (parent as ViewGroup?)?.removeView(this)
        removeAllViews()
    }

    override fun onDestroy() {
        TemplateWebViewPool.getInstance().recycle(this)
    }
}