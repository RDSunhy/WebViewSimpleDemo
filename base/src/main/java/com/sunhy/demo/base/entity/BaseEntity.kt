package com.sunhy.demo.base.entity

import com.google.gson.annotations.SerializedName

class BaseEntity<T> {

    @SerializedName("list")
    var list: List<T> = mutableListOf()
}