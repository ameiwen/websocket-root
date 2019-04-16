package com.yaa.root.websocket.starter;

import com.yaa.root.websocket.comm.ServerConfig;
import com.yaa.root.websocket.handler.ServerHandler;
import com.yaa.root.websocket.linstener.ServerListener;
import org.springframework.stereotype.Component;
import org.tio.server.ServerGroupContext;
import org.tio.websocket.server.WsServerStarter;

import javax.annotation.PostConstruct;

@Component
public class SocketServer {

    private ServerGroupContext serverGroupContext;

    private WsServerStarter wsServerStarter;

    @PostConstruct
    public void starts() throws Exception {
        wsServerStarter = new WsServerStarter(ServerConfig.SERVER_PORT, ServerHandler.me);

        serverGroupContext = wsServerStarter.getServerGroupContext();
        serverGroupContext.setServerAioListener(ServerListener.me);

        //设置ip统计时间段
//        serverGroupContext.ipStats.addDurations(ServerConfig.IpStatDuration.IP_STAT_DURATIONS);
        //设置ip监控
//        serverGroupContext.setIpStatListener(ServerIpStatListener.me);
        //设置心跳超时时间
        serverGroupContext.setHeartbeatTimeout(ServerConfig.HEARTBEAT_TIMEOUT);

        ServerConfig.groupContext = getServerGroupContext();
        wsServerStarter.start();
    }



    public ServerGroupContext getServerGroupContext() {
        return serverGroupContext;
    }



}
