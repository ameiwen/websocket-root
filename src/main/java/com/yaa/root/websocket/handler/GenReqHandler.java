package com.yaa.root.websocket.handler;

import com.yaa.root.websocket.bean.ChannelReq;
import com.yaa.root.websocket.processor.ServerProcessor;
import com.yaa.root.websocket.util.WsResultUtil;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

public class GenReqHandler extends ServerProcessor  {

    @Override
    public void handleMessage(WsRequest wsRequest, ChannelReq channelReq, ChannelContext channelContext) throws Exception {
        WsResponse response = WsResultUtil.buildResponseByText("单次消息");
        Tio.send(channelContext,response);
    }
}
