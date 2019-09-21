package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetBox;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CabinetBoxMapper extends MasterMapper {
    CabinetBox find(@Param("cabinetId") String cabinetId, @Param("boxNum") String boxNum);
    List<CabinetBox> findByCabinetId(String cabinetId);
    CabinetBox findByBatteryId(String batteryId);
    int updateChargeFullVolume(@Param("cabinetId") String cabinetId, @Param("chargeFullVolume") Integer chargeFullVolume);
    int updateBoxActive(@Param("cabinetId") String cabinetId, @Param("boxNum") String boxNum, @Param("isActive") int isActive, @Param("forbiddenCause")String forbiddenCause, @Param("operator") String operator, @Param("operatorTime") Date operatorTime);
    int unlockBox(@Param("cabinetId") String cabinetId, @Param("boxNum") String boxNum, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus);
    int statsBoxCountByStatus(@Param("cabinetId") String cabinetId);
    int statsCompleteChargeCount(@Param("cabinetId") String cabinetId, @Param("batteryStatus") int batteryStatus);
}