package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.UnregisterBatteryReport;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UnregisterBatteryReportMapper extends HistoryMapper {
    String findTableExist(@Param("tableName") String tableName);

    UnregisterBatteryReport find(UnregisterBatteryReport unregisterBatteryReport);

    int findPageCount(UnregisterBatteryReport unregisterBatteryReport);

    List<UnregisterBatteryReport> findPageResult(UnregisterBatteryReport unregisterBatteryReport);
}
