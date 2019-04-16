# websocket-root

基于Tio写的基本websocket Server

定时推送加普通推送

客户端用的js

``code
    var ws = new WebSocket("ws://192.168.0.169:9326");

    ws.onopen = function (event) {
    };

    ws.onmessage=function (event) {
        appendToDashboard("收到消息："+event.data);
    };

    ws.onclose=function (event) {
        appendToDashboard(event.data);
    };

    ws.onerror=function (event,e) {
        appendToDashboard(event.data);
    };
``
