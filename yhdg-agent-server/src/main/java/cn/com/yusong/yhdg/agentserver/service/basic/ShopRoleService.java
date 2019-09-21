package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.agentserver.persistence.basic.*;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.ShopMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.entity.tree.Node;
import cn.com.yusong.yhdg.common.entity.tree.NodeModel;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
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
public class ShopRoleService extends AbstractService {

	@Autowired
	ShopRoleMapper shopRoleMapper;
	@Autowired
	ShopMenuMapper shopMenuMapper;
	@Autowired
	ShopPermMapper shopPermMapper;
	@Autowired
	ShopRolePermMapper shopRolePermMapper;
	@Autowired
	UserMapper userMapper;
	@Autowired
	ShopMapper shopMapper;

	public ShopRole find(int id) {
		ShopRole shopRole = shopRoleMapper.find(id);
		Shop shop = shopMapper.find(shopRole.getShopId());
		if (shop != null && shop.getAgentId() != null) {
			AgentInfo agentInfo = findAgentInfo(shop.getAgentId());
			if (agentInfo != null) {
				shopRole.setAgentName(agentInfo.getAgentName());
			}
		}
		return shopRole;
	}

	public Page findPage(ShopRole search) {
		Page page = search.buildPage();
		page.setTotalItems(shopRoleMapper.findPageCount(search));
		search.setBeginIndex(page.getOffset());
		List<ShopRole> list = shopRoleMapper.findPageResult(search);
		for (ShopRole shopRole : list) {
			if (shopRole.getShopId() != null) {
				Shop shop = shopMapper.find(shopRole.getShopId());
				if (shop != null) {
					shopRole.setShopName(shop.getShopName());
					AgentInfo agentInfo = findAgentInfo(shop.getAgentId());
					if (agentInfo != null) {
						shopRole.setAgentName(agentInfo.getAgentName());
					}
				}
			}
		}
		page.setResult(list);
		return page;
	}

	public ExtResult create(ShopRole shopRole) {
		int roleNameUnique = shopRoleMapper.findUnique(shopRole.getId(), shopRole.getRoleName());
		if (roleNameUnique > 0) {
			return ExtResult.failResult("该角色名称已存在");
		}
		shopRoleMapper.insert(shopRole);
		for(String oper : shopRole.getPermList()) {
			ShopRolePerm shopRolePerm = new ShopRolePerm();
			shopRolePerm.setRoleId(shopRole.getId());
			shopRolePerm.setPermId(oper);
			shopRolePermMapper.insert(shopRolePerm);
		}
		return ExtResult.successResult();
	}

	public ExtResult update(ShopRole shopRole) {
		int roleNameUnique = shopRoleMapper.findUnique(shopRole.getId(), shopRole.getRoleName());
		if (roleNameUnique > 0) {
			return ExtResult.failResult("该角色名称已存在");
		}
		shopRoleMapper.update(shopRole);
		shopRolePermMapper.delete(shopRole.getId());
		for (String oper : shopRole.getPermList()) {
			ShopRolePerm shopRolePerm = new ShopRolePerm();
			shopRolePerm.setRoleId(shopRole.getId());
			shopRolePerm.setPermId(oper);
			shopRolePermMapper.insert(shopRolePerm);
		}
		return ExtResult.successResult();
	}

	public ExtResult delete(int id) {
		String username = userMapper.hasRecordByProperty("shopRoleId", id);
		if(StringUtils.isNotEmpty(username)) {
			return ExtResult.failResult(String.format("角色被用户[%s]使用，删除失败", username));
		}

		shopRolePermMapper.delete(id);

		if (shopRoleMapper.delete(id) > 0) {
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("操作失败");
		}
	}

	@Transactional(rollbackFor=Throwable.class)
	public ExtResult batchDelete(int[] id) {
		int count = 0;
		for(int e : id) {
			String user = userMapper.hasRecordByProperty("shopRoleId", e);
			if(StringUtils.isEmpty(user)) {
				shopRoleMapper.delete(e);
				count++;
			}
		}

		return ExtResult.successResult(String.format("成功删除%d条记录", count));
	}

	public void tree(String shopId, OutputStream stream) throws IOException {

		List<ShopRole> list = shopRoleMapper.findByShopId(shopId);
		List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
		for (ShopRole shopRole : list) {
			NodeModel nodeModel = new NodeModel();
			Node<NodeModel> root = new Node<NodeModel>(nodeModel);
			nodeModel.setId(shopRole.getId());
			nodeModel.setName(shopRole.getRoleName());
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

	public void shopAppTree(Integer id, OutputStream stream) throws IOException {
		Set<String> checkedOper = Collections.EMPTY_SET;
		if(id != null) {
			checkedOper = new HashSet<String>(shopRolePermMapper.findAll(id));
		}

		List<ShopMenu> menuList = shopMenuMapper.findAll();
		Map<String, ShopMenu> menuMap = new LinkedHashMap<String, ShopMenu>();
		for(ShopMenu menu : menuList) {
			menuMap.put(menu.getId(), menu);
		}

		List<ShopPerm> operList = shopPermMapper.findAll();
		Map<String, ShopPerm> operMap = new HashMap<String, ShopPerm>();
		for(ShopPerm oper : operList) {
			operMap.put(oper.getId(), oper);
		}


		Map<String, List<String>> menuParentMap = makeParentMap(menuMap);
		Map<String, List<String>> operMenuMap = makeMenuMap(operMap);
		List<String> rootMenuIdList = shopMenuMapper.findRoots();

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

	private void addSonMenu(AtomicInteger seq, String menuId, Node<NodeModel> parent, Map<String, ShopMenu> menuMap, Map<String, List<String>> menuParentMap, Map<String, ShopPerm> operMap, Map<String, List<String>> operMenuMap, Set<String> operIdSet) {
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
					ShopPerm oper = operMap.get(operId);
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

	private Map<String, List<String>> makeParentMap(Map<String, ShopMenu> menuMap) {
		Map<String, List<String>> parentMap = new HashMap<String, List<String>>();
		for(ShopMenu menu : menuMap.values()) {
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

	private Map<String, List<String>> makeMenuMap(Map<String, ShopPerm> operMap) {
		Map<String, List<String>> menuMap = new HashMap<String, List<String>>();
		for(ShopPerm oper : operMap.values()) {
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
