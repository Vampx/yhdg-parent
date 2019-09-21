package cn.com.yusong.yhdg.staticserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetBox;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface CabinetBoxMapper extends MasterMapper {
    public CabinetBox findOneFull(@Param("fullStatus") int fullStatus, @Param("batteryStatus") int batteryStatus, @Param("cabinetId") String cabinetId, @Param("batteryId") String batteryId, @Param("batteryType") int batteryType, @Param("bespeakBoxNum") String bespeakBoxNum);

    public CabinetBox find(@Param("cabinetId") String cabinetId, @Param("boxNum") String boxNum);

    public int findFullCount(@Param("boxStatus") int boxStatus, @Param("batteryStatus") int batteryStatus, @Param("cabinetId") String cabinetId);

    public int lockBox(@Param("cabinetId") String cabinetId, @Param("boxNum") String boxNum, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus, @Param("lockTime") Date lockTime);

    public int unlockBox(@Param("cabinetId") String cabinetId, @Param("boxNum") String boxNum, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus);
}