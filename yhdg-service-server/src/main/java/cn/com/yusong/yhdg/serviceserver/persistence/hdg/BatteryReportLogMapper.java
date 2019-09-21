package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryReportLog;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BatteryReportLogMapper extends HistoryMapper {
    public int createTable(@Param("suffix") String suffix);

    public int update(@Param("suffix") String suffix, @Param("address") String address, @Param("batteryId") String batteryId, @Param("reportTime") Date reportTime);

    public List<BatteryReportLog> find(@Param("suffix") String suffix, @Param("limit") Integer limit);

}