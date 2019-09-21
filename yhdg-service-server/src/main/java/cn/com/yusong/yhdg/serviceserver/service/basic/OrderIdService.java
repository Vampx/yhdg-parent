package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.OrderId;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.OrderIdMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.GregorianCalendar;

@Service
public class OrderIdService {

    @Autowired
    OrderIdMapper orderIdMapper;

    public void clean() {
        Calendar calendar = new GregorianCalendar();
        int suffix = calendar.get(Calendar.YEAR);

        for(OrderId.OrderIdType orderType : OrderId.OrderIdType.values()) {
            //容错，先创建当年表
            orderIdMapper.create(orderType.getValue(), suffix);

            long max = orderIdMapper.max(orderType.getValue(), suffix);
            if(max > 0) {
                orderIdMapper.delete(orderType.getValue(), suffix, max);
            }
            orderIdMapper.drop(orderType.getValue(), suffix - 1);
            orderIdMapper.create(orderType.getValue(), suffix + 1);
        }
    }
}
