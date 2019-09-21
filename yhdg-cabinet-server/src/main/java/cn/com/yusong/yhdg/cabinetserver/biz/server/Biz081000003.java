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
import cn.com.yusong.yhdg.common.protocol.msg08.Msg081000003;
import cn.com.yusong.yhdg.common.protocol.msg08.Msg082000003;
import cn.com.yusong.yhdg.common.tool.netty.Biz;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public  class Biz081000003 extends Biz {
    final Logger log = LogManager.getLogger(Biz081000003.class);

    @Autowired
    CabinetCodeService cabinetCodeService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    TerminalUpgradePackService terminalUpgradePackService;
    @Autowired
    TerminalUpgradePackDetailService terminalUpgradePackDetailService;

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) {

        Msg081000003 request = (Msg081000003) message;
        Msg082000003 response = new Msg082000003();
        response.setSerial(request.getSerial());
        response.seek =  request.seek;

        if(request.length == 1024){
            request.length = 512;
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
            TerminalUpgradePackDetail detail = terminalUpgradePackDetailService.find(upgradePack.getId(), cabinet.getId());
            if (detail != null) {
                //服务器有新版本
                hasNewVersion = true;

                File file = new File("/usr/local/yhdg/web-server/app" + upgradePack.getFilePath());
                long fileSize = file.length();
                if (fileSize > Integer.MAX_VALUE) {
                    log.error("file {} too big...", file.getAbsolutePath());
                    response.rtnCode = RespCode.CODE_12.getValue();
                    writeAndClose(context, response);
                    return;
                }
                if(request.seek >= file.length()) {
                    log.debug("seek:" + request.seek + "大于文件长度：" + file.length());
                    response.content = new byte[0];
                    response.length = 0;
                    writeAndClose(context, response);
                    return;
                }
                FileInputStream fi = null;
                byte[] buffer = new byte[request.length];
                try {
                    fi = new FileInputStream(file);
                    fi.skip(request.seek);
                    int readBytes = fi.read(buffer);
                    if(readBytes != buffer.length) {
                        //最后一段
                        byte[] bytes = new byte[readBytes];
                        System.arraycopy(buffer, 0, bytes, 0, readBytes);
                        buffer = bytes;
                    }

                }  catch (IOException e) {
                    log.error("读文件"+ file.getAbsolutePath() + " 错误", e);
                } finally {
                    IOUtils.closeQuietly(fi);
                }
                log.debug("读文件"+ file.getAbsolutePath() + " 完成，文件大小为{}", buffer.length);

                response.content =  buffer;
                response.length = buffer.length;
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
