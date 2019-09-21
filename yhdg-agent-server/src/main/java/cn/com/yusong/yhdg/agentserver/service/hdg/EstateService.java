package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.agentserver.config.AppConfig;
import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.*;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.Estate;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.entity.tree.Node;
import cn.com.yusong.yhdg.common.entity.tree.NodeModel;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

@Service
public class EstateService extends AbstractService {
    static Logger log = LoggerFactory.getLogger(EstateService.class);

    @Autowired
    EstateMapper estateMapper;
    @Autowired
    AppConfig config;
    @Autowired
    AreaCache areaCache;
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    ShopUserMapper shopUserMapper;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    PacketPeriodOrderMapper packetPeriodOrderMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    CustomerMapper customerMapper;

    /**
     * 查询分页
     *
     * @param search
     * @return
     */
    public Page findPage(Estate search) {
        Page page = search.buildPage();
        page.setTotalItems(estateMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<Estate> list = estateMapper.findPageResult(search);
        for (Estate estate : list) {
            AgentInfo agentInfo = findAgentInfo(estate.getAgentId());
            if (agentInfo != null) {
                estate.setAgentName(agentInfo.getAgentName());
            }
        }
        page.setResult(list);
        return page;
    }


    public Estate find(long id) {
        Estate estate =  estateMapper.find(id);
        if (estate != null) {
            if (estate.getAgentId() != null) {
                estate.setAgentName(findAgentInfo(estate.getAgentId()).getAgentName());
            }
        }
        return estate;
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult delete(long id) {
        List<Cabinet> cabinetList = cabinetMapper.findListByEstateId(id);
        if (cabinetList.size() > 0) {
            return ExtResult.failResult("该物业已绑定换电站，无法删除");
        }
        if (estateMapper.delete(id) == 0) {
            return ExtResult.failResult("删除失败！");
        }
        return ExtResult.successResult();
    }

    public ExtResult insert(Estate estate) {
        estate.setBalance(0);
        if (estate.getWorkTime().equals("-")) {
            estate.setWorkTime(null);
        }
        estate.setCreateTime(new Date());
        if (estateMapper.insert(estate) == 0) {
            return DataResult.failResult("对不起! 保存失败", null);
        }
        return DataResult.successResult("操作成功",estate.getId());
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult updateBasic(Estate estate) {
        Estate entity = estateMapper.find(estate.getId());
        if (entity == null) {
            return ExtResult.failResult("记录不存在");
        }
        if (estate.getWorkTime().equals("-")) {
            estate.setWorkTime(null);
        }
        if (estateMapper.update(estate) == 0) {
            return ExtResult.failResult("修改失败！");
        }

        return ExtResult.successResult();
    }

    public void tree(String dummy, Integer agentId, OutputStream stream) throws IOException {
        tree(buildTree(dummy, agentId), stream);
    }
    public void tree(Set<Integer> checked, String dummy, Integer agentId, ServletOutputStream stream) throws IOException {

        List<Estate> list = estateMapper.findByAgent(agentId);
        if (agentId == null && list.size() == ConstEnum.Flag.FALSE.getValue()) {
            list = estateMapper.findAll();
        }
        List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
        for (Estate estate : list) {
            NodeModel nodeModel = new NodeModel();
            Node<NodeModel> root = new Node<NodeModel>(nodeModel);
            nodeModel.setId(estate.getId());
            nodeModel.setName(estate.getEstateName());
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
    private List<Node<NodeModel>> buildTree(String dummy, Integer agentId) {
        Set<String> checked = Collections.emptySet();
        List<Estate> estateList = estateMapper.findByAgent(agentId);
        if (estateList.size() == ConstEnum.Flag.FALSE.getValue()) {
            estateList = estateMapper.findAll();
        }
        List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
        if (StringUtils.isNotEmpty(dummy)) {
            NodeModel data = new NodeModel();
            Node<NodeModel> root = new Node<NodeModel>(data);
            data.setId("");
            data.setName(dummy);
            roots.add(root);

            for (Estate topEstate : estateList) {
                NodeModel nodeModel = new NodeModel();
                Node<NodeModel> node = new Node<NodeModel>(nodeModel, root);

                nodeModel.setId(topEstate.getId());
                nodeModel.setName(topEstate.getEstateName());
                nodeModel.setCheckStatus(checked.contains(topEstate.getId()) ? NodeModel.CheckedStatus.checked : NodeModel.CheckedStatus.unchecked);
            }

        } else {
            for (Estate topEstate : estateList) {
                NodeModel nodeModel = new NodeModel();
                Node<NodeModel> node = new Node<NodeModel>(nodeModel);

                nodeModel.setId(topEstate.getId());
                nodeModel.setCheckStatus(checked.contains(topEstate.getId()) ? NodeModel.CheckedStatus.checked : NodeModel.CheckedStatus.unchecked);
                roots.add(node);
            }
        }
        return roots;
    }

    private void tree(List<Node<NodeModel>> roots, OutputStream stream) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator json = objectMapper.getJsonFactory().createJsonGenerator(stream, JsonEncoding.UTF8);
        json.writeStartArray();
        AppUtils.writeJson(roots, json);

        json.writeEndArray();
        json.flush();
        json.close();
    }

    public void updatePayPeople(long id, String payPeopleName, String payPeopleMpOpenId, String payPeopleFwOpenId, String payPeopleMobile, String payPassword) {
        estateMapper.updatePayPeople(id, payPeopleName, payPeopleMpOpenId, payPeopleFwOpenId, payPeopleMobile, payPassword);
    }


    @Transactional(rollbackFor = Throwable.class)
    public ExtResult setPayPeopleUpdate(Estate entity) {
        Estate estatePayPeopleMobile = estateMapper.findByPayPeopleMobile(entity.getPayPeopleMobile());
        if (estatePayPeopleMobile!= null){
            return ExtResult.failResult("收款人已存在");
        }
        Customer customer = customerMapper.find(entity.getCustomerId());
        Estate estate  = new Estate();
        estate.setId(entity.getId());
        estate.setPayPeopleMobile(entity.getPayPeopleMobile());
        estate.setPayPeopleName(entity.getPayPeopleName());
        if (customer.getMpOpenId() != null) {
            estate.setPayPeopleMpOpenId(customer.getMpOpenId());
        }
        if (customer.getFwOpenId() != null) {
            estate.setPayPeopleFwOpenId(customer.getFwOpenId());
        }
        estateMapper.update(estate);
        return ExtResult.successResult();
    }
}
