package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Station;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.entity.tree.Node;
import cn.com.yusong.yhdg.common.entity.tree.NodeModel;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.persistence.basic.*;
import cn.com.yusong.yhdg.webserver.persistence.hdg.StationMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StationRoleService extends AbstractService{

	@Autowired
	StationRoleMapper stationRoleMapper;
	@Autowired
	StationMenuMapper stationMenuMapper;
	@Autowired
	StationPermMapper stationPermMapper;
	@Autowired
	StationRolePermMapper stationRolePermMapper;
	@Autowired
	UserMapper userMapper;
	@Autowired
	StationMapper stationMapper;

	public StationRole find(int id) {
		StationRole stationRole = stationRoleMapper.find(id);
		Station station = stationMapper.find(stationRole.getStationId());
		if (station != null && station.getAgentId() != null) {
			AgentInfo agentInfo = findAgentInfo(station.getAgentId());
			if (agentInfo != null) {
				stationRole.setAgentName(agentInfo.getAgentName());
			}
		}
		return stationRole;
	}

	public Page findPage(StationRole search) {
		Page page = search.buildPage();
		page.setTotalItems(stationRoleMapper.findPageCount(search));
		search.setBeginIndex(page.getOffset());
		List<StationRole> list = stationRoleMapper.findPageResult(search);
		for (StationRole stationRole : list) {
			if (stationRole.getStationId() != null) {
				Station station = stationMapper.find(stationRole.getStationId());
				if (station != null) {
					stationRole.setStationName(station.getStationName());
					AgentInfo agentInfo = findAgentInfo(station.getAgentId());
					if (agentInfo != null) {
						stationRole.setAgentName(agentInfo.getAgentName());
					}
				}
			}
		}
		page.setResult(list);
		return page;
	}

	public ExtResult create(StationRole stationRole) {
		int roleNameUnique = stationRoleMapper.findUnique(stationRole.getId(), stationRole.getRoleName());
		if (roleNameUnique > 0) {
			return ExtResult.failResult("该角色名称已存在");
		}
		stationRoleMapper.insert(stationRole);
		for(String oper : stationRole.getPermList()) {
			StationRolePerm stationRolePerm = new StationRolePerm();
			stationRolePerm.setRoleId(stationRole.getId());
			stationRolePerm.setPermId(oper);
			stationRolePermMapper.insert(stationRolePerm);
		}
		return ExtResult.successResult();
	}

	public ExtResult update(StationRole stationRole) {
		int roleNameUnique = stationRoleMapper.findUnique(stationRole.getId(), stationRole.getRoleName());
		if (roleNameUnique > 0) {
			return ExtResult.failResult("该角色名称已存在");
		}
		stationRoleMapper.update(stationRole);
		stationRolePermMapper.delete(stationRole.getId());
		for (String oper : stationRole.getPermList()) {
			StationRolePerm stationRolePerm = new StationRolePerm();
			stationRolePerm.setRoleId(stationRole.getId());
			stationRolePerm.setPermId(oper);
			stationRolePermMapper.insert(stationRolePerm);
		}
		return ExtResult.successResult();
	}

	public ExtResult delete(int id) {
		String username = userMapper.hasRecordByProperty("stationRoleId", id);
		if(StringUtils.isNotEmpty(username)) {
			return ExtResult.failResult(String.format("角色被用户[%s]使用，删除失败", username));
		}

		stationRolePermMapper.delete(id);

		if (stationRoleMapper.delete(id) > 0) {
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("操作失败");
		}
	}

	@Transactional(rollbackFor=Throwable.class)
	public ExtResult batchDelete(int[] id) {
		int count = 0;
		for(int e : id) {
			String user = userMapper.hasRecordByProperty("stationRoleId", e);
			if(StringUtils.isEmpty(user)) {
				stationRoleMapper.delete(e);
				count++;
			}
		}

		return ExtResult.successResult(String.format("成功删除%d条记录", count));
	}

	public void tree(String stationId, OutputStream stream) throws IOException {

		List<StationRole> list = stationRoleMapper.findByStationId(stationId);
		List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
		for (StationRole stationRole : list) {
			NodeModel nodeModel = new NodeModel();
			Node<NodeModel> root = new Node<NodeModel>(nodeModel);
			nodeModel.setId(stationRole.getId());
			nodeModel.setName(stationRole.getRoleName());
			roots.add(root);
		}

		ObjectMapper objectMapper = new ObjectMapper();
		JsonGenerator json = objectMapper.getJsonFactory().createJsonGenerator(stream, JsonEncoding.UTF8);
		json.writeStartArray();
		YhdgUtils.writeJson(roots, json);
		json.writeEndArray();
		json.flush();
		json.close();
	}

	public void stationAppTree(Integer id, OutputStream stream) throws IOException {
		Set<String> checkedOper = Collections.EMPTY_SET;
		if(id != null) {
			checkedOper = new HashSet<String>(stationRolePermMapper.findAll(id));
		}

		List<StationMenu> menuList = stationMenuMapper.findAll();
		Map<String, StationMenu> menuMap = new LinkedHashMap<String, StationMenu>();
		for(StationMenu menu : menuList) {
			menuMap.put(menu.getId(), menu);
		}

		List<StationPerm> operList = stationPermMapper.findAll();
		Map<String, StationPerm> operMap = new HashMap<String, StationPerm>();
		for(StationPerm oper : operList) {
			operMap.put(oper.getId(), oper);
		}


		Map<String, List<String>> menuParentMap = makeParentMap(menuMap);
		Map<String, List<String>> operMenuMap = makeMenuMap(operMap);
		List<String> rootMenuIdList = stationMenuMapper.findRoots();

		AtomicInteger seq = new AtomicInteger();

		Node<NodeModel> root = new Node<NodeModel>(new NodeModel());
		root.getData().setId( seq.incrementAndGet() );
		root.getData().setName("所有操作");
		root.getData().setLevel(1);
		for(String menuId : rootMenuIdList) {
			addSonMenu(seq, menuId, root, menuMap, menuParentMap, operMap, operMenuMap, checkedOper);
		}

		List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
		roots.add(root);

		ObjectMapper objectMapper = new ObjectMapper();
		JsonGenerator json = objectMapper.getJsonFactory().createJsonGenerator(stream, JsonEncoding.UTF8);
		json.writeStartArray();
		YhdgUtils.writeJson(roots, json);
		json.writeEndArray();

		json.flush();
		json.close();
	}

	private void addSonMenu(AtomicInteger seq, String menuId, Node<NodeModel> parent, Map<String, StationMenu> menuMap, Map<String, List<String>> menuParentMap, Map<String, StationPerm> operMap, Map<String, List<String>> operMenuMap, Set<String> operIdSet) {
		Node<NodeModel> node = new Node<NodeModel>(new NodeModel(), parent);
		node.getData().setName(menuMap.get(menuId).getMenuName());
		node.getData().setId(seq.incrementAndGet());
		node.getData().setLevel(parent.getData().getLevel() + 1);
		List<String> menuIdList = menuParentMap.get(menuId);
		if(menuIdList != null) {
			for(String id : menuIdList) {
				addSonMenu(seq, id, node, menuMap, menuParentMap, operMap, operMenuMap, operIdSet);
			}
		} else {
			List<String> operIdList = operMenuMap.get(menuId);
			if(operIdList != null) {
				for(String operId : operIdList) {
					Node<NodeModel> n = new Node<NodeModel>(new NodeModel(), node);
					StationPerm oper = operMap.get(operId);
					n.getData().setId( seq.incrementAndGet() );
					n.getData().setName(oper.getPermName());
					n.getData().addAttribute("id", operId);
					n.getData().setLevel(node.getData().getLevel() + 1);
					if(operIdSet.contains(operId)) {
						n.getData().setCheckStatus(NodeModel.CheckedStatus.checked);
					} else {
						n.getData().setCheckStatus(NodeModel.CheckedStatus.unchecked);
					}
				}
			}
		}
	}

	private Map<String, List<String>> makeParentMap(Map<String, StationMenu> menuMap) {
		Map<String, List<String>> parentMap = new HashMap<String, List<String>>();
		for(StationMenu menu : menuMap.values()) {
			if(menu.getParentId() != null) {
				String parentId = menu.getParentId();
				List<String> v = parentMap.get(parentId);
				if(v != null) {
					v.add(menu.getId());
				} else {
					v = new ArrayList<String>();
					parentMap.put(parentId, v);
					v.add(menu.getId());
				}
			}
		}

		return parentMap;
	}

	private Map<String, List<String>> makeMenuMap(Map<String, StationPerm> operMap) {
		Map<String, List<String>> menuMap = new HashMap<String, List<String>>();
		for(StationPerm oper : operMap.values()) {
			if(oper.getMenuId() != null) {
				String menuId = oper.getMenuId();
				List<String> v = menuMap.get(menuId);
				if(v != null) {
					v.add(oper.getId());
				} else {
					v = new ArrayList<String>();
					menuMap.put(menuId, v);
					v.add(oper.getId());
				}
			}
		}

		return menuMap;
	}

}
