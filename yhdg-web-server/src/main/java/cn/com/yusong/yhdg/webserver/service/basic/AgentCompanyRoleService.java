package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.entity.tree.Node;
import cn.com.yusong.yhdg.common.entity.tree.NodeModel;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.persistence.basic.*;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ShopMapper;
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
public class AgentCompanyRoleService extends AbstractService{

	@Autowired
	AgentCompanyRoleMapper agentCompanyRoleMapper;
	@Autowired
	AgentCompanyMenuMapper agentCompanyMenuMapper;
	@Autowired
	AgentCompanyPermMapper agentCompanyPermMapper;
	@Autowired
	AgentCompanyRolePermMapper agentCompanyRolePermMapper;
	@Autowired
	UserMapper userMapper;
	@Autowired
	AgentCompanyMapper agentCompanyMapper;

	public AgentCompanyRole find(int id) {
		AgentCompanyRole agentCompanyRole = agentCompanyRoleMapper.find(id);
		AgentCompany agentCompany = agentCompanyMapper.find(agentCompanyRole.getAgentCompanyId());
		if (agentCompany != null && agentCompany.getAgentId() != null) {
			AgentInfo agentInfo = findAgentInfo(agentCompany.getAgentId());
			if (agentInfo != null) {
				agentCompanyRole.setAgentName(agentInfo.getAgentName());
			}
		}
		return agentCompanyRole;
	}

	public Page findPage(AgentCompanyRole search) {
		Page page = search.buildPage();
		page.setTotalItems(agentCompanyRoleMapper.findPageCount(search));
		search.setBeginIndex(page.getOffset());
		List<AgentCompanyRole> list = agentCompanyRoleMapper.findPageResult(search);
		for (AgentCompanyRole agentCompanyRole : list) {
			if (agentCompanyRole.getAgentCompanyId() != null) {
				AgentCompany agentCompany = agentCompanyMapper.find(agentCompanyRole.getAgentCompanyId());
				if (agentCompany != null) {
					agentCompanyRole.setAgentCompanyName(agentCompany.getCompanyName());
					AgentInfo agentInfo = findAgentInfo(agentCompany.getAgentId());
					if (agentInfo != null) {
						agentCompanyRole.setAgentName(agentInfo.getAgentName());
					}
				}
			}
		}
		page.setResult(list);
		return page;
	}

	public ExtResult create(AgentCompanyRole agentCompanyRole) {
		int roleNameUnique = agentCompanyRoleMapper.findUnique(agentCompanyRole.getId(), agentCompanyRole.getRoleName());
		if (roleNameUnique > 0) {
			return ExtResult.failResult("该角色名称已存在");
		}
		agentCompanyRoleMapper.insert(agentCompanyRole);
		for(String oper : agentCompanyRole.getPermList()) {
			AgentCompanyRolePerm agentCompanyRolePerm = new AgentCompanyRolePerm();
			agentCompanyRolePerm.setRoleId(agentCompanyRole.getId());
			agentCompanyRolePerm.setPermId(oper);
			agentCompanyRolePermMapper.insert(agentCompanyRolePerm);
		}
		return ExtResult.successResult();
	}

	public ExtResult update(AgentCompanyRole agentCompanyRole) {
		int roleNameUnique = agentCompanyRoleMapper.findUnique(agentCompanyRole.getId(), agentCompanyRole.getRoleName());
		if (roleNameUnique > 0) {
			return ExtResult.failResult("该角色名称已存在");
		}
		agentCompanyRoleMapper.update(agentCompanyRole);
		agentCompanyRolePermMapper.delete(agentCompanyRole.getId());
		for (String oper : agentCompanyRole.getPermList()) {
			AgentCompanyRolePerm agentCompanyRolePerm = new AgentCompanyRolePerm();
			agentCompanyRolePerm.setRoleId(agentCompanyRole.getId());
			agentCompanyRolePerm.setPermId(oper);
			agentCompanyRolePermMapper.insert(agentCompanyRolePerm);
		}
		return ExtResult.successResult();
	}

	public ExtResult delete(int id) {
		String username = userMapper.hasRecordByProperty("agentCompanyRoleId", id);
		if(StringUtils.isNotEmpty(username)) {
			return ExtResult.failResult(String.format("角色被用户[%s]使用，删除失败", username));
		}

		agentCompanyRolePermMapper.delete(id);

		if (agentCompanyRoleMapper.delete(id) > 0) {
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("操作失败");
		}
	}

	@Transactional(rollbackFor=Throwable.class)
	public ExtResult batchDelete(int[] id) {
		int count = 0;
		for(int e : id) {
			String user = userMapper.hasRecordByProperty("agentCompanyRoleId", e);
			if(StringUtils.isEmpty(user)) {
				agentCompanyRoleMapper.delete(e);
				count++;
			}
		}

		return ExtResult.successResult(String.format("成功删除%d条记录", count));
	}

	public void tree(String agentCompanyId, OutputStream stream) throws IOException {

		List<AgentCompanyRole> list = agentCompanyRoleMapper.findByAgentCompanyId(agentCompanyId);
		List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
		for (AgentCompanyRole agentCompanyRole : list) {
			NodeModel nodeModel = new NodeModel();
			Node<NodeModel> root = new Node<NodeModel>(nodeModel);
			nodeModel.setId(agentCompanyRole.getId());
			nodeModel.setName(agentCompanyRole.getRoleName());
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

	public void agentCompanyAppTree(Integer id, OutputStream stream) throws IOException {
		Set<String> checkedOper = Collections.EMPTY_SET;
		if(id != null) {
			checkedOper = new HashSet<String>(agentCompanyRolePermMapper.findAll(id));
		}

		List<AgentCompanyMenu> menuList = agentCompanyMenuMapper.findAll();
		Map<String, AgentCompanyMenu> menuMap = new LinkedHashMap<String, AgentCompanyMenu>();
		for(AgentCompanyMenu menu : menuList) {
			menuMap.put(menu.getId(), menu);
		}

		List<AgentCompanyPerm> operList = agentCompanyPermMapper.findAll();
		Map<String, AgentCompanyPerm> operMap = new HashMap<String, AgentCompanyPerm>();
		for(AgentCompanyPerm oper : operList) {
			operMap.put(oper.getId(), oper);
		}

		Map<String, List<String>> menuParentMap = makeParentMap(menuMap);
		Map<String, List<String>> operMenuMap = makeMenuMap(operMap);
		List<String> rootMenuIdList = agentCompanyMenuMapper.findRoots();

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

	private void addSonMenu(AtomicInteger seq, String menuId, Node<NodeModel> parent, Map<String, AgentCompanyMenu> menuMap, Map<String, List<String>> menuParentMap, Map<String, AgentCompanyPerm> operMap, Map<String, List<String>> operMenuMap, Set<String> operIdSet) {
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
					AgentCompanyPerm oper = operMap.get(operId);
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

	private Map<String, List<String>> makeParentMap(Map<String, AgentCompanyMenu> menuMap) {
		Map<String, List<String>> parentMap = new HashMap<String, List<String>>();
		for(AgentCompanyMenu menu : menuMap.values()) {
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

	private Map<String, List<String>> makeMenuMap(Map<String, AgentCompanyPerm> operMap) {
		Map<String, List<String>> menuMap = new HashMap<String, List<String>>();
		for(AgentCompanyPerm oper : operMap.values()) {
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
