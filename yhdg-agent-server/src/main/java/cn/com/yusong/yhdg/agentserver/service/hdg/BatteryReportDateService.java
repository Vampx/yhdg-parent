package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryReportDate;
import cn.com.yusong.yhdg.common.entity.tree.Node;
import cn.com.yusong.yhdg.common.entity.tree.NodeModel;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.BatteryReportDateMapper;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BatteryReportDateService {
    @Autowired
    BatteryReportDateMapper batteryReportDateMapper;

    public void tree(OutputStream stream, String batteryId) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator json = objectMapper.getJsonFactory().createJsonGenerator(stream, JsonEncoding.UTF8);
        json.writeStartArray();
        YhdgUtils.writeJson(buildTree(batteryId), json);
        json.writeEndArray();

        json.flush();
        json.close();
    }

    private List<Node<NodeModel>> buildTree(String batteryId) {
        List<String> yearList = batteryReportDateMapper.findYeah(batteryId);
        List<Map<String, String>> monthMap = batteryReportDateMapper.findMonth(batteryId);
        List<BatteryReportDate> list = batteryReportDateMapper.findList(batteryId);
        List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
        for (String year : yearList) {
            NodeModel nodeModel = new NodeModel();
            Node<NodeModel> node = new Node<NodeModel>(nodeModel);

            nodeModel.setId(year);
            nodeModel.setName(year);
            nodeModel.setCheckStatus(NodeModel.CheckedStatus.unchecked);
            for (Map<String, String> map : monthMap) {
                String month = map.get("month");
                if (map.get("yeah").equals(year)) {
                    NodeModel nodeModel2 = new NodeModel();
                    Node<NodeModel> node2 = new Node<NodeModel>(nodeModel2);
                    nodeModel2.setId(year + "-" + month);
                    nodeModel2.setName(month);
                    nodeModel2.setCheckStatus(NodeModel.CheckedStatus.unchecked);
                    for (BatteryReportDate date : list) {
                        if (year.equals(date.getYeah()) && month.equals(date.getMonth())) {
                            NodeModel nodeModel3 = new NodeModel();
                            Node<NodeModel> node3 = new Node<NodeModel>(nodeModel3);
                            nodeModel3.setId(year + "-" + month + "-" + date.getDay());
                            nodeModel3.setName(date.getDay() + "(" + date.getRecordCount() + "条)");
                            nodeModel3.setCheckStatus(NodeModel.CheckedStatus.unchecked);
                            node2.addChild(node3);
                        }
                    }
                    node.addChild(node2);
                }
            }
            roots.add(node);
        }
        return roots;
    }

}
