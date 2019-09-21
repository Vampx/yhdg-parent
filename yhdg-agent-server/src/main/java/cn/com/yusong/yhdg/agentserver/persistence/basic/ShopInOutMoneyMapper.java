package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentInOutMoney;
import cn.com.yusong.yhdg.common.domain.basic.ShopInOutMoney;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface ShopInOutMoneyMapper extends MasterMapper {
    ShopInOutMoney find(Long id);
    int findPageCount(ShopInOutMoney search);
    List<ShopInOutMoney> findPageResult(ShopInOutMoney search);
    int insert(ShopInOutMoney entity);
}
