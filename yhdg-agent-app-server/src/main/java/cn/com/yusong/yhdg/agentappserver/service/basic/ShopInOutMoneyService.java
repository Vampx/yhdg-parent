package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.persistence.basic.ShopInOutMoneyMapper;
import cn.com.yusong.yhdg.common.domain.basic.ShopInOutMoney;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ShopInOutMoneyService {

    @Autowired
    ShopInOutMoneyMapper shopInOutMoneyMapper;

    public List<ShopInOutMoney> findList(String shopId, int offset, int limit){
        return shopInOutMoneyMapper.findList(shopId, offset, limit);
    }

    public List<ShopInOutMoney> findIncome(String shopId, int offset, int limit) {
        return shopInOutMoneyMapper.findIncome(shopId, ShopInOutMoney.Type.INCOME.getValue(), offset, limit);
    }

    public int findIncomeCount(String shopId) {
        return shopInOutMoneyMapper.findIncomeCount(shopId, ShopInOutMoney.Type.INCOME.getValue());
    }

}
