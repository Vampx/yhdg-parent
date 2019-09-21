package cn.com.yusong.yhdg.batteryserver.biz.server;

import cn.com.yusong.yhdg.batteryserver.constant.RespCode;
import cn.com.yusong.yhdg.batteryserver.entity.HeartParam;
import cn.com.yusong.yhdg.batteryserver.entity.HeartResult;
import cn.com.yusong.yhdg.batteryserver.service.basic.BatteryUpgradePackDetailService;
import cn.com.yusong.yhdg.batteryserver.service.basic.BatteryUpgradePackService;
import cn.com.yusong.yhdg.batteryserver.service.hdg.BatteryHeartService;
import cn.com.yusong.yhdg.batteryserver.service.hdg.BatteryService;
import cn.com.yusong.yhdg.common.domain.basic.BatteryUpgradePack;
import cn.com.yusong.yhdg.common.domain.basic.BatteryUpgradePackDetail;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryParameter;
import cn.com.yusong.yhdg.common.protocol.msg06.Msg061000002;
import cn.com.yusong.yhdg.common.protocol.msg06.Msg062000002;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/*
 * 电池长心跳
 */
@Component
public class Biz061000002 extends AbstractBiz {
    @Autowired
    BatteryHeartService batteryHeartService;
    @Autowired
    BatteryUpgradePackService batteryUpgradePackService;
    @Autowired
    BatteryUpgradePackDetailService batteryUpgradePackDetailService;
    @Autowired
    BatteryService batteryService;

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg061000002 request = (Msg061000002) message;
        Msg062000002 response = new Msg062000002();
        response.setSerial(request.getSerial());

        //校验
        String sign = CodecUtils.signMd5(request.json);
        if(!sign.equals(request.sign)){
            response.rtnCode = RespCode.CODE_5.getValue();
            writeAndClose(context, response);
            return;
        }

        //处理心跳
        HeartParam heartParam = (HeartParam)YhdgUtils.decodeJson(request.json, HeartParam.class);
        heartParam.jsonData = request.json;
        if (!StringUtils.isEmpty(heartParam.IMEI)) {
            heartParam.heartType = BatteryParameter.HeartType.LONG_HEART.getValue();
            HeartResult heartResult = batteryHeartService.heart(heartParam);

            List<BatteryUpgradePack> upgradePackList = batteryUpgradePackService.findByOldVersion(null,  heartResult.version);
            //默认无新版本
            heartResult.json.put("NewVer", 0);
            if(!upgradePackList.isEmpty()) {
                for(BatteryUpgradePack upgradePack : upgradePackList){
                    BatteryUpgradePackDetail detail = batteryUpgradePackDetailService.find(upgradePack.getId(), heartResult.id);
                    if (detail != null) {
                        //服务器有新版本
                        heartResult.json.put("NewVer", 1);
                        heartResult.json.put("Md5", upgradePack.getMd5Sum());
                        heartResult.json.put("Total", upgradePack.getSize());
                    }
                }
            }
            response.json =  YhdgUtils.encodeJson(heartResult.json)
                    .replaceAll("\\r","")
                    .replaceAll("\\n","")
                    .replaceAll(" ","");
            writeAndFlush(context, response);
        }else{
            response.rtnCode = RespCode.CODE_2.getValue();
            writeAndClose(context, response);
            return;
        }
    }
}
