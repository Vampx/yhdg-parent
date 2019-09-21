package cn.com.yusong.yhdg.webserver.persistence.zd;


import cn.com.yusong.yhdg.common.domain.zd.RentInstallmentSetting;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RentInstallmentSettingMapper extends MasterMapper {
    RentInstallmentSetting find(@Param("id") Long id);

    List<RentInstallmentSetting> findByForegiftId(@Param("foregiftId") Long foregiftId);

    List<RentInstallmentSetting> findByPacketId(@Param("packetId") Long packetId);

    List<RentInstallmentSetting> findByInsuranceId(@Param("insuranceId") Long insuranceId);

    int findPageCount(RentInstallmentSetting battery);

    int findUnique(@Param("id") Long id, @Param("mobile") String mobile);

    List<RentInstallmentSetting> findPageResult(RentInstallmentSetting battery);

    int insert(RentInstallmentSetting battery);

    int update(RentInstallmentSetting battery);

    int delete(@Param("id") Long id);

    int deleteByForegiftId(@Param("foregiftId") Long foregiftId);

    int deleteByPacketId(@Param("packetId") Long packetId);

    int deleteByInsuranceId(@Param("insuranceId") Long insuranceId);

}
