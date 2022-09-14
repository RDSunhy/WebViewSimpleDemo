package com.sunhy.demo.web.activity

import android.widget.RelativeLayout
import com.sunhy.demo.web.R
import com.sunhy.demo.web.core.BaseTemplateWebActivity
import com.sunhy.demo.web.core.BaseWebActivity
import com.sunhy.demo.web.databinding.ActivityNewsDetailBinding

class NewsDetailActivity : BaseTemplateWebActivity<ActivityNewsDetailBinding>() {

    override fun getLayoutId() = R.layout.activity_news_detail

    override fun initView() {

        mBinding.webContainer.addView(
            mWebView,
            RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            )
        )

        mWebView.setLifecycleOwner(this)

        val title = intent.getStringExtra("title")?:"获取title失败"
        val tag = intent.getStringExtra("tag")?:""
        val content = intent.getStringExtra("content")?:""
        mWebView.evaluateJavascript("javascript:setNewsData(`$title`, `$tag`, `$content`)") {}
    }

    override fun initData() {
    }
}