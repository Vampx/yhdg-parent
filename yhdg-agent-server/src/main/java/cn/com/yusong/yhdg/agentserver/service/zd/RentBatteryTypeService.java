package cn.com.yusong.yhdg.agentserver.service.zd;

import cn.com.yusong.yhdg.agentserver.persistence.zd.RentBatteryForegiftMapper;
import cn.com.yusong.yhdg.agentserver.persistence.zd.RentBatteryTypeMapper;
import cn.com.yusong.yhdg.agentserver.persistence.zd.RentPeriodPriceMapper;
import cn.com.yusong.yhdg.common.domain.zd.RentBatteryForegift;
import cn.com.yusong.yhdg.common.domain.zd.RentBatteryType;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodPrice;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.entity.tree.Node;
import cn.com.yusong.yhdg.common.entity.tree.NodeModel;
import cn.com.yusong.yhdg.common.utils.AppUtils;
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
public class RentBatteryTypeService {
    @Autowired
    private RentBatteryTypeMapper rentBatteryTypeMapper;
    @Autowired
    private RentBatteryForegiftMapper rentBatteryForegiftMapper;
    @Autowired
    private RentPeriodPriceMapper rentPeriodPriceMapper;

    public RentBatteryType find(Integer batteryType, Integer agentId) {
        return rentBatteryTypeMapper.find(batteryType, agentId);
    }

    public List<RentBatteryType> findBatteryListByAgentId(Integer agentId) {
        return rentBatteryTypeMapper.findBatteryListByAgentId(agentId);
    }

    public RentBatteryType findByBatteryTypeAndAgent(Integer batteryType, Integer agentId) {
        return rentBatteryTypeMapper.findByBatteryTypeAndAgent(batteryType, agentId);
    }

    public Page findPage(RentBatteryType search) {
        Page page = search.buildPage();
        page.setTotalItems(rentBatteryTypeMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<RentBatteryType> list = rentBatteryTypeMapper.findPageResult(search);
        page.setResult(list);
        return page;
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult create(RentBatteryType search) {
        if (rentBatteryTypeMapper.find(search.getBatteryType(), search.getAgentId()) != null) {
            return ExtResult.failResult("该运营商已存在该电池类型，无法新建");
        }

        rentBatteryTypeMapper.insert(search);
        return DataResult.successResult(search);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult update(RentBatteryType search) {
        if (rentBatteryTypeMapper.find(search.getToBatteryType(), search.getAgentId()) != null) {
            return ExtResult.failResult("该运营商已存在该电池类型，修改失败");
        }

        if (search.getToBatteryType().intValue() != search.getBatteryType().intValue()) {
            List<RentBatteryForegift> rentBatteryForegiftList = rentBatteryForegiftMapper.findListByBatteryType(search.getBatteryType(), search.getAgentId());
            if (rentBatteryForegiftList.size() > 0) {
                return ExtResult.failResult("存在押金，无法修改电池类型");
            }

            List<RentPeriodPrice> rentPeriodPriceList = rentPeriodPriceMapper.findListByBatteryType(search.getBatteryType(), search.getAgentId());
            if (rentPeriodPriceList.size() != 0) {
                return ExtResult.failResult("存在租金，无法修改");
            }
        }

        rentBatteryTypeMapper.update(search.getToBatteryType(), search.getTypeName(), search.getBatteryType(), search.getAgentId());
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult delete(Integer batteryType, Integer agentId) {
        //存在押金
        List<RentBatteryForegift> rentBatteryForegiftList = rentBatteryForegiftMapper.findListByBatteryType(batteryType, agentId);
        if (rentBatteryForegiftList.size() > 0) {
            return ExtResult.failResult("存在押金，无法删除");
        }

        List<RentPeriodPrice> rentPeriodPriceList = rentPeriodPriceMapper.findListByBatteryType(batteryType, agentId);
        if (rentPeriodPriceList.size() != 0) {
            return ExtResult.failResult("存在租金，无法删除");
        }

        rentBatteryTypeMapper.delete(batteryType, agentId);
        return ExtResult.successResult();
    }

    public void tree(Set<Integer> checked, String dummy, Integer agentId, ServletOutputStream stream) throws IOException {
        List<RentBatteryType> list = rentBatteryTypeMapper.findListByAgent(agentId);
        List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
        for (RentBatteryType rentBatteryType : list) {
            NodeModel nodeModel = new NodeModel();
            Node<NodeModel> root = new Node<NodeModel>(nodeModel);
            nodeModel.setId(rentBatteryType.getBatteryType());
            nodeModel.setName(rentBatteryType.getTypeName());
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

}
