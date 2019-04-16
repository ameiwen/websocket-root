package com.yaa.root.websocket.handler;

import com.alibaba.fastjson.JSON;
import com.yaa.root.websocket.bean.ChannelReq;
import com.yaa.root.websocket.comm.Const;
import com.yaa.root.websocket.comm.ServerConfig;
import com.yaa.root.websocket.processor.ServerProcessor;
import com.yaa.root.websocket.util.WsResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.server.handler.IWsMsgHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerHandler extends AbsServerHandler implements IWsMsgHandler {

	private static Logger logger = LoggerFactory.getLogger(ServerHandler.class);

	public static ServerHandler me = new ServerHandler();

	private static final Map<String, ServerProcessor> handlerMap = new ConcurrentHashMap<>();

	static {
		handlerMap.put(Const.TIMER_HANDLER, new TimerReqHandler());//自动推送
		handlerMap.put(Const.GENE_HANDLER, new GenReqHandler());//普通推送
	}

	/**
	 * 字符消息（binaryType = blob）过来后会走这个方法
	 */
	@Override
	public Object onText(WsRequest wsRequest, String text, ChannelContext channelContext) throws Exception {
		if (text.equals(ServerConfig.HEART_BEAT)) {
			Map<String, Object> heart = new HashMap<>();
			heart.put("pong", new Date().getTime());
			Tio.send(channelContext, WsResultUtil.buildResponseByJson(heart));
			logger.info("heat:" + text);
		}
		ChannelReq channelReq = JSON.parseObject(text, ChannelReq.class);
		if (channelReq != null) {
			ServerProcessor server = handlerMap.get(channelReq.getChannel());
			if (server != null) {
				if (Const.TIMER_HANDLER.equals(channelReq.getChannel())) {
					//定时发送,绑定通道
					Tio.bindGroup(channelContext, channelReq.getChannel());
				}
				server.handleMessage(wsRequest, channelReq, channelContext);
			}
		}
		return null;
	}

}
