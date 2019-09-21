package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentCabinet;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentCount;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExchangeInstallmentCabinetMapper extends MasterMapper {

	ExchangeInstallmentCabinet findCabinetId(ExchangeInstallmentCabinet cabinet);

	int insert(ExchangeInstallmentCabinet cabinet);

	int update(ExchangeInstallmentCabinet cabinet);

	int deleteCabinetId(ExchangeInstallmentCabinet cabinet);

	List<ExchangeInstallmentCabinet> findSettingId(Long settingId);

}
