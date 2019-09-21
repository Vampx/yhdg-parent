package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.agentserver.persistence.basic.AgentCompanyCustomerMapper;
import cn.com.yusong.yhdg.agentserver.persistence.basic.AgentCompanyMapper;
import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompany;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyCustomer;
import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AgentCompanyCustomerService extends AbstractService {
	static Logger log = LoggerFactory.getLogger(AgentCompanyCustomerService.class);

	@Autowired
	AgentCompanyCustomerMapper agentCompanyCustomerMapper;
	@Autowired
	AgentCompanyMapper agentCompanyMapper;
	@Autowired
	CustomerMapper customerMapper;
	/**
	 * 查询分页
	 *
	 * @param search
	 * @return
	 */
	public Page findPage(AgentCompanyCustomer search) {
		Page page = search.buildPage();
		page.setTotalItems(agentCompanyCustomerMapper.findPageCount(search));
		search.setBeginIndex(page.getOffset());
		List<AgentCompanyCustomer> list = agentCompanyCustomerMapper.findPageResult(search);
		for (AgentCompanyCustomer agentCompanyCustomer : list) {
			if (agentCompanyCustomer.getAgentCompanyId() != null) {
				AgentCompany agentCompany = agentCompanyMapper.find(agentCompanyCustomer.getAgentCompanyId());
				if (agentCompany != null) {
					agentCompanyCustomer.setAgentCompanyName(agentCompany.getCompanyName());
				}
			}
			if (agentCompanyCustomer.getAgentId() != null) {
				AgentInfo agentInfo = findAgentInfo(agentCompanyCustomer.getAgentId());
				if (agentInfo != null) {
					agentCompanyCustomer.setAgentName(agentInfo.getAgentName());
				}
			}
		}
		page.setResult(list);
		return page;
	}

	public Page findUnbindCompanyPage(Customer search) {
		Page page = search.buildPage();
		page.setTotalItems(customerMapper.findPageCount(search));
		search.setBeginIndex(page.getOffset());
		List<Customer> list = customerMapper.findPageResult(search);
		for (Customer customer : list) {
			if (customer.getAgentId() != null) {
				AgentInfo agentInfo = findAgentInfo(customer.getAgentId());
				if (agentInfo != null) {
					customer.setAgentName(agentInfo.getAgentName());
				}
			}
		}
		page.setResult(list);
		return page;
	}

	public ExtResult insert(AgentCompanyCustomer agentCompanyCustomer) {
		agentCompanyCustomer.setCreateTime(new Date());
		customerMapper.bindCompany(agentCompanyCustomer.getCustomerId(), agentCompanyCustomer.getAgentCompanyId());
		agentCompanyCustomerMapper.insert(agentCompanyCustomer);
		return ExtResult.successResult();
	}

	@Transactional(rollbackFor = Throwable.class)
	public int bindCustomer(Long customerId, String agentCompanyId, Integer agentId, String agentName) {
		Customer customer = customerMapper.find(customerId);
		customerMapper.bindCompany(customerId, agentCompanyId);
		AgentCompanyCustomer agentCompanyCustomer = new AgentCompanyCustomer();
		agentCompanyCustomer.setAgentCompanyId(agentCompanyId);
		agentCompanyCustomer.setAgentId(agentId);
		agentCompanyCustomer.setAgentName(agentName);
		agentCompanyCustomer.setCustomerId(customerId);
		agentCompanyCustomer.setCustomerFullname(customer.getFullname());
		agentCompanyCustomer.setCustomerMobile(customer.getMobile());
		agentCompanyCustomer.setCreateTime(new Date());
		return agentCompanyCustomerMapper.insert(agentCompanyCustomer);
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult delete(String agentCompanyId, Long customerId) {
		customerMapper.clearAgentCompanyId(customerId);
		if (agentCompanyCustomerMapper.delete(agentCompanyId, customerId) == 0) {
			return ExtResult.failResult("删除失败！");
		}
		return ExtResult.successResult();
	}

}
