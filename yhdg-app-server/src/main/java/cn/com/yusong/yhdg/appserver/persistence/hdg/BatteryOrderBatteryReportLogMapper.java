package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrderBatteryReportLog;
import cn.com.yusong.yhdg.common.entity.LocationInfo;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 换电电池上报记录
 */
public interface BatteryOrderBatteryReportLogMapper extends HistoryMapper {
    String findTableExist(@Param("tableName") String tableName);
    List<BatteryOrderBatteryReportLog> findListByOrderId(@Param("orderId") String orderId, @Param("suffix") String suffix, @Param("offset") int offset, @Param("limit") int limit);
}
