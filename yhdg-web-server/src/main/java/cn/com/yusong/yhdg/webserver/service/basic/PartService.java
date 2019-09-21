package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.entity.tree.Node;
import cn.com.yusong.yhdg.common.entity.tree.NodeModel;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.persistence.basic.*;
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
public class PartService extends AbstractService {

    @Autowired
    PartMapper partMapper;
    @Autowired
    MenuMapper menuMapper;
    @Autowired
    PermMapper permMapper;
    @Autowired
    PartPermMapper partPermMapper;
    @Autowired
    AgentMapper agentMapper;


    public Part find(Integer id) {
        Part part = partMapper.find(id);
        return part;
    }

    public void tree(Integer id, OutputStream stream) throws IOException {
        Set<String> checkedPerm = Collections.EMPTY_SET;
        if(id != null) {
            checkedPerm = new HashSet<String>(partPermMapper.findAll(id));
        }

        List<Menu> menuList = menuMapper.findAll();
        Map<String, Menu> menuMap = new LinkedHashMap<String, Menu>();
        for(Menu menu : menuList) {
            menuMap.put(menu.getId(), menu);
        }

        List<Perm> permList = permMapper.findAll();
        Map<String, Perm> permMap = new HashMap<String, Perm>();
        for(Perm perm : permList) {
            permMap.put(perm.getId(), perm);
        }

        Map<String, List<String>> menuParentMap = makeParentMap(menuMap);
        Map<String, List<String>> permMenuMap = makeMenuMap(permMap);
        List<String> rootMenuIdList = menuMapper.findRoots();

        AtomicInteger seq = new AtomicInteger();

        Node<NodeModel> root = new Node<NodeModel>(new NodeModel());
        root.getData().setId(seq.incrementAndGet());
        root.getData().setName("所有操作");
        root.getData().setLevel(1);
        for(String menuId : rootMenuIdList) {
            addSonMenu(seq, menuId, root, menuMap, menuParentMap, permMap, permMenuMap, checkedPerm);
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

    public List<Part> findList(String mobile, Integer partType) {
        List<Part> agentPartList = partMapper.findList(mobile, partType);
        for (Part part : agentPartList) {
            if (part.getAgentId() != null) {
                AgentInfo agentInfo = findAgentInfo(part.getAgentId());
                if (agentInfo != null) {
                    part.setAgentName(agentInfo.getAgentName());
                }
            }
        }
        return agentPartList;
    }

    public Page findPage(Part part) {
        Page page = part.buildPage();
        page.setTotalItems(partMapper.findPageCount(part));
        part.setBeginIndex(page.getOffset());
        page.setResult(partMapper.findPageResult(part));
        return page;
    }

    public Set<String> loadOperCode(int partId) {
        List<String> permIdList = partPermMapper.findAll(partId);

        List<Perm> permList = permMapper.findAll();
        Map<String, Perm> permMap = new HashMap<String, Perm>();
        for(Perm perm : permList) {
            permMap.put(perm.getId(), perm);
        }

        Set<String> codeSet = new HashSet<String>();

        for(String permId : permIdList) {
            Perm perm = permMap.get(permId);
            if(perm != null) {
                codeSet.add(perm.getId());
                if (StringUtils.isNotEmpty(perm.getDepend())) {
                    String[] depend = StringUtils.split(perm.getDepend(), ",");
                    codeSet.addAll(Arrays.asList(depend));
                }
            }
        }
        return codeSet;
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult create(Part part) {
        int partNameUnique = partMapper.findUnique(part.getId(), part.getPartName());
        if (partNameUnique > 0) {
            return ExtResult.failResult("该角色名称已存在");
        }
        List<Part> agentPartList = partMapper.findList(part.getMobile(), Part.PartType.AGENT.getValue());
        for (Part agentPart : agentPartList) {
            if (agentPart.getAgentId() == part.getAgentId().intValue()) {
                return ExtResult.failResult("该运营商的角色已存在");
            }
        }
        part.setCreateTime(new Date());
        partMapper.insert(part);
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult update(Part part) {
        int total = partMapper.update(part);
        if (total == 0) {
            return ExtResult.failResult("记录不存在");
        }
        return ExtResult.successResult();
    }

    public int delete(Integer id) {
        partPermMapper.delete(id);
        return partMapper.delete(id);
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

    private Map<String, List<String>> makeMenuMap(Map<String, Perm> permMap) {
        Map<String, List<String>> menuMap = new HashMap<String, List<String>>();
        for(Perm perm : permMap.values()) {
            if(perm.getMenuId() != null) {
                String menuId = perm.getMenuId();
                List<String> v = menuMap.get(menuId);
                if(v != null) {
                    v.add(perm.getId());
                } else {
                    v = new ArrayList<String>();
                    menuMap.put(menuId, v);
                    v.add(perm.getId());
                }
            }
        }

        return menuMap;
    }

    private void addSonMenu(AtomicInteger seq, String menuId, Node<NodeModel> parent, Map<String, Menu> menuMap, Map<String, List<String>> menuParentMap, Map<String, Perm> permMap, Map<String, List<String>> permMenuMap, Set<String> permIdSet) {
        Node<NodeModel> node = new Node<NodeModel>(new NodeModel(), parent);
        node.getData().setName(menuMap.get(menuId).getMenuName());
        node.getData().setId(seq.incrementAndGet());
        node.getData().setLevel(parent.getData().getLevel() + 1);
        List<String> menuIdList = menuParentMap.get(menuId);
        if(menuIdList != null) {
            for(String id : menuIdList) {
                addSonMenu(seq, id, node, menuMap, menuParentMap, permMap, permMenuMap, permIdSet);
            }
        } else {
            List<String> permIdList = permMenuMap.get(menuId);
            if(permIdList != null) {
                for(String permId : permIdList) {
                    Node<NodeModel> n = new Node<NodeModel>(new NodeModel(), node);
                    Perm perm = permMap.get(permId);
                    n.getData().setId( seq.incrementAndGet() );
                    n.getData().setName(perm.getPermName());
                    n.getData().addAttribute("id", permId);
                    n.getData().setLevel(node.getData().getLevel() + 1);
                    if(permIdSet.contains(permId)) {
                        n.getData().setCheckStatus(NodeModel.CheckedStatus.checked);
                    } else {
                        n.getData().setCheckStatus(NodeModel.CheckedStatus.unchecked);
                    }
                }
            }
        }
    }
}
