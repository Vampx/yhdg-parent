package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentCustomer;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface ExchangeInstallmentCustomerMapper extends MasterMapper {


	ExchangeInstallmentCustomer findCustomerMobile(String customerMobile);


	List<ExchangeInstallmentCustomer> findSettingId(Long settingId);

}
