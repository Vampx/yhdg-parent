package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryChargeRecordFault;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface BatteryChargeRecordFaultMapper extends MasterMapper {
    List<BatteryChargeRecordFault> findPageResult(BatteryChargeRecordFault search);
}
