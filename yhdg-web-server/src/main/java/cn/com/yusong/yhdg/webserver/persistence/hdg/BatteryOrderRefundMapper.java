package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrderRefund;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BatteryOrderRefundMapper extends MasterMapper {
    int findCountByCabinet(@Param("cabinetId") String cabinetId);

    int findPageCount(BatteryOrderRefund search);

    List<BatteryOrderRefund> findPageResult(BatteryOrderRefund search);

    BatteryOrderRefund find(String id);

    int insert(BatteryOrderRefund batteryOrderRefund);

    int deleteByCustomerId(@Param("customerId") long customerId);

}
