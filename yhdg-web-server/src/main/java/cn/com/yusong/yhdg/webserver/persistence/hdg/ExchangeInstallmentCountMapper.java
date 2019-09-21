package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentCount;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentCountDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExchangeInstallmentCountMapper extends MasterMapper {



	int findPageCount(ExchangeInstallmentCount installmentCount);

	List<ExchangeInstallmentCount> findPageResult(ExchangeInstallmentCount installmentCount);

	ExchangeInstallmentCount find(long id);

	List<ExchangeInstallmentCount> findSettingId(Long settingId);

	int insert(ExchangeInstallmentCount installmentCount);

	int update(ExchangeInstallmentCount installmentCount);

	int delete(@Param("id") long id);

}
