package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 换电电池上报记录
 */
public interface BatteryReportLogMapper extends HistoryMapper {
    String findTableExist(@Param("tableName") String tableName);
    int findCount(@Param("batteryId") String batteryId, @Param("suffix") String suffix);
    int findGps(@Param("batteryId") String batteryId, @Param("suffix") String suffix);
}
