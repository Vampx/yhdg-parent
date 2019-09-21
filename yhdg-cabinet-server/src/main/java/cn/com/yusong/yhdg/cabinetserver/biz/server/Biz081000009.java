package cn.com.yusong.yhdg.cabinetserver.biz.server;

import cn.com.yusong.yhdg.cabinetserver.constant.RespCode;
import cn.com.yusong.yhdg.cabinetserver.service.basic.ChargerUpgradePackDetailService;
import cn.com.yusong.yhdg.cabinetserver.service.basic.ChargerUpgradePackService;
import cn.com.yusong.yhdg.cabinetserver.service.basic.TerminalUpgradePackDetailService;
import cn.com.yusong.yhdg.cabinetserver.service.basic.TerminalUpgradePackService;
import cn.com.yusong.yhdg.cabinetserver.service.hdg.CabinetCodeService;
import cn.com.yusong.yhdg.cabinetserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.common.domain.basic.ChargerUpgradePack;
import cn.com.yusong.yhdg.common.domain.basic.ChargerUpgradePackDetail;
import cn.com.yusong.yhdg.common.domain.basic.TerminalUpgradePack;
import cn.com.yusong.yhdg.common.domain.basic.TerminalUpgradePackDetail;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetCode;
import cn.com.yusong.yhdg.common.protocol.msg08.Msg081000009;
import cn.com.yusong.yhdg.common.protocol.msg08.Msg081000011;
import cn.com.yusong.yhdg.common.protocol.msg08.Msg082000009;
import cn.com.yusong.yhdg.common.protocol.msg08.Msg082000011;
import cn.com.yusong.yhdg.common.tool.netty.Biz;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public  class Biz081000009 extends Biz {

    @Autowired
    CabinetCodeService cabinetCodeService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    ChargerUpgradePackService chargerUpgradePackService;
    @Autowired
    ChargerUpgradePackDetailService chargerUpgradePackDetailService;

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg081000009 request = (Msg081000009) message;
        Msg082000009 response = new Msg082000009();
        response.setSerial(request.getSerial());

        if (request.checkCRC == null || request.checkCRC == false) {
            response.rtnCode = RespCode.CODE_4.getValue();
            writeAndFlush(context, response);
            return;
        }

        CabinetCode cabinetCode = cabinetCodeService.find(request.getCode());
        if(cabinetCode == null) {
            response.rtnCode = RespCode.CODE_3.getValue();
            writeAndClose(context, response);
            return;
        }
        Cabinet cabinet = cabinetService.find(cabinetCode.getCode());
        if(cabinet == null) {
            response.rtnCode = RespCode.CODE_7.getValue();
            writeAndClose(context, response);
            return;
        }

        //查询充电器配置版本
        ChargerUpgradePackDetail chargerUpgradePackDetail = chargerUpgradePackDetailService.findByTerminal(cabinet.getId());
        if(chargerUpgradePackDetail == null){
            //更新包没有，回无更新包
            response.rtnCode = RespCode.CODE_12.getValue();
            writeAndClose(context, response);
            return;
        }

        ChargerUpgradePack chargerUpgradePack = chargerUpgradePackService.find(chargerUpgradePackDetail.getUpgradePackId());
        if(chargerUpgradePack == null){
            //更新包没有，回无更新包
            response.rtnCode = RespCode.CODE_12.getValue();
            writeAndClose(context, response);
            return;
        }

        boolean hasNewVersion = false;

        if (chargerUpgradePackDetail != null) {
            //服务器有新版本
            hasNewVersion = true;
            response.fileLength = chargerUpgradePack.getSize().intValue();
            response.md5 = chargerUpgradePack.getMd5Sum();
            writeAndFlush(context, response);
        }

        if (!hasNewVersion) {
            //更新包有，但是不在更新队列，回无更新包
            response.rtnCode = RespCode.CODE_12.getValue();
            writeAndClose(context, response);
            return;
        }
    }
}
