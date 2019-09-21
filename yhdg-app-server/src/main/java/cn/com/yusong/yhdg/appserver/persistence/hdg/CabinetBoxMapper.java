package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetBox;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CabinetBoxMapper extends MasterMapper {
    public List<CabinetBox> findAllEmpty(@Param("cabinetId") String cabinetId,
                                     @Param("boxStatus") Integer boxStatus);

    public List<CabinetBox> findAllNotFull(@Param("cabinetId") String cabinetId,
                                              @Param("boxStatus") Integer boxStatus,
                                              @Param("batteryStatus") Integer batteryStatus);

    public List<CabinetBox> findList(@Param("cabinetId") String cabinetId);

    public CabinetBox findOneEmptyBoxNum(@Param("cabinetId") String cabinetId, @Param("boxStatus") int boxStatus, @Param("isActive") int isActive, @Param("isOnline") int isOnline, @Param("typeList") List<Integer> typeList);

    public CabinetBox findOneFull(@Param("fullStatus") int fullStatus, @Param("batteryStatus") int batteryStatus, @Param("cabinetId") String cabinetId, @Param("batteryId") String batteryId, @Param("batteryType") int batteryType, @Param("bespeakBoxNum") String bespeakBoxNum);

    public CabinetBox findOneFullByCabinet(@Param("fullStatus") int fullStatus, @Param("batteryStatus") int batteryStatus, @Param("cabinetId") String cabinetId);

    public CabinetBox find(@Param("cabinetId") String cabinetId, @Param("boxNum") String boxNum);

    public int findEmptyCount(@Param("cabinetId") String cabinetId, @Param("emptyStatus") Integer emptyStatus);

    public int findBespeakCount(@Param("cabinetId") String cabinetId, @Param("bespeakStatus") Integer bespeakStatus);

    public int findBatteryCount(@Param("cabinetId") String cabinetId);

    public int findFullCount(@Param("boxStatus") int boxStatus, @Param("batteryStatus") int batteryStatus, @Param("cabinetId") String cabinetId);

    List<CabinetBox> findByCabinet(String cabinetId);

    List<CabinetBox> findBySubcabinetId(String cabinetId);

    public int lockBox(@Param("cabinetId") String cabinetId, @Param("boxNum") String boxNum, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus, @Param("lockTime") Date lockTime);

    public int unlockBox(@Param("cabinetId") String cabinetId, @Param("boxNum") String boxNum, @Param("fromStatus") Integer fromStatus, @Param("toStatus") int toStatus);

    public int lockBoxByBackBattery(@Param("cabinetId") String cabinetId, @Param("boxNum") String boxNum, @Param("lockTime")Date lockTime, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus);

    public int updateOpenType(@Param("cabinetId") String cabinetId, @Param("boxNum") String boxNum, @Param("openType") int orderType, @Param("openerId") long openerId);

}