package com.yaa.root.websocket.processor;

import com.yaa.root.websocket.bean.ChannelReq;
import org.tio.core.ChannelContext;
import org.tio.websocket.common.WsRequest;


/**
 * 主要的操作抽象
 */
public abstract class ServerProcessor {

    /**
     * 收到文本信息时的通知操作
     *
     * @param wsRequest
     * @param channelReq
     * @param channelContext
     * @return
     * @throws Exception
     */
    public abstract void handleMessage(WsRequest wsRequest, ChannelReq channelReq, ChannelContext channelContext) throws Exception;

}
