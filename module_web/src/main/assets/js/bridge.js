var jsBridge = {};

// 系统判断
jsBridge.os = {
    'isAndroid': Boolean(navigator.userAgent.match(/android/ig)),
    'isIOS': Boolean(navigator.userAgent.match(/iphone|ipod|iOS/ig))
};

// 回调 map
jsBridge.mapCallbacks = {}

// 回调处理
jsBridge.postBridgeCallback = function(key, data){
    var obj = jsBridge.mapCallbacks[key];
    if(obj.callback){
        obj.callback(data);
        delete jsBridge.mapCallbacks[key];
    }else{
        console.log('jsBridge postBridgeCallback', 'callback not found: ' + key)
    }
}

// 发送命令
jsBridge.sendCommand = function(command, params, callback) {
	var message = {
		'command': command
	}
	if (params && typeof params === 'object') { // 支持传参
	    message['params'] = params
	}
	if (callback && typeof callback === 'function') { // 支持回调
        var key = generateCallbackKey() // 生成回调key
        jsBridge.mapCallbacks[key] = { 'callback': callback }
	    message['params'] ['bridgeCallback'] = key
	}
	console.log('jsBridge sendCommand message', JSON.stringify(message))
	console.log('jsBridge sendCommand mapCallbacks', JSON.stringify(jsBridge.mapCallbacks))
	if (jsBridge.os.isAndroid) { // android 桥接
		window.bridge.sendCommand(JSON.stringify(message))
	} else if (jsBridge.os.isIOS) { // ios 桥接
		window.webkit.messageHandlers.bridge.sendCommand(JSON.stringify(message))
	}
}

window.jsBridge = jsBridge;

// 生成回调key
function generateCallbackKey(){
    return "bridgeCallback_" + new Date().getTime() + "_" + randomCode();
}

// 随机码 防止并发重复
function randomCode(){
    var code = ""
    for(var i = 0; i < 6; i++){
        code += Math.floor(Math.random() * 10)
    }
    return code;
}

console.log('bridge.js load success!!')