package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.entity.tree.Node;
import cn.com.yusong.yhdg.common.entity.tree.NodeModel;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.agentserver.persistence.basic.*;
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
public class RoleService {

    @Autowired
    RoleMapper roleMapper;
    @Autowired
    MenuMapper menuMapper;
    @Autowired
    PermMapper operMapper;
    @Autowired
    RolePermMapper roleOperMapper;
    @Autowired
    AgentMenuMapper agentMenuMapper;
    @Autowired
    AgentPermMapper agentOperMapper;
    @Autowired
    AgentRolePermMapper agentRoleOperMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    AgentMapper agentMapper;

    public Role find(int id) {
        return roleMapper.find(id);
    }

    public List<Role> findByAgent(int agentId) {
        return roleMapper.findByAgent(agentId);
    }


    public Page findPage(Role search) {
        Page page = search.buildPage();
        page.setTotalItems(roleMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(roleMapper.findPageResult(search));
        return page;
    }

    public Page findAgentPage(Role search) {
        Page page = search.buildPage();
        page.setTotalItems(roleMapper.findAgentPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(roleMapper.findAgentPageResult(search));
        return page;
    }

    public Set<String> loadOperCode(int roleId) {
        List<String> operIdList = agentRoleOperMapper.findAll(roleId);

        List<AgentPerm> operList = agentOperMapper.findAll();
        Map<String, AgentPerm> operMap = new HashMap<String, AgentPerm>();
        for(AgentPerm perm : operList) {
            operMap.put(perm.getId(), perm);
        }

        Set<String> codeSet = new HashSet<String>();

        for(String operId : operIdList) {
            AgentPerm perm = operMap.get(operId);
            if(perm != null) {
                codeSet.add(perm.getId());
                if(StringUtils.isNotEmpty(perm.getDepend())) {
                    String[] depend =  StringUtils.split(perm.getDepend(), ",");
                    codeSet.addAll(Arrays.asList(depend));
                }
            }

        }
        return codeSet;
    }

    public ExtResult create(Role role) {
        int roleNameUnique = roleMapper.findUnique(role.getId(), role.getRoleName());
        if (roleNameUnique > 0) {
            return ExtResult.failResult("该角色名称已存在");
        }
        roleMapper.insert(role);
        Integer agentId = role.getAgentId();
        for(String oper : role.getPermList()) {
            if (agentId == null || agentId == 0) { //系统
                RolePerm rolePerm = new RolePerm();
                rolePerm.setRoleId(role.getId());
                rolePerm.setPermId(oper);
                roleOperMapper.insert(rolePerm);
            } else { //运营商
                AgentRolePerm agentRolePerm = new AgentRolePerm();
                agentRolePerm.setRoleId(role.getId());
                agentRolePerm.setPermId(oper);
                agentRoleOperMapper.insert(agentRolePerm);
            }
        }
        return ExtResult.successResult();
    }

    public ExtResult update(Role role) {
        int roleNameUnique = roleMapper.findUnique(role.getId(), role.getRoleName());
        if (roleNameUnique > 0) {
            return ExtResult.failResult("该角色名称已存在");
        }
        roleMapper.update(role);
        Integer agentId = role.getAgentId();
        if (agentId == null || agentId == 0) { //系统
            agentRoleOperMapper.delete(role.getId());
            for(String oper : role.getPermList()) {
                AgentRolePerm roleOper = new AgentRolePerm();
                roleOper.setRoleId(role.getId());
                roleOper.setPermId(oper);
                agentRoleOperMapper.insert(roleOper);
            }
        } else { //运营商
            agentRoleOperMapper.delete(role.getId());
            for(String oper : role.getPermList()) {
                AgentRolePerm agentRolePerm = new AgentRolePerm();
                agentRolePerm.setRoleId(role.getId());
                agentRolePerm.setPermId(oper);
                agentRoleOperMapper.insert(agentRolePerm);
            }
        }
        return ExtResult.successResult();
    }

    public ExtResult delete(int id) {
        String username = userMapper.hasRecordByProperty("roleId", id);
        if(StringUtils.isNotEmpty(username)) {
            return ExtResult.failResult(String.format("角色被用户[%s]使用，删除失败", username));
        }

        Integer agentId = roleMapper.find(id).getAgentId();
        if (agentId == null || agentId == 0) { //系统
            roleOperMapper.delete(id);
        } else { //运营商
            agentRoleOperMapper.delete(id);
        }

        if (roleMapper.delete(id) > 0) {
            return ExtResult.successResult();
        } else {
            return ExtResult.failResult("操作失败");
        }
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult batchDelete(int[] id) {
        int count = 0;
        for(int e : id) {
            String user = userMapper.hasRecordByProperty("roleId", e);
            if(StringUtils.isEmpty(user)) {
                roleMapper.delete(e);
                count++;
            }
        }

        return ExtResult.successResult(String.format("成功删除%d条记录", count));
    }

    public void tree(int agentId, OutputStream stream) throws IOException {

        List<Role> list =  roleMapper.findByAgent(agentId);
        List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
        for(Role role : list) {
            NodeModel nodeModel = new NodeModel();
            Node<NodeModel> root = new Node<NodeModel>(nodeModel);
            nodeModel.setId(role.getId());
            nodeModel.setName(role.getRoleName());
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

    public void tree(Integer id, OutputStream stream) throws IOException {
        Set<String> checkedOper = Collections.EMPTY_SET;
        if(id != null) {
            checkedOper = new HashSet<String>(roleOperMapper.findAll(id));
        }

        List<Menu> menuList = menuMapper.findAll();
        Map<String, Menu> menuMap = new LinkedHashMap<String, Menu>();
        for(Menu menu : menuList) {
            menuMap.put(menu.getId(), menu);
        }

        List<Perm> operList = operMapper.findAll();
        Map<String, Perm> operMap = new HashMap<String, Perm>();
        for(Perm oper : operList) {
            operMap.put(oper.getId(), oper);
        }


        Map<String, List<String>> menuParentMap = makeParentMap(menuMap);
        Map<String, List<String>> operMenuMap = makeMenuMap(operMap);
        List<String> rootMenuIdList = menuMapper.findRoots();

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

    public void agentTree(Integer id, OutputStream stream, Role agentRole) throws IOException {
        Set<String> checkedOper = Collections.EMPTY_SET;
        if (id != null) {
            checkedOper = new HashSet<String>(agentRoleOperMapper.findAll(id));
        }
        int isExchange = 0;
        int isRent = 0;
        int isVehicle = 0;
        if (id != null) {
            Role role = roleMapper.find(id);
            if (role != null) {
                Agent agent = agentMapper.find(role.getAgentId());
                if (agent != null) {
                    if (agent.getIsExchange() == 1) {
                        isExchange = 1;
                    }
                    if (agent.getIsRent() == 1) {
                        isRent = 1;
                    }
                    if (agent.getIsVehicle() == 1) {
                        isVehicle = 1;
                    }
                }
            }
        } else {
            Agent agent = agentMapper.find(agentRole.getAgentId());
            if (agent != null) {
                if (agent.getIsExchange() == 1) {
                    isExchange = 1;
                }
                if (agent.getIsRent() == 1) {
                    isRent = 1;
                }
                if (agent.getIsVehicle() == 1) {
                    isVehicle = 1;
                }
            }
        }

        List<String> activeMenuIdList = new ArrayList<String>();
        List<String> activePermIdList = new ArrayList<String>();

        List<AgentPerm> allOperList = agentOperMapper.findAll();
        Map<String, AgentPerm> allOperMap = new HashMap<String, AgentPerm>();
        for (AgentPerm oper : allOperList) {
            allOperMap.put(oper.getId(), oper);
        }

        if (agentRole != null) {
            List<String> permIdList = agentRoleOperMapper.findAll(agentRole.getId());
            List<String> webPermIdList = new ArrayList<String>();
            //过滤手机端权限
            for (String permId : permIdList) {
                AgentPerm agentPerm = allOperMap.get(permId);
                if (agentPerm != null && agentPerm.getClientType() == AgentPerm.ClientType.PC.getValue()) {
                    webPermIdList.add(permId);
                }
            }

            for (String permId : webPermIdList) {
                //将配置好的权限放入可用权限集合中
                activePermIdList.add(permId);
                AgentPerm agentPerm = agentOperMapper.find(permId);
                if (agentPerm != null) {
                    //将权限对应的菜单放入可用菜单集合中
                    activeMenuIdList.add(agentPerm.getMenuId());
                }
            }
        }

        List<AgentMenu> menuList = agentMenuMapper.findAll();

        Map<String, AgentMenu> allMenuMap = new LinkedHashMap<String, AgentMenu>();
        for (AgentMenu agentMenu : menuList) {
            allMenuMap.put(agentMenu.getId(), agentMenu);
        }

        //将可用菜单对应的上级菜单放入可用菜单集合中
        int level = 1;
        List<String> agentParentMenuList = new ArrayList<String>();
        for (String menuId : activeMenuIdList) {
            agentParentMenuList.add(menuId);
        }
        agentParentActiveMenu(agentParentMenuList, activeMenuIdList, menuList, allMenuMap, level);

        Map<String, AgentMenu> menuMap = new LinkedHashMap<String, AgentMenu>();
        for (AgentMenu agentMenu : menuList) {
            if (activeMenuIdList.contains(agentMenu.getId())) {
                menuMap.put(agentMenu.getId(), agentMenu);
            }
        }

        List<AgentPerm> operList = agentOperMapper.findAll();
        Map<String, AgentPerm> operMap = new HashMap<String, AgentPerm>();
        for (AgentPerm oper : operList) {
            if (activePermIdList.contains(oper.getId())) {
                operMap.put(oper.getId(), oper);
            }
        }

        Map<String, List<String>> menuParentMap = agentMakeParentMap(menuMap);
        Map<String, List<String>> operMenuMap = agentMakeMenuMap(operMap);
        List<String> rootMenuIdList = agentMenuMapper.findRoots();
        List<String> activeRootMenuIdList = new ArrayList<String>();

        for (String menuId : rootMenuIdList) {
            for (String activeMenuId : activeMenuIdList) {
                if (menuId.equals(activeMenuId)) {
                    activeRootMenuIdList.add(activeMenuId);
                }
            }
        }

        AtomicInteger seq = new AtomicInteger();

        Node<NodeModel> root = new Node<NodeModel>(new NodeModel());
        root.getData().setId(seq.incrementAndGet());
        root.getData().setName("所有操作");
        root.getData().setLevel(1);

        AgentMenu exchangeMenu = findByMenuName("换电");
        AgentMenu rentMenu = findByMenuName("租电");
        AgentMenu vehicleMenu = findByMenuName("租车");

        for (String menuId : activeRootMenuIdList) {
            if (isExchange == 0 && menuId.equals(exchangeMenu.getId())) {
                continue;
            }
            if (isRent == 0 && menuId.equals(rentMenu.getId())) {
                continue;
            }
            if (isVehicle == 0 && menuId.equals(vehicleMenu.getId())) {
                continue;
            }
            agentAddSonMenu(seq, menuId, root, menuMap, menuParentMap, operMap, operMenuMap, checkedOper);
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

    public void agentAppTree(Integer id, OutputStream stream, Role agentRole) throws IOException {
        Set<String> checkedOper = Collections.EMPTY_SET;
        if (id != null) {
            checkedOper = new HashSet<String>(agentRoleOperMapper.findAll(id));
        }

        List<String> activeMenuIdList = new ArrayList<String>();
        List<String> activePermIdList = new ArrayList<String>();

        List<AgentPerm> allOperList = agentOperMapper.findAllAppPerm();
        Map<String, AgentPerm> allOperMap = new HashMap<String, AgentPerm>();
        for (AgentPerm oper : allOperList) {
            allOperMap.put(oper.getId(), oper);
        }

        if (agentRole != null) {
            List<String> permIdList = agentRoleOperMapper.findAll(agentRole.getId());
            List<String> appPermIdList = new ArrayList<String>();
            //过滤web端权限
            for (String permId : permIdList) {
                AgentPerm agentPerm = allOperMap.get(permId);
                if (agentPerm != null && agentPerm.getClientType() == AgentPerm.ClientType.H5.getValue()) {
                    appPermIdList.add(permId);
                }
            }
            for (String permId : appPermIdList) {
                //将配置好的权限放入可用权限集合中
                activePermIdList.add(permId);
                AgentPerm agentPerm = agentOperMapper.findAppPerm(permId);
                if (agentPerm != null) {
                    //将权限对应的菜单放入可用菜单集合中
                    activeMenuIdList.add(agentPerm.getMenuId());
                }
            }
        }

        List<AgentMenu> menuList = agentMenuMapper.findAllByClientType(AgentMenu.ClientType.H5.getValue());
        Map<String, AgentMenu> allMenuMap = new LinkedHashMap<String, AgentMenu>();
        for (AgentMenu agentMenu : menuList) {
            //运营商端过滤掉电池规则菜单
            if (!agentMenu.getId().equals("app.hdg.BatteryRule")) {
                allMenuMap.put(agentMenu.getId(), agentMenu);
            }
        }

        //将可用菜单对应的上级菜单放入可用菜单集合中
        int level = 1;
        List<String> agentParentMenuList = new ArrayList<String>();
        for (String menuId : activeMenuIdList) {
            agentParentMenuList.add(menuId);
        }
        agentParentActiveMenu(agentParentMenuList, activeMenuIdList, menuList, allMenuMap, level);

        Map<String, AgentMenu> menuMap = new LinkedHashMap<String, AgentMenu>();
        for (AgentMenu agentMenu : menuList) {
            if (activeMenuIdList.contains(agentMenu.getId())) {
                menuMap.put(agentMenu.getId(), agentMenu);
            }
        }

        List<AgentPerm> operList = agentOperMapper.findAllByClientType(AgentPerm.ClientType.H5.getValue());
        Map<String, AgentPerm> operMap = new HashMap<String, AgentPerm>();
        for (AgentPerm oper : operList) {
            if (activePermIdList.contains(oper.getId())) {
                operMap.put(oper.getId(), oper);
            }
        }

        Map<String, List<String>> menuParentMap = agentMakeParentMap(menuMap);
        Map<String, List<String>> operMenuMap = agentMakeMenuMap(operMap);
        List<String> rootMenuIdList = agentMenuMapper.findRootsByClientType(AgentMenu.ClientType.H5.getValue());
        AtomicInteger seq = new AtomicInteger();

        Node<NodeModel> root = new Node<NodeModel>(new NodeModel());
        root.getData().setId(seq.incrementAndGet());
        root.getData().setName("所有操作");
        root.getData().setLevel(1);

        List<String> activeRootMenuIdList = new ArrayList<String>();

        for (String menuId : rootMenuIdList) {
            for (String activeMenuId : activeMenuIdList) {
                if (menuId.equals(activeMenuId)) {
                    activeRootMenuIdList.add(activeMenuId);
                }
            }
        }

        for (String menuId : activeRootMenuIdList) {
            agentAddSonMenu(seq, menuId, root, menuMap, menuParentMap, operMap, operMenuMap, checkedOper);
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

    private AgentMenu findByMenuName(String menuName) {
        return agentMenuMapper.findByMenuName(menuName);
    }

    private void addSonMenu(AtomicInteger seq, String menuId, Node<NodeModel> parent, Map<String, Menu> menuMap, Map<String, List<String>> menuParentMap, Map<String, Perm> operMap, Map<String, List<String>> operMenuMap, Set<String> operIdSet) {
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
                    Perm oper = operMap.get(operId);
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

    private void agentAddSonMenu(AtomicInteger seq, String menuId, Node<NodeModel> parent, Map<String, AgentMenu> menuMap, Map<String, List<String>> menuParentMap, Map<String, AgentPerm> operMap, Map<String, List<String>> operMenuMap, Set<String> operIdSet) {
        Node<NodeModel> node = new Node<NodeModel>(new NodeModel(), parent);
        node.getData().setName(menuMap.get(menuId).getMenuName());
        node.getData().setId(seq.incrementAndGet());
        node.getData().setLevel(parent.getData().getLevel() + 1);
        List<String> menuIdList = menuParentMap.get(menuId);
        if(menuIdList != null) {
            for(String id : menuIdList) {
                agentAddSonMenu(seq, id, node, menuMap, menuParentMap, operMap, operMenuMap, operIdSet);
            }
        } else {
            List<String> operIdList = operMenuMap.get(menuId);
            if(operIdList != null) {
                for(String operId : operIdList) {
                    Node<NodeModel> n = new Node<NodeModel>(new NodeModel(), node);
                    AgentPerm oper = operMap.get(operId);
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

    private Map<String, List<String>> makeParentMap(Map<String, Menu> menuMap) {
        Map<String, List<String>> parentMap = new HashMap<String, List<String>>();
        for(Menu menu : menuMap.values()) {
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

    private Map<String, List<String>> agentMakeParentMap(Map<String, AgentMenu> menuMap) {
        Map<String, List<String>> parentMap = new HashMap<String, List<String>>();
        for(AgentMenu menu : menuMap.values()) {
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

    private Map<String, List<String>> makeMenuMap(Map<String, Perm> operMap) {
        Map<String, List<String>> menuMap = new HashMap<String, List<String>>();
        for(Perm oper : operMap.values()) {
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

    private Map<String, List<String>> agentMakeMenuMap(Map<String, AgentPerm> operMap) {
        Map<String, List<String>> menuMap = new HashMap<String, List<String>>();
        for(AgentPerm oper : operMap.values()) {
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

    private void agentParentActiveMenu(List<String> agentParentMenuList, List<String> activeMenuList, List<AgentMenu> menuList, Map<String, AgentMenu> allMenuMap, int level) {
        for (String menuId : activeMenuList) {
            for (AgentMenu parentAgentMenu : menuList) {
                AgentMenu sonMenu = allMenuMap.get(menuId);
                if (sonMenu != null && StringUtils.isNotEmpty(sonMenu.getParentId()) && sonMenu.getParentId().equals(parentAgentMenu.getId())) {
                    if (!agentParentMenuList.contains(parentAgentMenu.getId())) {
                        agentParentMenuList.add(parentAgentMenu.getId());
                    }
                }
            }
        }

        for (String menuId : agentParentMenuList) {
            if (!activeMenuList.contains(menuId)) {
                activeMenuList.add(menuId);
            }
        }
        level++;
        //如果没有上一级别，就直接返回
        if (level <= 3) {
            agentParentActiveMenu(agentParentMenuList, activeMenuList, menuList, allMenuMap, level);
        }
    }
}
