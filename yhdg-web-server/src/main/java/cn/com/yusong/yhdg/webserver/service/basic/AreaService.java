package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Area;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.entity.tree.Node;
import cn.com.yusong.yhdg.common.entity.tree.NodeModel;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.persistence.basic.AreaMapper;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by chen on 2017/5/18.
 */
@Service
public class AreaService {

    @Autowired
    AreaCache areaCache;

    @Autowired
    AreaMapper areaMapper;

    public List<Area> findChildren(int id) {
        return areaMapper.findChildren(id);
    }

    public Area find(int id) {
        return areaMapper.find(id);
    }

    public List<Area> findAll() {
        return areaMapper.findAll();
    }

    public List<Area> findAllCity() {
        return areaMapper.findAllCity();
    }

    public void children(String dummy, String parentId, OutputStream stream) throws IOException {
        String ZERO = "0";
        String[] PREFIX_NAMES = {null, "province_", "city_", "district_"};

        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator json = objectMapper.getJsonFactory().createJsonGenerator(stream, JsonEncoding.UTF8);
        json.writeStartArray();

        if(parentId == null) {
            NodeModel data = new NodeModel();
            data.setName(dummy);
            data.setId(ZERO);
            data.setState("open");
            Node<NodeModel> root = new Node<NodeModel>(data);

            List<Area> areaList = areaCache.getRootList();

            for(Area area : areaList) {
                NodeModel child = new NodeModel();
                child.setName(area.getAreaName());
                child.setId(String.format("%s%d", PREFIX_NAMES[area.getAreaLevel()], area.getId()));
                child.setState(areaCache.getChildren(area.getId()).isEmpty() ? "open" : "closed");
                new Node<NodeModel>(child, root);
            }
            YhdgUtils.writeJson(root, json);

        } else {
            Integer id = Integer.parseInt(parentId.substring(parentId.indexOf("_") + 1, parentId.length()));
            List<Area> areaList = areaCache.getChildren(id);
            Node<NodeModel> root = new Node<NodeModel>(null);
            for(Area area : areaList) {
                NodeModel child = new NodeModel();
                child.setName(area.getAreaName());
                child.setId(String.format("%s%d", PREFIX_NAMES[area.getAreaLevel()], area.getId()));
                child.setState(areaCache.getChildren(area.getId()).isEmpty() ? "open" : "closed");
                new Node<NodeModel>(child, root);
            }
            YhdgUtils.writeJson(root.getChildren(), json);
        }

        json.writeEndArray();
        json.flush();
        json.close();
    }

//    public List<Area> findProvinceForCharger() {
//        return areaMapper.findProvinceForCharger();
//    }
//
//    public List<Area> findCityByProvinceForCharger(Integer provinceId) {
//        return areaMapper.findCityByProvinceForCharger(provinceId);
//    }


}
