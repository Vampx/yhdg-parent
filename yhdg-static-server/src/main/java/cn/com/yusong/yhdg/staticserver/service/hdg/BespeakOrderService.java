package cn.com.yusong.yhdg.staticserver.service.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.staticserver.persistence.hdg.BespeakOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by ruanjian5 on 2017/11/24.
 */
@Service
public class BespeakOrderService {

    @Autowired
    BespeakOrderMapper bespeakOrderMapper;


    public BespeakOrder find(String id) {
        return bespeakOrderMapper.find(id);
    }

    public BespeakOrder findSuccessByCustomer(long customerId) {
        return bespeakOrderMapper.findSuccessByCustomer(customerId, BespeakOrder.Status.SUCCESS.getValue());
    }

}
