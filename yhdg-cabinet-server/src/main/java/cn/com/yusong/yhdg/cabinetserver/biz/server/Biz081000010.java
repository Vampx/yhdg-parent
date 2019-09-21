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
import cn.com.yusong.yhdg.common.protocol.msg08.Msg081000010;
import cn.com.yusong.yhdg.common.protocol.msg08.Msg081000012;
import cn.com.yusong.yhdg.common.protocol.msg08.Msg082000010;
import cn.com.yusong.yhdg.common.protocol.msg08.Msg082000012;
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
public  class Biz081000010 extends Biz {
    final Logger log = LogManager.getLogger(Biz081000010.class);

    @Autowired
    CabinetCodeService cabinetCodeService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    ChargerUpgradePackService chargerUpgradePackService;
    @Autowired
    ChargerUpgradePackDetailService chargerUpgradePackDetailService;

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) {

        Msg081000010 request = (Msg081000010) message;
        Msg082000010 response = new Msg082000010();
        response.setSerial(request.getSerial());

        if (request.checkCRC == null || request.checkCRC == false) {
            response.rtnCode = RespCode.CODE_4.getValue();
            writeAndFlush(context, response);
            return;
        }

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

            File file = new File("/usr/local/yhdg/web-server/app" + chargerUpgradePack.getFilePath());
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

        if (!hasNewVersion) {
            //更新包有，但是不在更新队列，回无更新包
            response.rtnCode = RespCode.CODE_12.getValue();
            writeAndClose(context, response);
            return;
        }
    }
}
