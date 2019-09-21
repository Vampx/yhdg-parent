package cn.com.yusong.yhdg.cabinetserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetBox;
import cn.com.yusong.yhdg.common.entity.Kv;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CabinetBoxMapper extends MasterMapper {

    CabinetBox find(@Param("cabinetId") String cabinetId, @Param("boxNum") String boxNum);

    List<CabinetBox> findBoxBatteryList(String cabinetId);

    List<CabinetBox> findListByCabinet(String cabinetId);

    CabinetBox findOneFull(@Param("fullStatus") int fullStatus, @Param("batteryStatus") int batteryStatus, @Param("cabinetId") String cabinetId,  @Param("batteryId") String batteryId, @Param("batteryType") int batteryType, @Param("bespeakBoxNum") String bespeakBoxNum);

    CabinetBox findOneEmptyBoxNum(@Param("cabinetId") String cabinetId, @Param("boxStatus") int boxStatus, @Param("isActive") int isActive, @Param("isOnline") int isOnline);

    int findEmptyCount(@Param("cabinetId") String cabinetId, @Param("boxStatus") Integer boxStatus);

    int insert(CabinetBox cabinetBox);

    int update(@Param("cabinetId") String cabinetId, @Param("boxNum") String boxNum, @Param("kvList") List<Kv> lvList);

    int updateFaultLogId(@Param("cabinetId") String cabinetId, @Param("boxNum") String boxNum, @Param("smokeFaultLogId") Long smokeFaultLogId);

    int unlockBox(@Param("cabinetId") String cabinetId, @Param("boxNum") String boxNum, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus);

    int updateOnline(@Param("cabinetId") String cabinetId, @Param("isOnline") int isOnline);

    int updateBoxStatus(@Param("cabinetId") String cabinetId, @Param("boxNum") String boxNum, @Param("boxStatus") int boxStatus);
}
