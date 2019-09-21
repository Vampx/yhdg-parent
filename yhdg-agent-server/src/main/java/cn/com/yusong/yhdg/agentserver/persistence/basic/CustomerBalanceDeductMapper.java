package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerBalanceDeduct;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface CustomerBalanceDeductMapper extends MasterMapper {
    public CustomerBalanceDeduct find(int id);
    public int findPageCount(CustomerBalanceDeduct search);
    public List<CustomerBalanceDeduct> findPageResult(CustomerBalanceDeduct search);
    public int insert(CustomerBalanceDeduct search);
}
