package cn.com.yusong.yhdg.cabinetserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CabinetMapper extends MasterMapper{
   Cabinet find(String id);
   String findMaxId(String id);
   int updateOperationFlag(@Param("id") String id,@Param("operationFlag") Integer operationFlag);
   int insert(Cabinet cabinet);
   int updateHeart(@Param("id") String id, @Param("version") String version, @Param("mac") String mac, @Param("isOnline") int isOnline, @Param("heartTime") Date heartTime, @Param("temp1") Integer temp1, @Param("temp2") Integer temp2, @Param("networkType") Integer networkType, @Param("currentSignal") Integer currentSignal, @Param("tempFaultLogId") Long tempFaultLogId, @Param("isFpOpen") Integer isFpOpen,@Param("fanSpeed")Integer fanSpeed,@Param("waterLevel")Integer waterLevel,@Param("smokeState")Integer smokeState,@Param("acVoltageState")Integer acVoltageState,@Param("acVoltage")Integer acVoltage);
   int updateOnline(@Param("id") String id, @Param("isOnline") int isOnline);
   int updatePower(@Param("id") String id, @Param("power") int power, @Param("batteryNum") int batteryNum, @Param("chargeBatteryNum") int chargeBatteryNum);
   int cleanFaultLogId(@Param("id") String id);
}
