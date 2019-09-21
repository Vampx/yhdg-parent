package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentCabinet;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentStation;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface ExchangeInstallmentStationMapper extends MasterMapper {

	ExchangeInstallmentStation findStationId(ExchangeInstallmentStation cabinet);

	int insert(ExchangeInstallmentStation cabinet);

	int update(ExchangeInstallmentStation cabinet);

	int deleteStationId(ExchangeInstallmentStation cabinet);

	List<ExchangeInstallmentStation> findSettingId(Long settingId);

}
