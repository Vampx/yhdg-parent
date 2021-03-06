package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetBatteryType;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CabinetBatteryTypeMapper extends MasterMapper {

	List<CabinetBatteryType> findListByCabinet(@Param("cabinetId") String cabinetId);
}
