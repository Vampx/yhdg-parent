package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInOutMoney;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerInOutMoneyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerInOutMoneyService {

    @Autowired
    CustomerInOutMoneyMapper customerInOutMoneyMapper;
    @Autowired
    CustomerService customerService;

    public Page findPage(CustomerInOutMoney search) {
        Page page = search.buildPage();
        page.setTotalItems(customerInOutMoneyMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<CustomerInOutMoney> result = customerInOutMoneyMapper.findPageResult(search);
        for(CustomerInOutMoney customerInOutMoney:result){
            Customer customer = customerService.find(customerInOutMoney.getCustomerId());
            customerInOutMoney.setCustomerName(customer.getFullname());
        }
        page.setResult(result);
        return page;
    }

    public CustomerInOutMoney find(Long id) {
        return customerInOutMoneyMapper.find(id);
    }
}
