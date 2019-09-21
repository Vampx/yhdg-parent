package cn.com.yusong.yhdg.serviceserver.service.hdg;


import cn.com.yusong.yhdg.common.domain.basic.SyncCustomerInfo;
import cn.com.yusong.yhdg.common.domain.hdg.PushOrderMessage;
import cn.com.yusong.yhdg.common.utils.OkHttpClientUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.SyncCustomerInfoMapper;
import cn.com.yusong.yhdg.serviceserver.service.basic.InsertPushMessageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 推送
 */
@Service
public class SyncCustomerInfoService extends InsertPushMessageService {
    static Logger log = LogManager.getLogger(SyncCustomerInfoService.class);

    //租电测试服务器
    //String SYNC_CUSTOMER_INFO_URL = "https://zlhd.yusong.com.cn/api/v1/customer/basic/customer/sync_customer_info.htm";
    //生产
    String SYNC_CUSTOMER_INFO_URL = "https://zudian.yusong.com.cn/api/v1/customer/basic/customer/sync_customer_info.htm";


    @Autowired
    SyncCustomerInfoMapper syncCustomerInfoMapper;

    public void scanMessage() throws Exception {

        int offset = 0, limit = 20; //这里offset 不用做累加 因为处理完就无法查找到了
        while (true) {
            List<SyncCustomerInfo> syncCustomerInfoList = syncCustomerInfoMapper.findList(PushOrderMessage.SendStatus.NOT.getValue(), offset, limit);
            if (syncCustomerInfoList.isEmpty()) {
                break;
            }
            for (SyncCustomerInfo syncCustomerInfo : syncCustomerInfoList) {
                sendMsg(syncCustomerInfo);
            }
        }
    }

    public void sendMsg(SyncCustomerInfo syncCustomerInfo) throws Exception {
        Map params = new HashMap();

        if(syncCustomerInfo != null){
            if (syncCustomerInfo.getMpOpenId() != null) {
                params.put("mpOpenId", syncCustomerInfo.getMpOpenId());
            } else {
                params.put("mpOpenId", "");
            }
            if (syncCustomerInfo.getFwOpenId() != null) {
                params.put("fwOpenId", syncCustomerInfo.getFwOpenId());
            } else {
                params.put("fwOpenId", "");
            }
            params.put("nickname", syncCustomerInfo.getNickname());
            params.put("photoPath", syncCustomerInfo.getPhotoPath());
            params.put("mobile", syncCustomerInfo.getMobile());
            params.put("fullname", syncCustomerInfo.getFullname());
            params.put("idCard", syncCustomerInfo.getIdCard());
            params.put("company", syncCustomerInfo.getCompany());
            params.put("batteryType", syncCustomerInfo.getBatteryType());
            params.put("idCardFace", syncCustomerInfo.getIdCardFace());
            params.put("idCardRear", syncCustomerInfo.getIdCardRear());
        }

            boolean success = sendDate(params);
        if (success) {
            //发送成功更新状态
            syncCustomerInfoMapper.complete(syncCustomerInfo.getId(), new Date(), SyncCustomerInfo.SendStatus.OK.getValue(), syncCustomerInfo.getResendNum());
        } else {
            //发送失败
            log.error("syncCustomerInfo error");
            if (syncCustomerInfo.getResendNum() == null) {
                syncCustomerInfo.setResendNum(1);
                //更新状态，第一次发送失败，重发次数为1
                syncCustomerInfoMapper.complete(syncCustomerInfo.getId(), new Date(), SyncCustomerInfo.SendStatus.NOT.getValue(), syncCustomerInfo.getResendNum());
            } else if (syncCustomerInfo.getResendNum() != null && syncCustomerInfo.getResendNum() > 2) {
                //更新状态，失败此时大于2次，设置为发送失败
                syncCustomerInfoMapper.complete(syncCustomerInfo.getId(), new Date(), SyncCustomerInfo.SendStatus.FAIL.getValue(), syncCustomerInfo.getResendNum());
            } else {
                //更新状态，失败此时少于3次，重发次数加1
                syncCustomerInfo.setResendNum(syncCustomerInfo.getResendNum() + 1);
                syncCustomerInfoMapper.complete(syncCustomerInfo.getId(), new Date(), SyncCustomerInfo.SendStatus.NOT.getValue(), syncCustomerInfo.getResendNum());
            }
        }

    }

    private boolean sendDate(Map params){
        boolean flag = false;
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        OkHttpClientUtils.HttpResp httpResp = null;
        try {
            String url = SYNC_CUSTOMER_INFO_URL;
            httpResp = OkHttpClientUtils.post(url, YhdgUtils.encodeJson(params),header);
            if(httpResp != null && httpResp.status == 200) {
                log.debug("invoke success ","骑手信息同步成功");
                flag = true;
            } else {
                log.debug("invoke fail {}", httpResp);
            }
        } catch (Exception e) {
            log.error("发送失败：", e);
        }
        return  flag;
    }
}
