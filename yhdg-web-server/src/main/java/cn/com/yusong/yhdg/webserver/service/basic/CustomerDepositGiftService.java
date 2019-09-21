package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerDepositGift;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerDepositGiftMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomerDepositGiftService {
    @Autowired
    CustomerDepositGiftMapper customerDepositGiftMapper;

    public List<CustomerDepositGift> findPartnerId(Integer partnerId) {
        return customerDepositGiftMapper.findPartnerId(partnerId);
    }

    public List<CustomerDepositGift> findAll() {
        return customerDepositGiftMapper.findAll();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult update(int[] partnerId, float[] money) {
        Set<Float> set = new HashSet<Float>();
        List list = new ArrayList();
        for (float m : money) {
            if (m > 0) {
                set.add(m);
                list.add(m);
            }
        }
        if (set.size() != list.size()) {
            return ExtResult.failResult("不能存在相同充值金额！");
        }
        customerDepositGiftMapper.delete(partnerId[0]);
        for (int i = 0; i < money.length; i++) {
            if (money[i] != 0) {
                CustomerDepositGift line = new CustomerDepositGift();
                line.setPartnerId(partnerId[i]);
                line.setMoney((int)(money[i] * 100));
               // line.setGift((int)(gift[i] * 100));
                line.setGift(0);
                customerDepositGiftMapper.insert(line);
            }
        }
        return ExtResult.successResult();
    }
}
