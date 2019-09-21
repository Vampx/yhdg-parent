package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.AgentCompanyMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.UserMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.PacketPeriodOrderMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.ShopMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.utils.GeoHashUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AgentCompanyService extends AbstractService {
	@Autowired
	AgentCompanyMapper agentCompanyMapper;
	@Autowired
	AgentMapper agentMapper;
	@Autowired
	CustomerMapper customerMapper;

	public AgentCompany find(String id) {
		return agentCompanyMapper.find(id);
	}

	public List<AgentCompany> findVipAgentCompanyList(Integer agentId, String keyword, int offset, int limit) {
		return agentCompanyMapper.findVipAgentCompanyList(agentId, keyword, offset, limit);
	}

	@Transactional(rollbackFor = Throwable.class)
	public int setPayPassword(String id, String password) {
		return agentCompanyMapper.updatePayPassword(id, password);
	}

	public Customer findCustomer(Integer agentId, String fullname, String mobile) {
		Agent agent = agentMapper.find(agentId);
		Customer customer = customerMapper.findForAgentCompany(agent.getPartnerId(), fullname, mobile);
		return customer;
	}

	public Map findDetail(String id) {
		AgentCompany agentCompany = agentCompanyMapper.find(id);
		if (agentCompany == null) {
			return null;
		}
		Map line = new HashMap();
		line.put("id", agentCompany.getId());
		line.put("address", agentCompany.getAddress());
		line.put("agentCompanyName", agentCompany.getCompanyName());
		line.put("linkname", agentCompany.getLinkname());
		line.put("tel", agentCompany.getTel());
		line.put("provinceId", agentCompany.getProvinceId());
		line.put("cityId", agentCompany.getCityId());
		line.put("districtId", agentCompany.getDistrictId());
		line.put("street", agentCompany.getStreet());
		line.put("openTime", agentCompany.getWorkTime());

		String[] imagePath = {agentCompany.getImagePath1(), agentCompany.getImagePath2(), agentCompany.getImagePath3()};
		line.put("imagePath", imagePath);

		return line;
	}

	@Transactional(rollbackFor = Throwable.class)
	public RestResult update(String id, String agentCompanyName, String linkname, String tel, String workTime) {
		AgentCompany agentCompany = new AgentCompany();
		agentCompany.setId(id);
		agentCompany.setCompanyName(agentCompanyName);
		agentCompany.setLinkname(linkname);
		agentCompany.setTel(tel);
		agentCompany.setWorkTime(workTime);
		agentCompanyMapper.updateInfo(agentCompany);
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
	}

	@Transactional(rollbackFor = Throwable.class)
	public RestResult updatePayPeople(String id, String payPeopleMobile, String payPeopleName, String payPeopleMpOpenId, String payPeopleFwOpenId) {
		AgentCompany agentCompany = new AgentCompany();
		agentCompany.setId(id);
		agentCompany.setPayPeopleMobile(payPeopleMobile);
		agentCompany.setPayPeopleName(payPeopleName);
		if (payPeopleMpOpenId != null) {
			agentCompany.setPayPeopleMpOpenId(payPeopleMpOpenId);
		}
		if (payPeopleFwOpenId != null) {
			agentCompany.setPayPeopleFwOpenId(payPeopleFwOpenId);
		}
		agentCompanyMapper.updateInfo(agentCompany);
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
	}

}
