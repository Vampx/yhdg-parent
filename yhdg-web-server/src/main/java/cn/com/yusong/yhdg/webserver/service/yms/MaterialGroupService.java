package cn.com.yusong.yhdg.webserver.service.yms;

import cn.com.yusong.yhdg.common.domain.yms.MaterialGroup;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.entity.tree.Node;
import cn.com.yusong.yhdg.common.entity.tree.NodeModel;
import cn.com.yusong.yhdg.webserver.persistence.yms.MaterialGroupMapper;
import cn.com.yusong.yhdg.webserver.persistence.yms.MaterialMapper;
import cn.com.yusong.yhdg.webserver.utils.AppUtils;
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
public class MaterialGroupService {
    @Autowired
    MaterialGroupMapper materialGroupMapper;
    @Autowired
    MaterialMapper materialMapper;

    public MaterialGroup find(Long id) {
        return materialGroupMapper.find(id);
    }

    public Page findPage(MaterialGroup search) {
        Page page = search.buildPage();
        page.setTotalItems(materialGroupMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(materialGroupMapper.findPageResult(search));
        return page;
    }
    public int insert(MaterialGroup entity) {
        if(entity.getParentId() != null && entity.getParentId() == 0) {
            entity.setParentId(null);
        }
        return materialGroupMapper.insert(entity);
    }

    public int update(MaterialGroup entity) {
        if(entity.getParentId() != null && entity.getParentId() == 0) {
            entity.setParentId(null);
        }
        return materialGroupMapper.update(entity);
    }

    public ExtResult hasRef(long materialId) {
        String materialName = materialMapper.hasRecordByProperty("groupId", materialId);
        if(StringUtils.isNotEmpty(materialName)) {
            return ExtResult.failResult(String.format("分组下面有下有节目:%s不能删除", materialName));
        }

        String groupName = materialGroupMapper.hasRecordByProperty("parentId", materialId);
        if(StringUtils.isNotEmpty(groupName)) {
            return ExtResult.failResult(String.format("分组下面有子分组:%s不能删除", groupName));
        }

        return ExtResult.successResult();

    }

    public ExtResult delete(long id) {
        ExtResult result = hasRef(id);
        if(!result.isSuccess()) {
            return result;
        }
        materialGroupMapper.delete(id);
        return result;
    }


    public void tree(Set<Integer> checked, String dummy, Integer agentId, OutputStream stream) throws IOException {
        List<MaterialGroup> groupList = materialGroupMapper.findByAgent(agentId);;
        Map<Long, MaterialGroup> allMap = new HashMap<Long, MaterialGroup>();
        List<MaterialGroup> topList = new ArrayList<MaterialGroup>();
        for(MaterialGroup materialGroup : groupList) {
            allMap.put(materialGroup.getId(), materialGroup);
            if(materialGroup.getParentId() == null) {
                topList.add(materialGroup);
            }
        }

        List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
        if(StringUtils.isNotEmpty(dummy)) {
            NodeModel data = new NodeModel();
            Node<NodeModel> root = new Node<NodeModel>(data);
            data.setId(0);
            data.setName(dummy);
            roots.add(root);

            for(MaterialGroup topMaterialGroup : topList) {
                NodeModel nodeModel = new NodeModel();
                Node<NodeModel> node = new Node<NodeModel>(nodeModel, root);
                nodeModel.setId(topMaterialGroup.getId());
                nodeModel.setName(topMaterialGroup.getGroupName());
                nodeModel.setCheckStatus(checked.contains(topMaterialGroup.getId()) ? NodeModel.CheckedStatus.checked : NodeModel.CheckedStatus.unchecked);
                privateMakeTree(checked, node, allMap, makeParentMap(allMap));
            }

        } else {
            for(MaterialGroup topMaterialGroup : topList) {
                NodeModel nodeModel = new NodeModel();
                Node<NodeModel> node = new Node<NodeModel>(nodeModel);

                nodeModel.setId(topMaterialGroup.getId());
                nodeModel.setName(topMaterialGroup.getGroupName());
                nodeModel.setCheckStatus(checked.contains(topMaterialGroup.getId()) ? NodeModel.CheckedStatus.checked : NodeModel.CheckedStatus.unchecked);
                privateMakeTree(checked, node, allMap, makeParentMap(allMap));
                roots.add(node);
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator json = objectMapper.getJsonFactory().createJsonGenerator(stream, JsonEncoding.UTF8);
        json.writeStartArray();
        AppUtils.writeJson(roots, json);
        json.writeEndArray();

        json.flush();
        json.close();
    }

    private void privateMakeTree(Set<Integer> checked, Node<NodeModel> parent, Map<Long, MaterialGroup> allMap, Map<Long, List<Long>> parentMap) {
        List<Long> sonIdList = parentMap.get(parent.getData().getId());
        if(sonIdList != null) {
            for(Long id : sonIdList) {
                MaterialGroup materialGroup = allMap.get(id);
                NodeModel nodeModel = new NodeModel();
                Node<NodeModel> node = new Node<NodeModel>(nodeModel);
                nodeModel.setId(materialGroup.getId());
                nodeModel.setName(materialGroup.getGroupName());
                nodeModel.setCheckStatus(checked.contains(materialGroup.getId()) ? NodeModel.CheckedStatus.checked : NodeModel.CheckedStatus.unchecked);

                parent.addChild(node);

                privateMakeTree(checked, node, allMap,  parentMap);
            }
        }
    }

    private Map<Long, List<Long>> makeParentMap(Map<Long, MaterialGroup> materialGroupMap) {
        Map<Long, List<Long>> map = new HashMap<Long, List<Long>>();
        for(Map.Entry<Long, MaterialGroup> entry : materialGroupMap.entrySet()) {
            if(entry.getValue().getParentId() != null) {
                Long parentId = entry.getValue().getParentId();
                List<Long> v = map.get(parentId);
                if(v == null) {
                    map.put(parentId, v = new ArrayList<Long>());
                }
                v.add(entry.getKey());
            }
        }
        return map;
    }


}
