package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryChargeRecordDetail;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BatteryChargeRecordDetailMapper extends HistoryMapper {
    List<String> findTable(@Param("tableName") String tableName);

    List<BatteryChargeRecordDetail> find(@Param("id") Long id, @Param("suffix") String suffix);

    int findPageCount(BatteryChargeRecordDetail search);

    List<BatteryChargeRecordDetail> findPageResult(BatteryChargeRecordDetail search);

}
