package com.yaa.root.websocket.handler;

import com.yaa.root.websocket.bean.ChannelReq;
import com.yaa.root.websocket.processor.ServerProcessor;
import com.yaa.root.websocket.util.WsResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class TimerReqHandler extends ServerProcessor {

	private static Logger logger = LoggerFactory.getLogger(TimerReqHandler.class);

	private static final Lock lock = new ReentrantLock();

	private static final ConcurrentHashMap<String, TimerTread> timerMap = new ConcurrentHashMap<>();

	/**
	 * @param wsRequest
	 * @param channelReq
	 * @param channelContext
	 * @return
	 * @throws Exception
	 */
	@Override
	public void handleMessage(WsRequest wsRequest, ChannelReq channelReq, ChannelContext channelContext) throws Exception {
		String channel = channelReq.getChannel();
		logger.info("Channel[" + channel + "] subscribed");
		lock.lock();
		try {
			//保持一个线程运行
			TimerTread timerTread = timerMap.get(channel);
			if (timerTread == null) {
				timerTread = new TimerTread(1000l, channelContext, channelReq);
				timerTread.setDaemon(true);
				timerTread.start();
				timerMap.putIfAbsent(channel, timerTread);
			}
		} finally {
			lock.unlock();
		}
	}

	class TimerTread extends Thread {
		private Long sleepMills = 0l;
		private ChannelReq channelReq;
		private ChannelContext channelContext;

		public TimerTread(Long sleepMills, ChannelContext channelContext, ChannelReq channelReq) {
			this.sleepMills = sleepMills;
			this.channelReq = channelReq;
			this.channelContext = channelContext;
		}

		@Override
		public void run() {
			while (true) {
				try {
					int count = Tio.getChannelContextsByGroup(channelContext.getGroupContext(),channelReq.getChannel()).getObj().size();
					logger.info("当前订阅数量：" + count);
					WsResponse response = WsResultUtil.buildResponseByText("定时发送消息");
					Tio.sendToGroup(channelContext.getGroupContext(), channelReq.getChannel(), response);
					Thread.sleep(sleepMills);
				} catch (Exception e) {
					logger.error("error", e);
				}
			}
		}
	}

}
