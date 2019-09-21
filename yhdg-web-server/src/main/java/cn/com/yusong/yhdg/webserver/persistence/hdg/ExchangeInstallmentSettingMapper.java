package cn.com.yusong.yhdg.webserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentSetting;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ExchangeInstallmentSettingMapper extends MasterMapper {
    ExchangeInstallmentSetting find(@Param("id") Long id);

    List<ExchangeInstallmentSetting> findByForegiftId(@Param("foregiftId") Long foregiftId);

    List<ExchangeInstallmentSetting> findByPacketId(@Param("packetId") Long packetId);

    List<ExchangeInstallmentSetting> findByInsuranceId(@Param("insuranceId") Long insuranceId);

    int findPageCount(ExchangeInstallmentSetting battery);

    int findUnique(@Param("id") Long id, @Param("mobile") String mobile);

    List<ExchangeInstallmentSetting> findPageResult(ExchangeInstallmentSetting battery);

    int insert(ExchangeInstallmentSetting battery);

    int update(ExchangeInstallmentSetting battery);

    int delete(@Param("id") Long id);

    int deleteByForegiftId(@Param("foregiftId") Long foregiftId);

    int deleteByPacketId(@Param("packetId") Long packetId);

    int deleteByInsuranceId(@Param("insuranceId") Long insuranceId);

}
