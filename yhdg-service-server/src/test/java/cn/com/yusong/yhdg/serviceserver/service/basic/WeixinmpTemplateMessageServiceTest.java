package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.WeixinmpTemplateMessage;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.common.tool.weixin.WxMpServiceHolder;
import cn.com.yusong.yhdg.serviceserver.BaseJunit4Test;
import cn.com.yusong.yhdg.serviceserver.config.AppConfig;
import cn.com.yusong.yhdg.serviceserver.service.hdg.*;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Map;

public class WeixinmpTemplateMessageServiceTest extends BaseJunit4Test {
	@Autowired
	WeixinmpTemplateMessageService service;

	@Test
	public void sendMessage() throws IOException {
		AppConfig config = SpringContextHolder.getBean(AppConfig.class);

		CabinetService cabinetService = SpringContextHolder.getBean(CabinetService.class);
		cabinetService.refreshOnline();
		cabinetService.refreshOfflineMessageTime();

		MobileMessageService mobileMessageService = SpringContextHolder.getBean(MobileMessageService.class);
		mobileMessageService.scanMessage();

		CabinetBoxService cabinetBoxService = SpringContextHolder.getBean(CabinetBoxService.class);
		cabinetBoxService.clearLockTime();

		BackBatteryOrderService backBatteryOrderService = SpringContextHolder.getBean(BackBatteryOrderService.class);
		backBatteryOrderService.refreshExpireOrder();

		BatteryService batteryService = SpringContextHolder.getBean(BatteryService.class);
		batteryService.refreshOnlineStatus();

		BatteryOrderService batteryOrderService = SpringContextHolder.getBean(BatteryOrderService.class);
		batteryOrderService.batteryOrderNotTakeTimeOut();

		WeixinmpTemplateMessageService weixinmpTemplateMessageService = SpringContextHolder.getBean(WeixinmpTemplateMessageService.class);
//		WxMpServiceHolder wxMpServiceHolder = SpringContextHolder.getBean(WxMpServiceHolder.class);
		WxMpServiceHolder wxMpServiceHolder = new WxMpServiceHolder(new WxMpServiceHolder.Store() {
			@Override
			public WxMpService obtainPartner(int id) {
				return null;
			}

			@Override
			public WxMpService obtainWeixinmp(int id) {
				return null;
			}
		});
		service.sendMessage(wxMpServiceHolder, null, config.getWeixinUrl());
	}

	@Test
	public void doSend() {
		WeixinmpTemplateMessageService.Sender defaultSender = new WeixinmpTemplateMessageService.Sender() {
			@Override
			public void doSend(WxMpServiceHolder wxMpServiceHolder, String weixinMsgUrl, Map<Integer, Map<String, String>> colorsMap, WeixinmpTemplateMessage message) throws WxErrorException, IOException {
				service.doSend(wxMpServiceHolder, weixinMsgUrl, colorsMap, message);
			}
		};
	}



}
