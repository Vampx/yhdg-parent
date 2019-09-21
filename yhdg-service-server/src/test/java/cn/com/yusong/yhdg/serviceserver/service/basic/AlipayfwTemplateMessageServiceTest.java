package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.common.tool.alipay.AlipayfwClientHolder;
import cn.com.yusong.yhdg.common.tool.weixin.WxMpServiceHolder;
import cn.com.yusong.yhdg.serviceserver.BaseJunit4Test;
import cn.com.yusong.yhdg.serviceserver.config.AppConfig;
import cn.com.yusong.yhdg.serviceserver.service.hdg.*;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayRequest;
import com.alipay.api.AlipayResponse;
import me.chanjar.weixin.mp.api.WxMpService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Map;

public class AlipayfwTemplateMessageServiceTest extends BaseJunit4Test {
	@Autowired
	AlipayfwTemplateMessageService service;

	@Test
	public void sendMessage() throws IOException{
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
		weixinmpTemplateMessageService.sendMessage(wxMpServiceHolder, null, config.getWeixinUrl());

		AlipayClient alipayClient = new AlipayClient() {
			@Override
			public <T extends AlipayResponse> T execute(AlipayRequest<T> alipayRequest) throws AlipayApiException {
				return null;
			}

			@Override
			public <T extends AlipayResponse> T execute(AlipayRequest<T> alipayRequest, String s) throws AlipayApiException {
				return null;
			}

			@Override
			public <T extends AlipayResponse> T execute(AlipayRequest<T> alipayRequest, String s, String s1) throws AlipayApiException {
				return null;
			}

			@Override
			public <T extends AlipayResponse> T pageExecute(AlipayRequest<T> alipayRequest) throws AlipayApiException {
				return null;
			}

			@Override
			public <T extends AlipayResponse> T sdkExecute(AlipayRequest<T> alipayRequest) throws AlipayApiException {
				return null;
			}

			@Override
			public <T extends AlipayResponse> T pageExecute(AlipayRequest<T> alipayRequest, String s) throws AlipayApiException {
				return null;
			}

			@Override
			public <TR extends AlipayResponse, T extends AlipayRequest<TR>> TR parseAppSyncResult(Map<String, String> map, Class<T> aClass) throws AlipayApiException {
				return null;
			}
		};

		AlipayfwClientHolder alipayfwClientHolder = new AlipayfwClientHolder(new AlipayfwClientHolder.Store() {
			@Override
			public AlipayClient obtainPartner(int id) {
				return null;
			}

			@Override
			public AlipayClient obtainAlipayfw(int id) {
				return null;
			}
		});
		service.sendMessage(alipayfwClientHolder, null, config.getWeixinUrl());
	}

}
