package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerGuide;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.entity.tree.Node;
import cn.com.yusong.yhdg.common.entity.tree.NodeModel;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerGuideMapper;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class CustomerGuideService {
    @Autowired
    CustomerGuideMapper customerGuideMapper;

    public Page findPage(CustomerGuide search) {
        Page page = search.buildPage();
        page.setTotalItems(customerGuideMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(customerGuideMapper.findPageResult(search));
        return page;
    }

    public ExtResult insert(CustomerGuide customerGuide) {
        if (StringUtils.isEmpty(customerGuide.getName())) {
            return ExtResult.failResult("名称不可为空");
        }
        if (customerGuideMapper.insert(customerGuide) == 0) {
            return ExtResult.failResult("创建错误！");
        }
        return ExtResult.successResult();
    }

    public CustomerGuide find(Integer id) {
        return customerGuideMapper.find(id);
    }

    public ExtResult update(CustomerGuide entity) {
        if (StringUtils.isEmpty(entity.getName())) {
            return ExtResult.failResult("名称不可为空");
        }
        if(entity.getParentId()==entity.getId()){
            return ExtResult.failResult("不可将自己设置为子集");
        }
        if (entity.getParentId() != null) {
            if (entity.getParentId() == 0) {
                entity.setParentId(null);
            }
        }
        CustomerGuide customerGuide = customerGuideMapper.find(entity.getId());
        if (customerGuideMapper.update(entity) == 0) {
            return ExtResult.failResult("更新失败！");
        }
        if (customerGuide.getParentId() != entity.getParentId()) {
            customerGuideMapper.updateByParentId(entity.getId(), entity.getParentId());
        }
        return ExtResult.successResult();
    }

    public ExtResult delete(int id) {
        customerGuideMapper.delete(id);
        customerGuideMapper.deleteByParentId(id);
        return ExtResult.successResult();
    }

    public void tree(Set<Integer> checked, String dummy, ServletOutputStream stream) throws IOException {
        List<CustomerGuide> topList = new ArrayList<CustomerGuide>();

        List<CustomerGuide> customerGuides = customerGuideMapper.findAll();
        Map<Integer, CustomerGuide> allMap = new HashMap<Integer, CustomerGuide>();
        for (CustomerGuide customerGuide : customerGuides) {
            allMap.put(customerGuide.getId(), customerGuide);
            if (customerGuide.getParentId() == null) {
                topList.add(customerGuide);
            }
        }

        List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
        if (StringUtils.isNotEmpty(dummy)) {
            NodeModel data = new NodeModel();
            Node<NodeModel> root = new Node<NodeModel>(data);
            data.setId(0);
            data.setName(dummy);
            roots.add(root);

            for (CustomerGuide top : topList) {
                NodeModel nodeModel = new NodeModel();
                Node<NodeModel> node = new Node<NodeModel>(nodeModel, root);

                nodeModel.setId(top.getId());
                nodeModel.setName(top.getName());
                nodeModel.setCheckStatus(checked.contains(top.getId()) ? NodeModel.CheckedStatus.checked :
                        NodeModel.CheckedStatus.unchecked);
                privateMakeTree(checked, node, allMap, makeParentMap(allMap));
            }

        } else {
            for (CustomerGuide top : topList) {
                NodeModel nodeModel = new NodeModel();
                Node<NodeModel> node = new Node<NodeModel>(nodeModel);

                nodeModel.setId(top.getId());
                nodeModel.setName(top.getName());
                nodeModel.setCheckStatus(checked.contains(top.getId()) ? NodeModel.CheckedStatus.checked :
                        NodeModel.CheckedStatus.unchecked);
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

    public void parentTree(Set<Integer> checked, String dummy, ServletOutputStream stream) throws IOException {
        List<CustomerGuide> topList = new ArrayList<CustomerGuide>();

        List<CustomerGuide> customerGuides = customerGuideMapper.findAll();
        for (CustomerGuide customerGuide : customerGuides) {
            if (customerGuide.getParentId() == null) {
                topList.add(customerGuide);
            }
        }

        List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
        if (StringUtils.isNotEmpty(dummy)) {
            NodeModel data = new NodeModel();
            Node<NodeModel> root = new Node<NodeModel>(data);
            data.setId(0);
            data.setName(dummy);
            roots.add(root);

            for (CustomerGuide top : topList) {
                NodeModel nodeModel = new NodeModel();
                Node<NodeModel> node = new Node<NodeModel>(nodeModel, root);

                nodeModel.setId(top.getId());
                nodeModel.setName(top.getName());
                nodeModel.setCheckStatus(checked.contains(top.getId()) ? NodeModel.CheckedStatus.checked :
                        NodeModel.CheckedStatus.unchecked);
            }

        } else {
            for (CustomerGuide top : topList) {
                NodeModel nodeModel = new NodeModel();
                Node<NodeModel> node = new Node<NodeModel>(nodeModel);

                nodeModel.setId(top.getId());
                nodeModel.setName(top.getName());
                nodeModel.setCheckStatus(checked.contains(top.getId()) ? NodeModel.CheckedStatus.checked :
                        NodeModel.CheckedStatus.unchecked);
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

    private void privateMakeTree(Set<Integer> checked, Node<NodeModel> parent, Map<Integer, CustomerGuide> allMap, Map<Integer, List<Integer>> parentMap) {
        List<Integer> sonIdList = parentMap.get(parent.getData().getId());
        if (sonIdList != null) {
            for (Integer id : sonIdList) {
                CustomerGuide customerGuide = allMap.get(id);
                NodeModel nodeModel = new NodeModel();
                Node<NodeModel> node = new Node<NodeModel>(nodeModel);
                nodeModel.setId(customerGuide.getId());
                nodeModel.setName(customerGuide.getName());
                nodeModel.setCheckStatus(checked.contains(customerGuide.getId()) ? NodeModel.CheckedStatus.checked : NodeModel.CheckedStatus.unchecked);

                parent.addChild(node);

//                privateMakeTree(checked, node, allMap, parentMap);
            }
        }
    }

    private Map<Integer, List<Integer>> makeParentMap(Map<Integer, CustomerGuide> customerGuideMap) {
        Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
        for (Map.Entry<Integer, CustomerGuide> entry : customerGuideMap.entrySet()) {
            if (entry.getValue().getParentId() != null) {
                Integer parentId = entry.getValue().getParentId();
                List<Integer> v = map.get(parentId);
                if (v == null) {
                    map.put(parentId, v = new ArrayList<Integer>());
                }
                v.add(entry.getKey());
            }
        }

        return map;
    }


}
