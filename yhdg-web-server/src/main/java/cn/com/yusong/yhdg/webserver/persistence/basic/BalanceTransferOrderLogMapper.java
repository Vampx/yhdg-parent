package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.BalanceTransferOrderLog;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BalanceTransferOrderLogMapper extends MasterMapper {
    List<BalanceTransferOrderLog> findByOrderId(@Param("orderId") String orderId);
    int insert(BalanceTransferOrderLog log);
}
