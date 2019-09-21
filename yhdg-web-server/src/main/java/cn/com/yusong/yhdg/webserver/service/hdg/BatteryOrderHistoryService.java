package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.entity.tree.Node;
import cn.com.yusong.yhdg.common.entity.tree.NodeModel;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.persistence.hdg.BatteryOrderHistoryMapper;
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
public class BatteryOrderHistoryService extends AbstractService {
    @Autowired
    BatteryOrderHistoryMapper batteryOrderHistoryMapper;

    public Page findPage(BatteryOrder search) {
        Page page = search.buildPage();
        search.setBeginIndex(page.getOffset());
        if (StringUtils.isEmpty(search.getSuffix())) {
            page.setTotalItems(0);
            page.setResult(null);
        } else {
            page.setTotalItems(batteryOrderHistoryMapper.findPageCount(search));
            page.setResult(batteryOrderHistoryMapper.findPageResult(search));
        }
        return page;
    }

    public BatteryOrder find(String id,String suffix) {
        return batteryOrderHistoryMapper.find(id,suffix);
    }

    public void tree(OutputStream stream) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator json = objectMapper.getJsonFactory().createJsonGenerator(stream, JsonEncoding.UTF8);
        json.writeStartArray();
        YhdgUtils.writeJson(buildTree(), json);
        json.writeEndArray();

        json.flush();
        json.close();
    }

    private List<Node<NodeModel>> buildTree() {
        List<String> tableNames = batteryOrderHistoryMapper.findTable(BatteryOrder.BATTERY_ORDER_TABLE_NAME+"%");
        Set<String> yearList = new HashSet<String>();
        List<Map<String, String>> monthMap = new ArrayList<Map<String, String>>();
        for (String tableName : tableNames) {
            String year = tableName.substring(tableName.lastIndexOf("_") + 1, tableName.lastIndexOf("_") + 5);
            String month = tableName.substring(tableName.lastIndexOf("_") + 5);
            yearList.add(year);
            Map map = new HashMap();
            map.put("year", year);
            map.put("month", month);
            monthMap.add(map);
        }
        List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
        for (String year : yearList) {
            NodeModel nodeModel = new NodeModel();
            Node<NodeModel> node = new Node<NodeModel>(nodeModel);

            nodeModel.setId(year);
            nodeModel.setName(year);
            nodeModel.setCheckStatus(NodeModel.CheckedStatus.unchecked);
            for (Map<String, String> map : monthMap) {
                if (map.get("year").equals(year)) {
                    NodeModel nodeModel2 = new NodeModel();
                    Node<NodeModel> node2 = new Node<NodeModel>(nodeModel2);
                    nodeModel2.setId(year + map.get("month"));
                    nodeModel2.setName(map.get("month"));
                    nodeModel2.setCheckStatus(NodeModel.CheckedStatus.unchecked);
                    node.addChild(node2);
                }
            }
            roots.add(node);
        }
        return roots;
    }
}
