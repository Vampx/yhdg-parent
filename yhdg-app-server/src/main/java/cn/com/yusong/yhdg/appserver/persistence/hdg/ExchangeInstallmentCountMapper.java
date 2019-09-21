package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentCount;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExchangeInstallmentCountMapper extends MasterMapper {



	ExchangeInstallmentCount find(long id);

	List<ExchangeInstallmentCount> findSettingId(Long settingId);


}
