
package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class CustomerService extends AbstractService {
    @Autowired
    CustomerMapper customerMapper;

    public Customer find(long id) {
        return customerMapper.find(id);
    }

    public Customer findByMobile(Integer partnerId, String mobile) {
        return customerMapper.findByMobile(partnerId, mobile);
    }

    public List<Customer> findList(Integer agentId, String keyword, int offset, int limit) {
        return customerMapper.findList(agentId, keyword, offset, limit);
    }

    public List<Customer> findListOrderByForegift(Integer agentId, String keyword, int offset, int limit) {
        return customerMapper.findListOrderByForegift(agentId, keyword, offset, limit);
    }

    public List<Customer> findAgentCompanyCustomer(Integer agentId, String agentCompanyId, String keyword, int offset, int limit) {
        return customerMapper.findAgentCompanyCustomer(agentId, agentCompanyId, keyword, offset, limit);
    }

    public int findCustomerCount(Integer agentId) {
        return customerMapper.findCustomerCount(agentId);
    }

    public int findHdCustomerCountByStatus(Integer agentId, Integer status, Date beginTime, Date endTime) {
        return customerMapper.findHdCustomerCountByStatus(agentId, status, beginTime, endTime);
    }

    public int findZdCustomerCountByStatus(Integer agentId, Integer status, Date beginTime, Date endTime) {
        return customerMapper.findZdCustomerCountByStatus(agentId, status, beginTime, endTime);
    }

    public RestResult findMobileList(Integer agentId, Integer partnerId, String mobile, int offset, int limit) {
        List<Customer> list = customerMapper.findMobileList(agentId, partnerId, mobile,null, offset, limit);

        List<Map> data = new ArrayList<Map>();
        for (Customer customer : list) {
            NotNullMap line = new NotNullMap();
            line.put("id", customer.getId());
            line.put("fullname", customer.getFullname());
            line.put("mobile", customer.getMobile());
            line.put("mpOpenId", customer.getMpOpenId());
            line.put("fwOpenId", customer.getFwOpenId());
            data.add(line);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    public RestResult findMobileInfo(Integer partnerId, String mobile) {
        Customer customer = customerMapper.findByMobile(partnerId, mobile);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "该客户未实名认证，请先认证。");
        }
        Map line = new HashMap();
        line.put("id", customer.getId());
        line.put("fullname", customer.getFullname());
        line.put("mobile", customer.getMobile());
        line.put("mpOpenId", customer.getMpOpenId());
        line.put("fwOpenId", customer.getFwOpenId());

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, line);
    }

}


