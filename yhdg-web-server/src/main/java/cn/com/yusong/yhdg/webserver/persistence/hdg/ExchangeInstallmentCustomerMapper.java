package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentCount;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentCustomer;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExchangeInstallmentCustomerMapper extends MasterMapper {


	int findPageCount(ExchangeInstallmentCustomer exchangeInstallmentCustomer);

	List<ExchangeInstallmentCustomer> findPageResult(ExchangeInstallmentCustomer exchangeInstallmentCustomer);

	ExchangeInstallmentCustomer findCustomerMobile(String customerMobile);

	int insert(ExchangeInstallmentCustomer customer);

	int update(ExchangeInstallmentCustomer customer);

	int deleteCustomerMobile(ExchangeInstallmentCustomer customer);

	List<ExchangeInstallmentCustomer> findSettingId (long settingId);

}
