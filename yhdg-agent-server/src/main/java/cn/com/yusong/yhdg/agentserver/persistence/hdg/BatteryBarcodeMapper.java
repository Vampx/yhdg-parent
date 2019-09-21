package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryBarcode;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BatteryBarcodeMapper extends MasterMapper {

	String findMaxCode(@Param("batteryFormatId") long batteryFormatId);

	BatteryBarcode findMaxCodeBatteryBarcode(@Param("batteryFormatId") long batteryFormatId);

	BatteryBarcode findByBarcode(@Param("barcode") String barcode);

	List<BatteryBarcode> findList(@Param("batteryFormatId") long batteryFormatId);

	int findPageCount(BatteryBarcode batteryBarcode);

	List<BatteryBarcode> findPageResult(BatteryBarcode batteryBarcode);

	int insert(BatteryBarcode batteryBarcode);

	int delete(@Param("id") long id);

}
