package cn.com.yusong.yhdg.batteryserver.biz.server;

import cn.com.yusong.yhdg.batteryserver.constant.RespCode;
import cn.com.yusong.yhdg.batteryserver.service.basic.BatteryUpgradePackDetailService;
import cn.com.yusong.yhdg.batteryserver.service.basic.BatteryUpgradePackService;
import cn.com.yusong.yhdg.batteryserver.service.hdg.BatteryService;
import cn.com.yusong.yhdg.common.domain.basic.BatteryUpgradePack;
import cn.com.yusong.yhdg.common.domain.basic.BatteryUpgradePackDetail;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.protocol.msg06.Msg061000003;
import cn.com.yusong.yhdg.common.protocol.msg06.Msg062000003;
import cn.com.yusong.yhdg.common.utils.*;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * 固件下载
 */
@Component
public class Biz061000003 extends AbstractBiz {
    @Autowired
    BatteryService batteryService;
    @Autowired
    BatteryUpgradePackService batteryUpgradePackService;
    @Autowired
    BatteryUpgradePackDetailService batteryUpgradePackDetailService;

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg061000003 request = (Msg061000003) message;
        Msg062000003 response = new Msg062000003();
        response.setSerial(request.getSerial());

        //校验
        String sign = CodecUtils.signMd5(request.json);
        if(!sign.equals(request.sign)){
            response.rtnCode = RespCode.CODE_5.getValue();
            writeAndClose(context, response);
            return;
        }

        Map jsonMap = (Map)YhdgUtils.decodeJson(request.json, Map.class);
        String IMEI = jsonMap.get("IMEI") != null? jsonMap.get("IMEI").toString() : "";
        Integer fotaType = jsonMap.get("fotaType") != null? Integer.parseInt(jsonMap.get("fotaType").toString()) : null;
        String version = jsonMap.get("Version") != null? jsonMap.get("Version").toString() : "";
        int seek = jsonMap.get("Seek") != null? Integer.parseInt(jsonMap.get("Seek").toString()) : 0;
        int length = jsonMap.get("Length") != null? Integer.parseInt(jsonMap.get("Length").toString()) : 0;
        response.seek =  seek;

        List<BatteryUpgradePack> upgradePackList = batteryUpgradePackService.findByOldVersion(fotaType , version);

        if(upgradePackList.isEmpty()){
            //更新包没有，回无更新包
            response.rtnCode = RespCode.CODE_4.getValue();
            writeAndClose(context, response);
            return;
        }

        boolean hasNewVersion = false;

        Battery battery = batteryService.findByCode(IMEI);
        if(battery == null){
            response.rtnCode = RespCode.CODE_4.getValue();
            writeAndClose(context, response);
            return;
        }

        for(BatteryUpgradePack upgradePack : upgradePackList){
            BatteryUpgradePackDetail detail = batteryUpgradePackDetailService.find(upgradePack.getId(), battery.getId());
            if (detail != null) {
                //服务器有新版本
                hasNewVersion = true;

                final String url = config.getStaticUrl() + upgradePack.getFilePath();
             //   String fileName = new File(upgradePack.getFilePath()).getName();
                File dir = new File(config.appDir, "upgrade");
                final File file = new File(dir, upgradePack.getFileName());
                if(!file.exists()) {
                    if(log.isDebugEnabled()) {
                        log.debug("file {} 不存在", file.getAbsolutePath());
                    }

                    AppUtils.makeParentDir(file);
                    final BatteryUpgradePack finalNewUpgradePack = upgradePack;
                    config.upgradeExecutorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            if(!file.exists()) {
                                if(log.isDebugEnabled()) {
                                    log.debug("开始下载 url: {}, file: {} ", url, file.getAbsolutePath());
                                }

                                try {
                                    File temp = new File(file.getParentFile(), IdUtils.uuid() + ".tmp");
                                    HttpClientUtils.download(url, temp);
                                    if(!temp.exists()){
                                        log.debug("文件服务器不存在 url: {}, file: {} ", url, file.getAbsolutePath());
                                        return ;
                                    }
                                    String hex = YhdgUtils.md5Hex(temp);
                                    if(!hex.equalsIgnoreCase(finalNewUpgradePack.getMd5Sum())) {
                                        log.error("下载文件后md5不一致 url: {}, file: {}, 标准md5: {}, 文件md5: {}", url, temp.getAbsolutePath(), finalNewUpgradePack.getMd5Sum(), hex);
                                        temp.delete();
                                    } else {
                                        if(file.exists()) {
                                            if(file.length() == 0 || !YhdgUtils.md5Hex(file).equalsIgnoreCase(finalNewUpgradePack.getMd5Sum())) {
                                                log.debug("原文件MD5错误, {}", file.getAbsolutePath());
                                                file.delete();
                                            }
                                        }
                                        temp.renameTo(file);

                                        if(file.exists() && file.length() > 0 && YhdgUtils.md5Hex(file).equalsIgnoreCase(finalNewUpgradePack.getMd5Sum())) {
                                            if(log.isDebugEnabled()) {
                                                log.debug("下载完成 url: {}, file: {} ", url, file.getAbsolutePath());
                                            }
                                        }
                                    }

                                } catch (Exception ex) {
                                    log.error("下载文件出现错误 url: {}, file: {} ", url, file.getAbsolutePath());
                                    log.error("下载文件出现错误", ex);
                                }

                            }
                        }
                    });

                    response.rtnCode = RespCode.CODE_4.getValue();
                    writeAndClose(context, response);
                    return;
                } else {
                    String hex = YhdgUtils.md5Hex(file);
                    if(file.length() == 0 || !hex.equalsIgnoreCase(upgradePack.getMd5Sum())) {
                        log.error("文件md5不一致 file: {}, 标准md5: {}, 文件md5: {}", file.getAbsolutePath(), upgradePack.getMd5Sum(), hex);
                        file.delete();

                        response.rtnCode = RespCode.CODE_4.getValue();
                        writeAndClose(context, response);
                        return;
                    }
                }

                long fileSize = file.length();
                Map json = new HashMap();
                if(seek == 0){
                    json.put("Md5",upgradePack.getMd5Sum());
                    json.put("Total",fileSize);
                }

                if (fileSize > Integer.MAX_VALUE) {
                    log.error("file {} too big...", file.getAbsolutePath());
                    response.rtnCode = RespCode.CODE_4.getValue();
                    writeAndClose(context, response);
                    return;
                }
                if(seek >= file.length()) {
                    log.debug("seek:" + seek  + "大于文件长度：" + file.length());
                    response.content = new byte[0];
                    response.length = 0;
                    writeAndClose(context, response);
                    return;
                }


                FileInputStream fi = null;
                byte[] buffer = new byte[length];
                try {
                    fi = new FileInputStream(file);
                    fi.skip(seek);
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

                response.length = buffer.length;
                response.content =  buffer;
                writeAndFlush(context, response);
            }
        }
        if (!hasNewVersion) {
            //更新包有，但是不在更新队列，回无更新包
            response.rtnCode = RespCode.CODE_4.getValue();
            writeAndClose(context, response);
            return;
        }
    }
}
