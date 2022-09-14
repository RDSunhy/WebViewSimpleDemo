package com.sunhy.demo.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.blankj.utilcode.util.LogUtils
import com.sunhy.demo.R
import com.sunhy.demo.databinding.ActivityMainBinding
import com.sunhy.demo.web.activity.NewsDetailActivity
import com.sunhy.demo.web.activity.WebActivity

class MainActivity : AppCompatActivity() {

    protected lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.lifecycleOwner = this

        mBinding.bnWeb.setOnClickListener {
            startActivityForResult(Intent(this, WebActivity::class.java).apply {
                putExtra("url", "https://www.baidu.com")
            }, 1)
        }

        mBinding.bnNewsTemplate.setOnClickListener {
            val title = "《闻香榭》热播 刘帅海滩度假写真俏皮甜美"
            val tag = "影视资讯"
            val content =
                "<p ><img src=\"https://img.yparse.com/upload/p5YD3D9o000oq2Wfoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2gzYo000oHupSEWlx9VkENhkLBb53T2PZP3adL3HwTYuBllRA52Z90mtAO0O0OO0O0O.jpg\"></p><p ><img src=\"https://img.yparse.com/upload/rslQi2ho000or2afoj3ZiPjutpRAjVvqeOyGbuQUyHBG00M3ofW7W2gzYo000oXlpHQXlEJQkkY0xo000o0LvHzxa8KlaNWqR1fc6U4zFgBxZ90mtAO0O0OO0O0O.jpg\"></p><p ><img src=\"https://img.yparse.com/upload/ocZUjWgkrGOfoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2gzY7TooSUQxBpUx0Ezkbta6CCrasX1Odz7G1ONuh1lQQkiZ90mtAO0O0OO0O0O.jpg\"></p><p ><img src=\"https://img.yparse.com/upload/pcRW3D0uqzWfoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2gzYo000oTsrHMWmUkCkBBkwbwJ5ielPZegNIGtRwGMthwzFA0jZ90mtAO0O0OO0O0O.jpg\"></p><p ><img src=\"https://img.yparse.com/upload/8pFZhjkooo00o2Sfoj3ZiPjutpRAjFvqeOyGbuQUyHBG00M3ofW7W2gzY7XqoydGmUpUzBJuxo000o5d5yeiOJChP4SvSVSNvBsxRQh1Z90mtAO0O0OO0O0O.jpg\"></p><p>玄幻古偶剧《闻香榭》本周持续热播,刘帅饰演的红袖因为在剧中总是助攻男女主恋情而被观众封为&ldquo;古代情感专家&rdquo;,近日刘帅一组海滩度假风写真曝光,以粉色吊带牛仔裤、白衬衫小白鞋两套造型亮相的刘帅邻家甜美、元气满满。</p>"
            val intent = Intent(this, NewsDetailActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("tag", tag)
            intent.putExtra("content", content)
            startActivity(intent)
        }

        mBinding.bnNewsList.setOnClickListener {
            startActivity(Intent(this, NewsListActivity::class.java))
        }

        mBinding.bnBridge.setOnClickListener {
            startActivity(Intent(this, WebActivity::class.java).apply {
                putExtra("url", "file:///android_asset/test_default_bridge.html")
            })
        }

        mBinding.bnCommandBridge.setOnClickListener {
            startActivity(Intent(this, WebActivity::class.java).apply {
                putExtra("url", "file:///android_asset/test_command_bridge.html")
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        LogUtils.e(
            "返回结果",
            "requestCode: $requestCode",
            "resultCode: $resultCode",
            "data: ${data?.getStringExtra("message")}"
        )
    }
}