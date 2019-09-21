package cn.com.yusong.yhdg.serviceserver.job;


import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.common.tool.alipay.AlipayfwClientHolder;
import cn.com.yusong.yhdg.common.tool.weixin.WxMpServiceHolder;
import cn.com.yusong.yhdg.serviceserver.config.AppConfig;
import cn.com.yusong.yhdg.serviceserver.service.basic.AlipayfwTemplateMessageService;
import cn.com.yusong.yhdg.serviceserver.service.basic.MobileMessageService;
import cn.com.yusong.yhdg.serviceserver.service.basic.VoiceMessageService;
import cn.com.yusong.yhdg.serviceserver.service.basic.WeixinmpTemplateMessageService;
import cn.com.yusong.yhdg.serviceserver.service.hdg.*;
import cn.com.yusong.yhdg.serviceserver.service.yms.TerminalService;

import java.io.IOException;

/**
 * 一分钟
 */
public class OneMinuteTask extends ConcurrentTask {
    @Override
    protected String getZkNode() {
        return ConstEnum.Node.NODE_ONE_MINUTE_TASK.getValue();
    }

    @Override
    protected void doBiz() throws IOException {
        AppConfig config = SpringContextHolder.getBean(AppConfig.class);

        CabinetService cabinetService = SpringContextHolder.getBean(CabinetService.class);
        cabinetService.refreshOnline();
        cabinetService.refreshOfflineMessageTime();

        MobileMessageService mobileMessageService = SpringContextHolder.getBean(MobileMessageService.class);
        mobileMessageService.scanMessage();

        CabinetBoxService cabinetBoxService = SpringContextHolder.getBean(CabinetBoxService.class);
        cabinetBoxService.clearLockTime();

        PacketPeriodOrderService packetPeriodOrderService = SpringContextHolder.getBean(PacketPeriodOrderService.class);
        packetPeriodOrderService.used();
        packetPeriodOrderService.expire();

        BespeakOrderService bespeakOrderService = SpringContextHolder.getBean(BespeakOrderService.class);
        bespeakOrderService.expire();

        BackBatteryOrderService backBatteryOrderService = SpringContextHolder.getBean(BackBatteryOrderService.class);
        backBatteryOrderService.refreshExpireOrder();

        BatteryService batteryService = SpringContextHolder.getBean(BatteryService.class);
        batteryService.refreshOnlineStatus();

        BatteryOrderService batteryOrderService = SpringContextHolder.getBean(BatteryOrderService.class);
        batteryOrderService.batteryOrderNotTakeTimeOut();

        WeixinmpTemplateMessageService weixinmpTemplateMessageService = SpringContextHolder.getBean(WeixinmpTemplateMessageService.class);
        WxMpServiceHolder wxMpServiceHolder = SpringContextHolder.getBean(WxMpServiceHolder.class);
        weixinmpTemplateMessageService.sendMessage(wxMpServiceHolder, null, config.getWeixinUrl());

        AlipayfwTemplateMessageService alipayfwTemplateMessageService = SpringContextHolder.getBean(AlipayfwTemplateMessageService.class);
        AlipayfwClientHolder alipayfwClientHolder = SpringContextHolder.getBean(AlipayfwClientHolder.class);
        alipayfwTemplateMessageService.sendMessage(alipayfwClientHolder, null, config.getWeixinUrl());

        TerminalService terminalService = SpringContextHolder.getBean(TerminalService.class);
        terminalService.refreshOnline();

        VoiceMessageService voiceMessageService = SpringContextHolder.getBean(VoiceMessageService.class);
        voiceMessageService.scanMessage();
    }
}
