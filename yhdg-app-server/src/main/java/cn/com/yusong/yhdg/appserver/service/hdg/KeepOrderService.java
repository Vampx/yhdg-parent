package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.KeepOrderMapper;
import cn.com.yusong.yhdg.common.domain.hdg.KeepOrder;
import cn.com.yusong.yhdg.common.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/11.
 */
@Service
public class KeepOrderService extends AbstractService {
        @Autowired
        KeepOrderMapper keepOrderMapper;
        public List<KeepOrder> findTakeList(String takeOrderId){
                return keepOrderMapper.findTakeOrder(takeOrderId);
        }

        public List<KeepOrder> findPutList(String putOrderId){
            return keepOrderMapper.findPutOrder(putOrderId);
        }
}
