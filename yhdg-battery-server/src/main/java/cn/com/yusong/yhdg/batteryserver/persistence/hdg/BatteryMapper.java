package cn.com.yusong.yhdg.batteryserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface BatteryMapper extends MasterMapper {
    Battery findByCode(@Param("code") String code);

    int insert(Battery battery);

    int update(Battery battery);

    int updateChargeStatus(@Param("code") String code, @Param("chargeStatus") Integer chargeStatus, @Param("lowVolumeNoticeTime") Date lowVolumeNoticeTime, @Param("chargeRecordId") Long chargeRecordId);

    int updateStatus(@Param("code") String code, @Param("fromStatus") Integer fromStatus, @Param("toStatus") Integer toStatus);

    int updateRescueStatus(@Param("id") String id, @Param("fromStatus") Integer fromStatus, @Param("toStatus") Integer toStatus);

    int updateWaitCharge(@Param("code") String code, @Param("chargeStatus") Integer chargeStatus);
}
