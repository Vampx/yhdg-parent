package cn.com.yusong.yhdg.cabinetserver.biz.server;

import cn.com.yusong.yhdg.cabinetserver.constant.RespCode;
import cn.com.yusong.yhdg.cabinetserver.service.basic.TerminalUpgradePackDetailService;
import cn.com.yusong.yhdg.cabinetserver.service.basic.TerminalUpgradePackService;
import cn.com.yusong.yhdg.cabinetserver.service.hdg.CabinetCodeService;
import cn.com.yusong.yhdg.cabinetserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.common.domain.basic.TerminalUpgradePack;
import cn.com.yusong.yhdg.common.domain.basic.TerminalUpgradePackDetail;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetCode;
import cn.com.yusong.yhdg.common.protocol.msg08.Msg081000002;
import cn.com.yusong.yhdg.common.protocol.msg08.Msg081000011;
import cn.com.yusong.yhdg.common.protocol.msg08.Msg082000002;
import cn.com.yusong.yhdg.common.protocol.msg08.Msg082000011;
import cn.com.yusong.yhdg.common.tool.netty.Biz;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public  class Biz081000011 extends Biz {

    @Autowired
    CabinetCodeService cabinetCodeService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    TerminalUpgradePackService terminalUpgradePackService;
    @Autowired
    TerminalUpgradePackDetailService terminalUpgradePackDetailService;

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg081000011 request = (Msg081000011) message;
        Msg082000011 response = new Msg082000011();
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

        List<TerminalUpgradePack> upgradePackList = terminalUpgradePackService.findByOldVersion(TerminalUpgradePack.PackType.SUBCABINET_UPGRADE.getValue(), request.version);;

        if(upgradePackList.isEmpty()){
            //更新包没有，回无更新包
            response.rtnCode = RespCode.CODE_12.getValue();
            writeAndClose(context, response);
            return;
        }
        boolean hasNewVersion = false;
        for(TerminalUpgradePack upgradePack : upgradePackList){
            TerminalUpgradePackDetail upgradePackDetail = terminalUpgradePackDetailService.find(upgradePack.getId(), cabinet.getId());
            if (upgradePackDetail != null) {
                //服务器有新版本
                hasNewVersion = true;
                response.fileLength = upgradePack.getSize().intValue();
                response.md5 = upgradePack.getMd5Sum();
                writeAndFlush(context, response);
            }
        }
        if (!hasNewVersion) {
            //更新包有，但是不在更新队列，回无更新包
            response.rtnCode = RespCode.CODE_12.getValue();
            writeAndClose(context, response);
            return;
        }
    }
}
