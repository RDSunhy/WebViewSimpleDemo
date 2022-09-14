package com.sunhy.demo.apt.processor

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.sunhy.demo.apt.annotations.JsBridgeCommand
import java.io.IOException
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

@AutoService(Processor::class)
class JsBridgeCommandProcessor : AbstractProcessor() {

    companion object {
        private const val TAG = "JsBridgeCommandProcessor_APT"
    }

    private lateinit var mElementUtils: Elements
    private lateinit var mTypeUtils: Types
    private lateinit var mFilerUtils: Filer
    private lateinit var mMessagerUtils: Messager

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        mElementUtils = processingEnv.elementUtils
        mTypeUtils = processingEnv.typeUtils
        mFilerUtils = processingEnv.filer
        mMessagerUtils = processingEnv.messager
    }

    //指定处理的版本
    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    //给到需要处理的注解
    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        val types: LinkedHashSet<String> = LinkedHashSet()
        getSupportedAnnotations().forEach { clazz: Class<out Annotation> ->
            types.add(clazz.canonicalName)
        }

        return types
    }

    private fun getSupportedAnnotations(): Set<Class<out Annotation>> {
        val annotations: LinkedHashSet<Class<out Annotation>> = LinkedHashSet()
        // 需要解析的自定义注解
        annotations.add(JsBridgeCommand::class.java)
        return annotations
    }

    override fun process(
        p0: MutableSet<out TypeElement>?,
        roundEnvironment: RoundEnvironment?
    ): Boolean {
        println("$TAG start")
        // 要自动注册的 Command Map <name, command>
        val commandMap = mutableMapOf<String, String>()
        roundEnvironment?.getElementsAnnotatedWith(JsBridgeCommand::class.java)
            ?.forEach { element ->
                (element as? TypeElement)?.let { item ->
                    // 全类名
                    val clz = item.qualifiedName.toString()
                    println("$TAG getClz = $clz")

                    // 获取注解参数 name
                    val name = item.getAnnotation(JsBridgeCommand::class.java).name
                    println("$TAG getName = $name")

                    // 放入map
                    commandMap[name] = clz
                }
            }

        // 生成代码 路径
        val packageName = "com.sunhy.demo.apt"

        // 生成类方法
        val registerMethodBuilder = FunSpec.builder("autoRegist")
            .addComment("web jsbridge command auto load")

        // 定义局部变量
        val arrayMap = ClassName("android.util", "ArrayMap")
        val iBridgeCommand = ClassName("com.sunhy.demo.web.bridge", "IBridgeCommand")
        val arrayMapCommand = arrayMap.parameterizedBy(String::class.asTypeName(), iBridgeCommand)
        registerMethodBuilder.addStatement("val commandMap = %L()", arrayMapCommand)

        commandMap.forEach { (key, value) ->
            registerMethodBuilder.addStatement("commandMap[%S] = $value()", key)
        }

        // 方法返回类型
        registerMethodBuilder.returns(arrayMapCommand)
        registerMethodBuilder.addStatement("return commandMap")

        // 生成伴生对象
        val companionObject = TypeSpec.companionObjectBuilder()
            .addFunction(registerMethodBuilder.build())
            .build()

        // 生成类
        val clazzBuilder = TypeSpec.classBuilder("JsBridgeUtil")
            .addType(companionObject)

        //生成类文件
        val classFile = FileSpec.builder(packageName, "JsBridgeUtil")
            .addType(clazzBuilder.build())
            .build()

        //输出到文件
        try {
            mFilerUtils.let { filer -> classFile.writeTo(filer) }
        } catch (e: IOException) {
            println(e.message)
        }

        return false

    }

}