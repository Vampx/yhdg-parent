package cn.com.yusong.yhdg.agentserver.service.zc;

import cn.com.yusong.yhdg.common.domain.zc.GroupOrder;
import cn.com.yusong.yhdg.common.domain.zc.RentPrice;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.tree.Node;
import cn.com.yusong.yhdg.common.entity.tree.NodeModel;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.agentserver.persistence.zc.GroupOrderMapper;
import cn.com.yusong.yhdg.agentserver.persistence.zc.RentPriceMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class RentPriceService extends AbstractService {

    @Autowired
    RentPriceMapper rentPriceMapper;
    @Autowired
    GroupOrderMapper groupOrderMapper;

    public void tree(Set<Integer> checked, String dummy, Long priceSettingId, ServletOutputStream stream) throws IOException {
        List<RentPrice> list = rentPriceMapper.findListBySetting(priceSettingId);
        List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
        for (RentPrice rentPrice : list) {
            NodeModel nodeModel = new NodeModel();
            Node<NodeModel> root = new Node<NodeModel>(nodeModel);
            nodeModel.setId(rentPrice.getId());
            nodeModel.setName(rentPrice.getPriceName());
            roots.add(root);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator json = objectMapper.getJsonFactory().createJsonGenerator(stream, JsonEncoding.UTF8);
        json.writeStartArray();
        AppUtils.writeJson(roots, json);
        json.writeEndArray();
        json.flush();
        json.close();
    }

    public RentPrice find(Long id) {
        return rentPriceMapper.find(id);
    }

    public List<RentPrice> findListByBatteryType(Integer batteryType, Integer agentId, Long priceSettingId) {
        return rentPriceMapper.findListByBatteryType(batteryType, agentId, priceSettingId);
    }

    public List<RentPrice> findListBySetting(Long priceSettingId) {
        return rentPriceMapper.findListBySetting(priceSettingId);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult findGroupByPrice(long id) {
        List<GroupOrder> list = groupOrderMapper.findByRentPriceId(id);
        if (list.size() > 0) {
            return ExtResult.failResult("该套餐已生成订单不能删除");
        } else {
            return ExtResult.successResult();
        }
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(long id) {
        rentPriceMapper.delete(id);
        return ExtResult.successResult();
    }
}
