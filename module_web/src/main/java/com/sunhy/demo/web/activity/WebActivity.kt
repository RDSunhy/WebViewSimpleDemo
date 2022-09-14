package com.sunhy.demo.web.activity

import android.util.Log
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import com.sunhy.demo.base.utils.LoginUtils
import com.sunhy.demo.web.BaseWebView
import com.sunhy.demo.web.R
import com.sunhy.demo.web.core.BaseWebActivity
import com.sunhy.demo.web.databinding.ActivityWebBinding

class WebActivity : BaseWebActivity<ActivityWebBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_web

    override fun initView() {
        mBinding.webContainer.addView(
            mWebView,
            RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            )
        )

        mWebView.setLifecycleOwner(this)
        mWebView.setBlankMonitorCallback(object : BaseWebView.BlankMonitorCallback {
            override fun onBlank() {
                AlertDialog.Builder(this@WebActivity)
                    .setTitle("提示")
                    .setMessage("检测到页面发生异常，是否重新加载？")
                    .setPositiveButton("重新加载") { dialog, _ ->
                        dialog.dismiss()
                        mWebView.reload()
                    }
                    .setNegativeButton("返回上一页") { dialog, _ ->
                        dialog.dismiss()
                        onBackPressed()
                    }
                    .create()
                    .show()
            }
        })
    }

    override fun initData() {
        Log.e("web进程获取 UserInfo", LoginUtils.getUserInfo())
        val url = intent.getStringExtra("url")?:"https://www.baidu.com"
        mWebView.loadUrl(url)
    }

}