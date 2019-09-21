package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryFormat;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BatteryFormatMapper extends MasterMapper {
    BatteryFormat find(long id);
    List<BatteryFormat> findByCellModelId(@Param("cellModelId") long cellModelId);
    int findPageCount(BatteryFormat batteryFormat);
    List<BatteryFormat> findPageResult(BatteryFormat batteryFormat);
    int insert(BatteryFormat batteryFormat);
    int update(BatteryFormat batteryFormat);
    int delete(long id);
}
