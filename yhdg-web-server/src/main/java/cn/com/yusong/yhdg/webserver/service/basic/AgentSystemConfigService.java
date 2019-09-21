package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentSystemConfig;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.entity.tree.Node;
import cn.com.yusong.yhdg.common.entity.tree.NodeModel;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.config.AppConfig;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentSystemConfigMapper;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AgentSystemConfigService {

    @Autowired
    AgentMapper agentMapper;
    @Autowired
    AgentSystemConfigMapper systemConfigMapper;
    @Autowired
    AppConfig appConfig;

    public List<AgentSystemConfig> findAll() {
        AgentSystemConfig search = new AgentSystemConfig();
        return systemConfigMapper.findAll(search);
    }

    public AgentSystemConfig find(String id, Integer agentId) {
        return systemConfigMapper.find(id, agentId);
    }

    public Page findPage(AgentSystemConfig search) {
        if (search.getAgentId() == null) {
            search.setAgentId(ConstEnum.Flag.TRUE.getValue());
        }
        Page page = search.buildPage();
        page.setResult(systemConfigMapper.findAll(search));
        return page;
    }

    public String findConfigValue(String id, Integer agentId) {
        return systemConfigMapper.findConfigValue(id, agentId);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void update(AgentSystemConfig entity) {
        entity.setConfigValue(StringUtils.trimToEmpty(entity.getConfigValue()));
        systemConfigMapper.update(entity);
        updateConfig(entity);
    }


    public void initConfig() {
        List<AgentSystemConfig> list = findAll();
        Map<String, AgentSystemConfig> map = new HashMap<String, AgentSystemConfig>();

        for (AgentSystemConfig e : list) {
            map.put(e.getId(), e);
        }
        appConfig.setStaticUrl((String) map.get(ConstEnum.SystemConfigKey.STATIC_URL.getValue()).getConfigValue());
    }

    private void updateConfig(AgentSystemConfig entity) {

    }


    public void tree(String dummy, Integer agentId, OutputStream stream) throws IOException {
        List<AgentSystemConfig> list = systemConfigMapper.findAllCategoryByAgentId(agentId);
        List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
        if (StringUtils.isNotEmpty(dummy)) {
            NodeModel data = new NodeModel();
            Node<NodeModel> root = new Node<NodeModel>(data);
            data.setId(0);
            data.setName(dummy);
            roots.add(root);

            for (AgentSystemConfig s : list) {
                NodeModel nodeModel = new NodeModel();
                Node<NodeModel> node = new Node<NodeModel>(nodeModel);
                nodeModel.setId(s.getCategoryType());
                nodeModel.setName(s.getCategoryName());
                root.addChild(node);
            }
        } else {
            for (AgentSystemConfig s : list) {
                NodeModel nodeModel = new NodeModel();
                Node<NodeModel> node = new Node<NodeModel>(nodeModel);
                nodeModel.setId(s.getCategoryType());
                nodeModel.setName(s.getCategoryName());
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

}
