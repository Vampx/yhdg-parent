package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.SystemConfig;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.entity.tree.Node;
import cn.com.yusong.yhdg.common.entity.tree.NodeModel;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.config.AppConfig;
import cn.com.yusong.yhdg.webserver.persistence.basic.SystemConfigMapper;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SystemConfigService {


    @Autowired
    SystemConfigMapper systemConfigMapper;
    @Autowired
    AppConfig appConfig;

    public List<SystemConfig> findAll() {
        SystemConfig search = new SystemConfig();
        return systemConfigMapper.findAll(search);
    }

    public SystemConfig find(String id) {
        return systemConfigMapper.find(id);
    }

    public String findConfigValue(String id) {
        return systemConfigMapper.findConfigValue(id);
    }

    public Page findPage(SystemConfig search) {
        Page page = search.buildPage();
        page.setResult(systemConfigMapper.findAll(search));
        return page;
    }

    public void update(SystemConfig entity) {
        entity.setConfigValue(StringUtils.trimToEmpty(entity.getConfigValue()));
        systemConfigMapper.update(entity);
        updateConfig(entity);
    }


    public void initConfig() {
        List<SystemConfig> list = findAll();
        Map<String, SystemConfig> map = new HashMap<String, SystemConfig>();

        for (SystemConfig e : list) {
            map.put(e.getId(), e);
        }
        appConfig.setStaticUrl((String) map.get(ConstEnum.SystemConfigKey.STATIC_URL.getValue()).getConfigValue());
        appConfig.setBrandImageSuffix((String) map.get(ConstEnum.SystemConfigKey.BRAND_IMAGE_SUFFIX.getValue()).getConfigValue());
        appConfig.setAppVersion((String) map.get(ConstEnum.SystemConfigKey.APP_VERSION.getValue()).getConfigValue());
        appConfig.setBrandPlatformName((String) map.get(ConstEnum.SystemConfigKey.BRAND_PLATFORM_NAME.getValue()).getConfigValue());
    }

    private void updateConfig(SystemConfig entity) {

    }


    public void tree(String dummy, OutputStream stream) throws IOException {
        List<SystemConfig> list = systemConfigMapper.findAllCategory();
        List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
        if (StringUtils.isNotEmpty(dummy)) {
            NodeModel data = new NodeModel();
            Node<NodeModel> root = new Node<NodeModel>(data);
            data.setId(0);
            data.setName(dummy);
            roots.add(root);

            for (SystemConfig s : list) {
                NodeModel nodeModel = new NodeModel();
                Node<NodeModel> node = new Node<NodeModel>(nodeModel);
                nodeModel.setId(s.getCategoryType());
                nodeModel.setName(s.getCategoryName());
                root.addChild(node);
            }
        } else {
            for (SystemConfig s : list) {
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
