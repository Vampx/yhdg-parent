package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Dept;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.entity.tree.Node;
import cn.com.yusong.yhdg.common.entity.tree.NodeModel;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.config.AppConfig;
import cn.com.yusong.yhdg.webserver.persistence.basic.DeptMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.UserMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

@Service
public class DeptService extends AbstractService {
    @Autowired
    DeptMapper deptMapper;
    @Autowired
    UserMapper userMapper;

    public Dept find(int id) {
        return deptMapper.find(id);
    }

    public Page findPage(Dept search) {
        Page page = search.buildPage();
        page.setTotalItems(deptMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(deptMapper.findPageResult(search));
        return page;
    }
    public int insert(Dept entity) {
        entity.setCreateTime(new Date());
        if(entity.getParentId() != null && entity.getParentId() == 0) {
            entity.setParentId(null);
        }
        return deptMapper.insert(entity);
    }
    public int update(Dept entity) {
        return deptMapper.update(entity);
    }

    public ExtResult hasRef(int deptId) {
        Dept dept = deptMapper.findByParent(deptId);
        if (dept != null) {
            return ExtResult.failResult(String.format("存在下级部门:%s不能删除", dept.getDeptName()));
        }
        String username = userMapper.hasRecordByProperty("deptId", deptId);
        if(StringUtils.isEmpty(username)) {
            return ExtResult.successResult();
        }
        return ExtResult.failResult(String.format("部门下有用户:%s不能删除", username));
    }

    public ExtResult delete(int id) {
        ExtResult result = hasRef(id);
        if(!result.isSuccess()) {
            return result;
        }
        deptMapper.delete(id);
        return result;
    }

    public void tree(Set<Integer> checked, String dummy, Integer agentId, OutputStream stream) throws IOException {
        List<Dept> deptList = deptMapper.findByAgent(agentId);
        Map<Integer, Dept> allMap = new HashMap<Integer, Dept>();
        List<Dept> topList = new ArrayList<Dept>();
        for(Dept dept : deptList) {
            allMap.put(dept.getId(), dept);
            if(dept.getParentId() == null) {
                topList.add(dept);
            }
        }

        List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
        if(StringUtils.isNotEmpty(dummy)) {
            NodeModel data = new NodeModel();
            Node<NodeModel> root = new Node<NodeModel>(data);
            data.setId(0);
            data.setName(dummy);
            roots.add(root);

            for(Dept topDept : topList) {
                NodeModel nodeModel = new NodeModel();
                Node<NodeModel> node = new Node<NodeModel>(nodeModel, root);

                nodeModel.setId(topDept.getId());
                nodeModel.setName(topDept.getDeptName());
                nodeModel.setCheckStatus(checked.contains(topDept.getId()) ? NodeModel.CheckedStatus.checked : NodeModel.CheckedStatus.unchecked);
                privateMakeTree(checked, node, allMap, makeParentMap(allMap));
            }

        } else {
            for(Dept topDept : topList) {
                NodeModel nodeModel = new NodeModel();
                Node<NodeModel> node = new Node<NodeModel>(nodeModel);

                nodeModel.setId(topDept.getId());
                nodeModel.setName(topDept.getDeptName());
                nodeModel.setCheckStatus(checked.contains(topDept.getId()) ? NodeModel.CheckedStatus.checked : NodeModel.CheckedStatus.unchecked);
                privateMakeTree(checked, node, allMap, makeParentMap(allMap));
                roots.add(node);
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator json = objectMapper.getJsonFactory().createJsonGenerator(stream, JsonEncoding.UTF8);
        json.writeStartArray();
        YhdgUtils.writeJson(roots, json);
        json.writeEndArray();

        json.flush();
        json.close();
    }
    private void privateMakeTree(Set<Integer> checked, Node<NodeModel> parent, Map<Integer, Dept> allMap, Map<Integer, List<Integer>> parentMap) {
        List<Integer> sonIdList = parentMap.get(parent.getData().getId());
        if(sonIdList != null) {
            for(Integer id : sonIdList) {
                Dept dept = allMap.get(id);
                NodeModel nodeModel = new NodeModel();
                Node<NodeModel> node = new Node<NodeModel>(nodeModel);
                nodeModel.setId(dept.getId());
                nodeModel.setName(dept.getDeptName());
                nodeModel.setCheckStatus(checked.contains(dept.getId()) ? NodeModel.CheckedStatus.checked : NodeModel.CheckedStatus.unchecked);

                parent.addChild(node);

                privateMakeTree(checked, node, allMap,  parentMap);
            }
        }
    }
    private Map<Integer, List<Integer>> makeParentMap(Map<Integer, Dept> deptMap) {
        Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
        for(Map.Entry<Integer, Dept> entry : deptMap.entrySet()) {
            if(entry.getValue().getParentId() != null) {
                Integer parentId = entry.getValue().getParentId();
                List<Integer> v = map.get(parentId);
                if(v == null) {
                    map.put(parentId, v = new ArrayList<Integer>());
                }
                v.add(entry.getKey());
            }
        }

        return map;
    }

}
