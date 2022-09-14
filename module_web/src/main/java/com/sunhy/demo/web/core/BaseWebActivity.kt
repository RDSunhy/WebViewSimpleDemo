package com.sunhy.demo.web.core

import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.sunhy.demo.web.WebViewPool

abstract class BaseWebActivity<B: ViewDataBinding>: AppCompatActivity() {

    protected lateinit var mBinding: B

    protected val mWebView by lazy { WebViewPool.getInstance().getWebView(this) }

    abstract fun getLayoutId(): Int

    abstract fun initView()

    abstract fun initData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, getLayoutId())
        mBinding.lifecycleOwner = this

        initView()
        initData()
    }

    override fun onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack()
            return
        }
        super.onBackPressed()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

}