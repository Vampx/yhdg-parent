package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentCabinet;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentStation;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface ExchangeInstallmentStationMapper extends MasterMapper {

	ExchangeInstallmentStation findStationId(String stationId);

	List<ExchangeInstallmentStation> findSettingId(Long settingId);



}
