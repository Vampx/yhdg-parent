package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryCell;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryCellModel;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BatteryCellMapper extends MasterMapper {

	BatteryCell find(@Param("id") long id);

	BatteryCell findByBarcode(@Param("barcode") String barcode);

	List<BatteryCell> findByBatteryId(@Param("batteryId") String batteryId);

	int findEmptyPageCount(BatteryCell batteryCell);

	List<BatteryCell> findEmptyPageResult(BatteryCell batteryCell);

	int findPageCount(BatteryCell batteryCell);

	List<BatteryCell> findPageResult(BatteryCell batteryCell);

	int insert(BatteryCell batteryCell);

	int update(BatteryCell batteryCell);

	int unbind(@Param("id") long id);

	int unbindByBatteryId(@Param("batteryId") String batteryId);

	int bindBattery(@Param("id") long id, @Param("batteryId") String batteryId);

}
