package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentCabinet;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface ExchangeInstallmentCabinetMapper extends MasterMapper {

	ExchangeInstallmentCabinet findCabinetId(String cabinetId);

	List<ExchangeInstallmentCabinet> findSettingId(Long settingId);



}
