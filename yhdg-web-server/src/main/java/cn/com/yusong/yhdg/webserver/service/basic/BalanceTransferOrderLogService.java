package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.BalanceTransferOrderLog;
import cn.com.yusong.yhdg.webserver.persistence.basic.BalanceTransferOrderLogMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BalanceTransferOrderLogService {

    @Autowired
    BalanceTransferOrderLogMapper balanceTransferOrderLogMapper;

    public List<BalanceTransferOrderLog> findByOrderId(@Param("orderId") String orderId) {
        return balanceTransferOrderLogMapper.findByOrderId(orderId);
    }
}
