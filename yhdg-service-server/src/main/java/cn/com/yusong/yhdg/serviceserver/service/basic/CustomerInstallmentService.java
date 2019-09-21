package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecordPayDetail;
import cn.com.yusong.yhdg.common.domain.basic.CustomerNoticeMessage;
import cn.com.yusong.yhdg.common.domain.basic.PushMessage;
import cn.com.yusong.yhdg.common.domain.basic.PushMetaData;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.*;
import cn.com.yusong.yhdg.serviceserver.persistence.hdg.PacketPeriodOrderMapper;
import cn.com.yusong.yhdg.serviceserver.service.AbstractService;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class CustomerInstallmentService extends AbstractService {
    final static Logger log = LogManager.getLogger(CustomerInstallmentService.class);

    @Autowired
    CustomerInstallmentRecordPayDetailMapper customerInstallmentRecordPayDetailMapper;
    @Autowired
    SystemConfigMapper systemConfigMapper;
    @Autowired
    PushMetaDataMapper pushMetaDataMapper;
    @Autowired
    CustomerNoticeMessageMapper customerNoticeMessageMapper;

    public void willExpirePush() {
        //查询到期时间
        Date nowDate = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addDays(new Date(), 1), Calendar.DAY_OF_MONTH),-1);
        Date expireDate = DateUtils.addDays(nowDate, 3);

        log.debug("要到期的分期推送...");
        int  offset = 0,limit = 1000;

        while (true) {
            List<CustomerInstallmentRecordPayDetail> orderList = customerInstallmentRecordPayDetailMapper.findWillExpire(CustomerInstallmentRecordPayDetail.Status.WAIT_PAY.getValue(), expireDate, offset,  limit);
            if(orderList.isEmpty()){
                break;
            }
            for(CustomerInstallmentRecordPayDetail detail : orderList){
                //如果当天有推送，不再推送
                CustomerNoticeMessage customerNoticeMessage = customerNoticeMessageMapper.findByToday(detail.getCustomerId(), CustomerNoticeMessage.Type.CUSTOMER_INSTALLMENT_EXPIRE.getValue());
                if(customerNoticeMessage != null){
                    continue;
                }

                //推送客户
                PushMetaData pushMetaData = new PushMetaData();
                pushMetaData.setSourceType(PushMessage.SourceType.CUSTOMER_INSTALLMENT_EXPIRE.getValue());
                pushMetaData.setSourceId(detail.getId().toString());
                pushMetaData.setCreateTime(new Date());
                pushMetaDataMapper.insert(pushMetaData);
            }

            offset += limit;
        }

        log.debug("要到期的分期推送结束...");
    }
}
