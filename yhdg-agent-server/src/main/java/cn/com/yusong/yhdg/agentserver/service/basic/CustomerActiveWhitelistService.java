package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerActiveWhitelistMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerActiveWhitelistService extends AbstractService {

    @Autowired
    CustomerActiveWhitelistMapper customerActiveWhitelistMapper;

    public Page findPage(Customer customer) {
        Page page = customer.buildPage();
        page.setTotalItems(customerActiveWhitelistMapper .findPageCount(customer));
        customer.setBeginIndex(page.getOffset());

        List<Customer> customerList = customerActiveWhitelistMapper .findPageResult(customer);
        page.setResult(customerList);
        return page;
    }

    public Page findPageNotWhitelist(Customer customer) {
        Page page = customer.buildPage();
        page.setTotalItems(customerActiveWhitelistMapper .findPageCountNotWhitelist(customer));
        customer.setBeginIndex(page.getOffset());

        List<Customer> customerList = customerActiveWhitelistMapper .findPageResultNotWhitelist(customer);
        page.setResult(customerList);
        return page;
    }

    public ExtResult delete(long id){
        if (customerActiveWhitelistMapper.deleteActiveWhitelist(id) != 1) {
            return ExtResult.failResult("删除失败");
        }else{
            return ExtResult.successResult();
        }
    }

    public ExtResult insert(long[] ids){
        for (long id : ids){
            customerActiveWhitelistMapper.insertActiveWhitelist(id);
        }
        return ExtResult.successResult();
    }
}
