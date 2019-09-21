package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CancelLaxinCustomer;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface CancelLaxinCustomerMapper extends MasterMapper {

    int insert(CancelLaxinCustomer cancelLaxinCustomer);
}
