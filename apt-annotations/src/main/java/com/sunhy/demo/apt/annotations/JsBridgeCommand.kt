package com.sunhy.demo.apt.annotations

@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class JsBridgeCommand(
    val name: String
)