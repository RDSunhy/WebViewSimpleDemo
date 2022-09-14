package com.sunhy.demo.base.utils

import com.blankj.utilcode.util.GsonUtils
import com.sunhy.demo.base.entity.UserInfo

object LoginUtils {

    private var userInfo: UserInfo? = null

    fun getUserInfo(): String{
        return GsonUtils.toJson(userInfo)
    }

    // 模拟登陆
    fun login(){
        this.userInfo = UserInfo("孙先森@", "ASDJKLQJDKL12KLDKL3KLJ1234KL12KLLDA")
    }
}