package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryCellModel;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.entity.tree.Node;
import cn.com.yusong.yhdg.common.entity.tree.NodeModel;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.GeoHashUtils;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentCompanyCustomerMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentCompanyMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.BatteryOrderMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.PacketPeriodOrderMapper;
import cn.com.yusong.yhdg.webserver.persistence.zd.RentPeriodOrderMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

@Service
public class AgentCompanyService extends AbstractService {
	static Logger log = LoggerFactory.getLogger(AgentCompanyService.class);

	@Autowired
	AgentCompanyMapper agentCompanyMapper;
	@Autowired
	AgentCompanyCustomerMapper agentCompanyCustomerMapper;
	@Autowired
	BatteryOrderMapper batteryOrderMapper;
	@Autowired
	PacketPeriodOrderMapper packetPeriodOrderMapper;
	@Autowired
	RentPeriodOrderMapper rentPeriodOrderMapper;
	@Autowired
	AreaCache areaCache;

	/**
	 * 查询分页
	 *
	 * @param search
	 * @return
	 */
	public Page findPage(AgentCompany search) {
		Page page = search.buildPage();
		page.setTotalItems(agentCompanyMapper.findPageCount(search));
		search.setBeginIndex(page.getOffset());
		List<AgentCompany> list = agentCompanyMapper.findPageResult(search);
		for (AgentCompany agentCompany : list) {
			if (agentCompany.getAgentId() != null) {
				AgentInfo agentInfo = findAgentInfo(agentCompany.getAgentId());
				if (agentInfo != null) {
					agentCompany.setAgentName(agentInfo.getAgentName());
				}
			}
		}
		page.setResult(list);
		return page;
	}

	public String findMaxId() {
		String date = DateFormatUtils.format(new Date(), "yyyyMMdd");
		String id = agentCompanyMapper.findMaxId(date);
		if (StringUtils.isEmpty(id)) {
			id = String.format("%s%0" + Constant.CABINET_ID_SEQUENCE_LENGTH + "d", date, 1);
		} else {
			String num = id.substring(id.length() - Constant.CABINET_ID_SEQUENCE_LENGTH);
			if (Long.parseLong(num) >= Math.pow(10, Constant.CABINET_ID_SEQUENCE_LENGTH) - 1) {
				throw new RuntimeException("今日运营公司数量已达到上限");
			}

			id = String.valueOf(Long.parseLong(id) + 1);
		}
		return id;
	}

	public List<AgentCompany> findAll() {
		return agentCompanyMapper.findAll();
	}

	public AgentCompany find(String id) {
		AgentCompany agentCompany = agentCompanyMapper.find(id);
		return agentCompany;
	}

	public ExtResult insert(AgentCompany agentCompany) {
		if (agentCompany.getId() == null) {
			agentCompany.setId(findMaxId());
		}
		if (agentCompany.getProvinceId() == null) {
			agentCompany.setProvinceId(Constant.PROVINCE_ID);
		}
		if (agentCompany.getCityId() == null) {
			agentCompany.setCityId(Constant.CITY_ID);
		}
		if (agentCompany.getDistrictId() == null) {
			agentCompany.setDistrictId(Constant.DISTRICT_ID);
		}
		if (agentCompany.getLng() == null) {
			agentCompany.setLng(Constant.LNG);
		}
		if (agentCompany.getLat() == null) {
			agentCompany.setLat(Constant.LAT);
		}
		agentCompany.setBalance(0);
		if (agentCompany.getWorkTime().equals("-")) {
			agentCompany.setWorkTime(null);
		}
		agentCompany.setCreateTime(new Date());
		if (agentCompanyMapper.insert(agentCompany) == 0) {
			return DataResult.failResult("对不起! 保存失败", null);
		}else{
			updateNewLocation(agentCompany);
		}
		return DataResult.successResult("操作成功",agentCompany.getId());
	}

	public ExtResult update(AgentCompany agentCompany) {
		AgentCompany entity = agentCompanyMapper.find(agentCompany.getId());
		if (entity == null) {
			return ExtResult.failResult("记录不存在");
		}
		if (agentCompany.getWorkTime().equals("-")) {
			agentCompany.setWorkTime(null);
		}
		if (agentCompanyMapper.update(agentCompany) >= 1) {
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("修改失败");
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult delete(String id) {
		List<AgentCompanyCustomer> agentCompanyCustomerList = agentCompanyCustomerMapper.findByCompanyId(id);
		if (agentCompanyCustomerList.size() > 0) {
			return ExtResult.failResult("运营公司存在骑手，无法删除！");
		}
		int batteryOrderCount = batteryOrderMapper.findCountByAgentCompany(id);
		if (batteryOrderCount > 0) {
			return ExtResult.failResult("存在换电订单，无法删除");
		}
		int packetPeriodOrderCount = packetPeriodOrderMapper.findCountByAgentCompany(id);
		if (packetPeriodOrderCount > 0) {
			return ExtResult.failResult("存在换电包时段订单，无法删除");
		}
		int rentPeriodOrderCount = rentPeriodOrderMapper.findCountByAgentCompany(id);
		if (rentPeriodOrderCount > 0) {
			return ExtResult.failResult("存在租电包时段订单，无法删除");
		}
		if (agentCompanyMapper.delete(id) == 0) {
			return ExtResult.failResult("删除失败！");
		}
		return ExtResult.successResult();
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult updateRatio(String id, Integer companyRatio, Integer keepShopRatio, Integer companyFixedMoney, Integer ratioBaseMoney) {
		int total = agentCompanyMapper.updateRatio(id, companyRatio, keepShopRatio, companyFixedMoney, ratioBaseMoney);
		if (total == 0) {
			return ExtResult.failResult("修改分成方式失败！");
		} else {
			return ExtResult.successResult();
		}
	}

	public ExtResult updateNewLocation(AgentCompany agentCompany) {
		AgentCompany entity = agentCompanyMapper.find(agentCompany.getId());
		if (entity == null) {
			return ExtResult.failResult("记录不存在");
		}
		if (agentCompany.getProvinceName() != null) {
			if (agentCompany.getProvinceName().substring(agentCompany.getProvinceName().length() - 1).equals("市")) {
				agentCompany.setProvinceName(agentCompany.getProvinceName().substring(0, agentCompany.getProvinceName().length() - 1));
			}
			Area area = areaCache.getByName(agentCompany.getProvinceName());
			if (area != null) {
				agentCompany.setProvinceId(Integer.valueOf(area.getAreaCode()));
			}
		}
		if (agentCompany.getCityName() != null) {
			Area area = areaCache.getByName(agentCompany.getCityName());
			if (area != null) {
				agentCompany.setCityId(Integer.valueOf(area.getAreaCode()));
			}
		}
		if (agentCompany.getDistrictName() != null) {
			Area area = areaCache.getByName(agentCompany.getDistrictName());
			if (area != null) {
				agentCompany.setDistrictId(Integer.valueOf(area.getAreaCode()));
			}
		}
		agentCompany.setGeoHash(GeoHashUtils.getGeoHashString(agentCompany.getLng(), agentCompany.getLat()));
		String street = null;
		if (agentCompany.getStreetName() != null) {
			street = agentCompany.getStreetName();
		}
		if (agentCompany.getStreetNumber() != null) {
			street = agentCompany.getStreetNumber();
		}
		if (agentCompany.getStreetName() != null && agentCompany.getStreetNumber() != null) {
			street = agentCompany.getStreetName() + agentCompany.getStreetNumber();
		}
		agentCompany.setStreet(street);
		agentCompany.setKeyword(agentCompany.getAddress() + entity.getCompanyName());
		int effect = agentCompanyMapper.update(agentCompany);
		if (effect == 0) {
			return ExtResult.failResult("修改失败！");
		}
		return ExtResult.successResult("操作成功");
	}

	public void tree(Set<Integer> checked, String dummy, Integer agentId, ServletOutputStream stream) throws IOException {

		List<AgentCompany> list = agentCompanyMapper.findByAgent(agentId);
		if (agentId == null && list.size() == ConstEnum.Flag.FALSE.getValue()) {
			list = agentCompanyMapper.findAll();
		}
		List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
		for (AgentCompany agentCompany : list) {
			NodeModel nodeModel = new NodeModel();
			Node<NodeModel> root = new Node<NodeModel>(nodeModel);
			nodeModel.setId(agentCompany.getId());
			nodeModel.setName(agentCompany.getCompanyName());
			roots.add(root);
		}

		ObjectMapper objectMapper = new ObjectMapper();
		JsonGenerator json = objectMapper.getJsonFactory().createJsonGenerator(stream, JsonEncoding.UTF8);
		json.writeStartArray();
		AppUtils.writeJson(roots, json);
		json.writeEndArray();
		json.flush();
		json.close();
	}

	public void tree(String dummy, Integer agentId, OutputStream stream) throws IOException {
		tree(buildTree(dummy, agentId), stream);
	}

	private List<Node<NodeModel>> buildTree(String dummy, Integer agentId) {
		Set<String> checked = Collections.emptySet();
		List<AgentCompany> agentCompanyList = agentCompanyMapper.findByAgent(agentId);
		if (agentCompanyList.size() == ConstEnum.Flag.FALSE.getValue()) {
			agentCompanyList = agentCompanyMapper.findAll();
		}
		List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
		if (StringUtils.isNotEmpty(dummy)) {
			NodeModel data = new NodeModel();
			Node<NodeModel> root = new Node<NodeModel>(data);
			data.setId("");
			data.setName(dummy);
			roots.add(root);

			for (AgentCompany topAgentCompany : agentCompanyList) {
				NodeModel nodeModel = new NodeModel();
				Node<NodeModel> node = new Node<NodeModel>(nodeModel, root);

				nodeModel.setId(topAgentCompany.getId());
				nodeModel.setName(topAgentCompany.getCompanyName());
				nodeModel.setCheckStatus(checked.contains(topAgentCompany.getId()) ? NodeModel.CheckedStatus.checked : NodeModel.CheckedStatus.unchecked);
			}

		} else {
			for (AgentCompany topAgentCompany : agentCompanyList) {
				NodeModel nodeModel = new NodeModel();
				Node<NodeModel> node = new Node<NodeModel>(nodeModel);

				nodeModel.setId(topAgentCompany.getId());
				nodeModel.setCheckStatus(checked.contains(topAgentCompany.getId()) ? NodeModel.CheckedStatus.checked : NodeModel.CheckedStatus.unchecked);
				roots.add(node);
			}
		}
		return roots;
	}

	private void tree(List<Node<NodeModel>> roots, OutputStream stream) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonGenerator json = objectMapper.getJsonFactory().createJsonGenerator(stream, JsonEncoding.UTF8);
		json.writeStartArray();
		AppUtils.writeJson(roots, json);

		json.writeEndArray();
		json.flush();
		json.close();
	}

	public void updatePayPeople(String id, String payPeopleName, String payPeopleMpOpenId, String payPeopleFwOpenId, String payPeopleMobile, String payPassword) {
		agentCompanyMapper.updatePayPeople(id, payPeopleName, payPeopleMpOpenId, payPeopleFwOpenId, payPeopleMobile, payPassword);
	}
}
