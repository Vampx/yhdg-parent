package cn.com.yusong.yhdg.serviceserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.basic.PushMessage;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.serviceserver.persistence.hdg.BespeakOrderMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.hdg.CabinetBoxMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.hdg.CabinetMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.hdg.FaultLogMapper;
import cn.com.yusong.yhdg.serviceserver.service.AbstractService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class BespeakOrderService extends AbstractService {
    final static Logger log = LogManager.getLogger(BespeakOrderService.class);

    @Autowired
    BespeakOrderMapper bespeakOrderMapper;
    @Autowired
    CabinetBoxMapper cabinetBoxMapper;

    public void expire() {
        log.debug("预约订单成功->已过期更新开始...");
        int  offset = 0,limit = 1000;
        while (true) {
            List<BespeakOrder> orderList = bespeakOrderMapper.findExpireList(BespeakOrder.Status.SUCCESS.getValue(), new Date(), offset, limit);
            if(orderList.isEmpty()){
                break;
            }
            for(BespeakOrder order : orderList){
                int result = bespeakOrderMapper.updateExpiredOrder(order.getId(), BespeakOrder.Status.SUCCESS.getValue(), BespeakOrder.Status.EXPIRE.getValue());
                if(result > 0 ){
                    cabinetBoxMapper.updateStatus(order.getBespeakCabinetId(), order.getBespeakBoxNum(), CabinetBox.BoxStatus.BESPEAK.getValue(), CabinetBox.BoxStatus.FULL.getValue());
                }
            }
           // offset += limit;
        }
        log.debug("预约订单成功->已过期更新开始...");
    }
}
