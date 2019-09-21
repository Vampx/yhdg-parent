package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.entity.tree.Node;
import cn.com.yusong.yhdg.common.entity.tree.NodeModel;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentBatteryTypeMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.*;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

@Service
public class AgentBatteryTypeService extends AbstractService {
	@Autowired
	AgentBatteryTypeMapper agentBatteryTypeMapper;
	@Autowired
	CabinetBatteryTypeMapper cabinetBatteryTypeMapper;
	@Autowired
	StationBatteryTypeMapper stationBatteryTypeMapper;
	@Autowired
	ExchangeBatteryForegiftMapper exchangeBatteryForegiftMapper;
	@Autowired
	PacketPeriodPriceMapper packetPeriodPriceMapper;
	@Autowired
	InsuranceMapper insuranceMapper;
	@Autowired
	ExchangePriceTimeMapper exchangePriceTimeMapper;

	public AgentBatteryType find(Integer batteryType, Integer agentId) {
		return agentBatteryTypeMapper.find(batteryType, agentId);
	}

	public List<AgentBatteryType> findListByCabinetId(String cabinetId) {
		return agentBatteryTypeMapper.findListByCabinetId(cabinetId);
	}

	public AgentBatteryType findForName(Integer batteryType, Integer agentId) {
		return agentBatteryTypeMapper.findForName(batteryType, agentId);
	}

	public List<AgentBatteryType> findBatteryListByAgentId(Integer agentId) {
		return agentBatteryTypeMapper.findBatteryListByAgentId(agentId);
	}

	public Page findPage(AgentBatteryType search) {
		Page page = search.buildPage();
		page.setTotalItems(agentBatteryTypeMapper.findPageCount(search));
		search.setBeginIndex(page.getOffset());
		List<AgentBatteryType> agentBatteryTypeList = agentBatteryTypeMapper.findPageResult(search);
		page.setResult(agentBatteryTypeList);
		return page;
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult create(AgentBatteryType search) {
		if (agentBatteryTypeMapper.find(search.getBatteryType(), search.getAgentId()) != null) {
			return ExtResult.failResult("该运营商已存在该电池类型，无法新建");
		}

		ExchangePriceTime exchangePriceTime = exchangePriceTimeMapper.findByBatteryType(search.getBatteryType(), search.getAgentId());
		if (exchangePriceTime != null) {
			exchangePriceTimeMapper.deleteByBatteryType(search.getBatteryType(), search.getAgentId());
		}
		ExchangePriceTime priceTime = new ExchangePriceTime();
		priceTime.setAgentId(search.getAgentId());
		priceTime.setBatteryType(search.getBatteryType());
		priceTime.setActiveSingleExchange(search.getActiveSingleExchange());
		priceTime.setTimesPrice(search.getTimesPrice());
		exchangePriceTimeMapper.insert(priceTime);

		agentBatteryTypeMapper.insert(search);

		return ExtResult.successResult();
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult update(AgentBatteryType search) {
		if (search.getToBatteryType() != null) {
			if (agentBatteryTypeMapper.find(search.getToBatteryType(), search.getAgentId()) != null) {
				return ExtResult.failResult("该运营商已存在该电池类型，修改失败");
			}
			if (search.getToBatteryType().intValue() != search.getBatteryType().intValue()) {
				List<ExchangeBatteryForegift> exchangeBatteryForegiftList = exchangeBatteryForegiftMapper.findByBatteryType(search.getBatteryType(), search.getAgentId());
				if (exchangeBatteryForegiftList.size() > 0) {
					return ExtResult.failResult("存在押金，无法修改电池类型");
				}
				List<PacketPeriodPrice> packetPeriodPriceList = packetPeriodPriceMapper.findListByBatteryType(search.getBatteryType(), search.getAgentId());
				if (packetPeriodPriceList.size() != 0) {
					return ExtResult.failResult("存在租金，修改失败");
				}
				List<CabinetBatteryType> cabinetBatteryTypeList = cabinetBatteryTypeMapper.findListByBatteryType(search.getBatteryType(), search.getAgentId());
				if (cabinetBatteryTypeList.size() != 0) {
					return ExtResult.failResult("电池类型已绑定换电柜，修改失败");
				}
			}
		}
		exchangePriceTimeMapper.deleteByBatteryType(search.getBatteryType(), search.getAgentId());
		ExchangePriceTime priceTime = new ExchangePriceTime();
		priceTime.setAgentId(search.getAgentId());
		priceTime.setBatteryType(search.getBatteryType());
		priceTime.setActiveSingleExchange(search.getActiveSingleExchange());
		priceTime.setTimesPrice(search.getTimesPrice());
		exchangePriceTimeMapper.insert(priceTime);

		if (agentBatteryTypeMapper.update(search.getBatteryType(), search.getTypeName(), search.getBatteryType(), search.getAgentId()) >= 1) {
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("修改失败");
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public int bindCabinet(Integer batteryType, String cabinetId) {
		CabinetBatteryType cabinetBatteryType = new CabinetBatteryType();
		cabinetBatteryType.setCabinetId(cabinetId);
		cabinetBatteryType.setBatteryType(batteryType);
		return cabinetBatteryTypeMapper.insert(cabinetBatteryType);
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult delete(Integer batteryType, Integer agentId) {
		//存在押金
		List<ExchangeBatteryForegift> exchangeBatteryForegiftList = exchangeBatteryForegiftMapper.findByBatteryType(batteryType, agentId);
		if (exchangeBatteryForegiftList.size() > 0) {
			return ExtResult.failResult("存在押金，无法删除");
		}
		List<PacketPeriodPrice> packetPeriodPriceList = packetPeriodPriceMapper.findListByBatteryType(batteryType, agentId);
		if (packetPeriodPriceList.size() != 0) {
			return ExtResult.failResult("存在租金，无法删除");
		}
		List<CabinetBatteryType> cabinetBatteryTypeList = cabinetBatteryTypeMapper.findListByBatteryType(batteryType, agentId);
		if (cabinetBatteryTypeList.size() != 0) {
			return ExtResult.failResult("电池类型已绑定换电柜，无法删除");
		}
		List<StationBatteryType> stationBatteryTypeList = stationBatteryTypeMapper.findListByBatteryType(batteryType, agentId);
		if (stationBatteryTypeList.size() != 0) {
			return ExtResult.failResult("电池类型已绑定站点，无法删除");
		}
		if (agentBatteryTypeMapper.delete(batteryType, agentId) >= 1) {
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("删除失败");
		}
	}

	public void tree(Set<Integer> checked, String dummy, Integer agentId, ServletOutputStream stream) throws IOException {

		List<AgentBatteryType> list = agentBatteryTypeMapper.findListByAgentId(agentId);
		List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
		for (AgentBatteryType agentBatteryType : list) {
			NodeModel nodeModel = new NodeModel();
			Node<NodeModel> root = new Node<NodeModel>(nodeModel);
			nodeModel.setId(agentBatteryType.getBatteryType());
			nodeModel.setName(agentBatteryType.getTypeName());
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
		List<AgentBatteryType> groupList = agentBatteryTypeMapper.findListByAgentId(agentId);

		List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
		if (StringUtils.isNotEmpty(dummy)) {
			NodeModel data = new NodeModel();
			Node<NodeModel> root = new Node<NodeModel>(data);
			data.setId("");
			data.setName(dummy);
			roots.add(root);

			for (AgentBatteryType topAgentBatteryType : groupList) {
				NodeModel nodeModel = new NodeModel();
				Node<NodeModel> node = new Node<NodeModel>(nodeModel, root);

				nodeModel.setId(topAgentBatteryType.getBatteryType());
				nodeModel.setName(topAgentBatteryType.getTypeName());
				nodeModel.setCheckStatus(checked.contains(topAgentBatteryType.getBatteryType()) ? NodeModel.CheckedStatus.checked : NodeModel.CheckedStatus.unchecked);
			}

		} else {
			for (AgentBatteryType topAgentBatteryType : groupList) {
				NodeModel nodeModel = new NodeModel();
				Node<NodeModel> node = new Node<NodeModel>(nodeModel);

				nodeModel.setId(topAgentBatteryType.getBatteryType());
				nodeModel.setCheckStatus(checked.contains(topAgentBatteryType.getBatteryType()) ? NodeModel.CheckedStatus.checked : NodeModel.CheckedStatus.unchecked);
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

}
