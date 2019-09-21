package cn.com.yusong.yhdg.cabinetserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryChargeRecordDetail;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

public interface BatteryChargeRecordDetailMapper extends HistoryMapper {
    void insert(BatteryChargeRecordDetail batteryChargeRecordDetail);
    int createTable(@Param("tableName") String tableName);
}
