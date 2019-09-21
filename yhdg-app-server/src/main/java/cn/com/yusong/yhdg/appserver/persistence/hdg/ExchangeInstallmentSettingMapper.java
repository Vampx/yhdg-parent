package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentSetting;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;


public interface ExchangeInstallmentSettingMapper extends MasterMapper {

    ExchangeInstallmentSetting find(long id);

    ExchangeInstallmentSetting findByMobile(@Param("mobile") String mobile);

    int update(@Param("id") Long id,@Param("foregiftMoney") String foregiftMoney,@Param("packetMoney") String packetMoney);

}
