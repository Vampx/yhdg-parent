package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.entity.tree.Node;
import cn.com.yusong.yhdg.common.entity.tree.NodeModel;
import cn.com.yusong.yhdg.common.exception.BalanceNotEnoughException;
import cn.com.yusong.yhdg.common.service.AbstractService;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.config.AppConfig;
import cn.com.yusong.yhdg.webserver.persistence.basic.*;
import cn.com.yusong.yhdg.webserver.persistence.hdg.BatteryOrderMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.CabinetMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.PacketPeriodOrderMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ShopMapper;
import cn.com.yusong.yhdg.webserver.persistence.yms.TerminalStrategyMapper;
import cn.com.yusong.yhdg.webserver.persistence.zd.RentForegiftOrderMapper;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;


@Service
public class AgentService extends AbstractService {
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    AppConfig config;
    @Autowired
    AreaCache areaCache;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    PartnerMapper partnerMapper;
    @Autowired
    DeptMapper deptMapper;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    TerminalStrategyMapper terminalStrategyMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    BatteryOrderMapper batteryOrderMapper;
    @Autowired
    AgentSystemConfigMapper agentSystemConfigMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    OrderIdService orderIdService;
    @Autowired
    AgentForegiftWithdrawOrderMapper agentForegiftWithdrawOrderMapper;
    @Autowired
    AgentInOutMoneyMapper agentInOutMoneyMapper;
    @Autowired
    CustomerForegiftOrderMapper customerForegiftOrderMapper;
    @Autowired
    RentForegiftOrderMapper rentForegiftOrderMapper;
    @Autowired
    AgentForegiftDepositOrderMapper agentForegiftDepositOrderMapper;
    @Autowired
    AgentForegiftInOutMoneyMapper agentForegiftInOutMoneyMapper;
    @Autowired
    ShopMapper shopMapper;
    @Autowired
    ShopInOutMoneyMapper shopInOutMoneyMapper;
    @Autowired
    AgentShopBalanceRecordMapper agentShopBalanceRecordMapper;
    @Autowired
    AgentShopBalanceRecordDetailMapper agentShopBalanceRecordDetailMapper;
    @Autowired
    PacketPeriodOrderMapper packetPeriodOrderMapper;

    public Agent find(long id) {
        Agent agent = agentMapper.find(id);
        return (Agent) setAreaProperties(areaCache, agent);
    }

    public Agent findForegift(long id) {
        Agent agent = agentMapper.find(id);
        if(agent != null){
            int ratio = 100;
            String systemRatio = agentSystemConfigMapper.findConfigValue(ConstEnum.AgentSystemConfigKey.BATTERY_FOREGIFT_KEEP_RATIO.getValue(), agent.getId());
            if(StringUtils.isNotEmpty(systemRatio)) {
                ratio = Integer.parseInt(systemRatio);
            }

            int floorBalance =  agent.getForegiftBalance() * ratio / 100;
            agent.setHdWithdrawMoney(agent.getForegiftRemainMoney() - floorBalance);
            int zdFloorBalance =  agent.getZdForegiftBalance() * ratio / 100;
            agent.setZdWithdrawMoney(agent.getZdForegiftRemainMoney() - zdFloorBalance);

        }
        return agent;
    }

    public List<Agent> findAll() {
        return agentMapper.findAll();
    }

    public List<Agent> topAgentList() {
        return agentMapper.topAgentList();
    }

    public Page findPage(Agent search) {
        Page page = search.buildPage();
        page.setTotalItems(agentMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<Agent> agentList = agentMapper.findPageResult(search);
        for (Agent agent : agentList) {

            Partner partner = partnerMapper.find(agent.getPartnerId());
            if (partner != null) {
                agent.setPartnerName(partner.getPartnerName());
            }
        }
        page.setResult(agentList);
        return page;
    }

    public Page findForegiftPage(Agent search) {
        Page page = search.buildPage();
        page.setTotalItems(agentMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<Agent> agentList = agentMapper.findPageResult(search);
        for (Agent agent : agentList) {
            int ratio = 100;
            String systemRatio = agentSystemConfigMapper.findConfigValue(ConstEnum.AgentSystemConfigKey.BATTERY_FOREGIFT_KEEP_RATIO.getValue(), agent.getId());
            if(StringUtils.isNotEmpty(systemRatio)) {
                ratio = Integer.parseInt(systemRatio);
            }
            int floorBalance =  agent.getForegiftBalance() * ratio / 100;
            agent.setHdWithdrawMoney(agent.getForegiftRemainMoney() - floorBalance);
            int zdFloorBalance =  agent.getZdForegiftBalance() * ratio / 100;
            agent.setZdWithdrawMoney(agent.getZdForegiftRemainMoney() - zdFloorBalance);
            agent.setCategory(search.getCategory());
        }
        page.setResult(agentList);
        return page;
    }

    public Page findPageTree(Agent search) {
        Page page = search.buildPage();
        page.setTotalItems(agentMapper.findTreePageCount(search));
        search.setBeginIndex(page.getOffset());
        List<Agent> agentList = agentMapper.findTreePageResult(search);
        for(Agent agent : agentList) {
            List<Agent> childrens = agentMapper.findByParentId(agent.getId());
            Partner partner = partnerMapper.find(agent.getPartnerId());
            if (partner != null) {
                agent.setPartnerName(partner.getPartnerName());
            }
            if(childrens == null || childrens.size() == 0) {
                agent.setState("open");
            } else {
                agent.setState("closed");
            }
        }
        page.setResult(agentList);
        return page;
    }

    public List<Agent> childPage(Integer parentId) {
        List<Agent> childrens = agentMapper.findByParentId(parentId);
        for(Agent agent : childrens) {
            List<Agent> childrenList = agentMapper.findByParentId(agent.getId());
            if(childrenList == null || childrenList.size() == 0) {
                agent.setState("open");
            } else {
                agent.setState("closed");
            }
        }
        return childrens;
    }

    public int update(Agent entity) {
        if (entity.getParentId() != null && entity.getParentId() == 0) {
            entity.setParentId(null);
        }
        return agentMapper.update(entity);
    }

    private void buildNameMap(Node<NodeModel> node, Map<Object, String> map) {
        map.put(node.getData().getId(), node.getData().getName());

        for (Node<NodeModel> child : node.getChildren()) {
            buildNameMap(child, map);
        }
    }

    public void tree(String dummy, Integer parentId, OutputStream stream) throws IOException {
        tree(buildTree(parentId, dummy), stream);
    }

    private void tree(List<Node<NodeModel>> roots, OutputStream stream) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator json = objectMapper.getJsonFactory().createJsonGenerator(stream, JsonEncoding.UTF8);
        json.writeStartArray();
        YhdgUtils.writeJson(roots, json);
        json.writeEndArray();

        json.flush();
        json.close();
    }

    private List<Node<NodeModel>> buildTree(Integer parentId, String dummy) {
        Set<Integer> checked = Collections.emptySet();

        List<Agent> agentList = agentMapper.findAll();
        Map<Integer, Agent> allMap = new HashMap<Integer, Agent>();
        List<Agent> topList = new ArrayList<Agent>();

        for (Agent agent : agentList) {
            allMap.put(agent.getId(), agent);
            if (parentId == null) {
                if (agent.getParentId() == null) {
                    topList.add(agent);
                }
            } else {
                if (agent.getId().equals(parentId)) {
                    topList.add(agent);
                }
            }
        }

        List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
        if (StringUtils.isNotEmpty(dummy)) {
            NodeModel data = new NodeModel();
            Node<NodeModel> root = new Node<NodeModel>(data);
            data.setId("");
            data.setName(dummy);
            roots.add(root);

            for (Agent topAgent : topList) {
                NodeModel nodeModel = new NodeModel();
                Node<NodeModel> node = new Node<NodeModel>(nodeModel, root);

                nodeModel.setId(topAgent.getId());
                nodeModel.setName(topAgent.getAgentName());
                nodeModel.setCheckStatus(checked.contains(topAgent.getId()) ? NodeModel.CheckedStatus.checked : NodeModel.CheckedStatus.unchecked);
                privateMakeTree(checked, node, allMap, makeParentMap(allMap));
            }

        } else {
            for (Agent topAgent : topList) {
                NodeModel nodeModel = new NodeModel();
                Node<NodeModel> node = new Node<NodeModel>(nodeModel);

                nodeModel.setId(topAgent.getId());
                nodeModel.setName(topAgent.getAgentName());
                nodeModel.setCheckStatus(checked.contains(topAgent.getId()) ? NodeModel.CheckedStatus.checked : NodeModel.CheckedStatus.unchecked);
                privateMakeTree(checked, node, allMap, makeParentMap(allMap));
                roots.add(node);
            }
        }

        return roots;
    }

    private void privateMakeTree(Set<Integer> checked, Node<NodeModel> parent, Map<Integer, Agent> allMap, Map<Integer, List<Integer>> parentMap) {
        List<Integer> sonIdList = parentMap.get(parent.getData().getId());
        if (sonIdList != null) {
            for (Integer id : sonIdList) {
                Agent agent = allMap.get(id);
                NodeModel nodeModel = new NodeModel();
                Node<NodeModel> node = new Node<NodeModel>(nodeModel);
                nodeModel.setId(agent.getId());
                nodeModel.setName(agent.getAgentName());
                nodeModel.setCheckStatus(checked.contains(agent.getId()) ? NodeModel.CheckedStatus.checked : NodeModel.CheckedStatus.unchecked);

                parent.addChild(node);

                privateMakeTree(checked, node, allMap, parentMap);
            }
        }
    }

    private Map<Integer, List<Integer>> makeParentMap(Map<Integer, Agent> agentMap) {
        Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
        for (Map.Entry<Integer, Agent> entry : agentMap.entrySet()) {
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

    private ExtResult hasRef(int agentId) {
        int amount = agentMapper.hasRecordByProperty("parentId", agentId);
        if (amount > 0) {
            return ExtResult.failResult("请先删除子运营商");
        }

        amount = deptMapper.hasRecordByProperty("agentId", agentId);
        if (amount > 0) {
            return ExtResult.failResult("该运营商下含有部门不能删除");
        }

        amount = roleMapper.hasRecordByProperty("agentId", agentId);
        if (amount > 0) {
            return ExtResult.failResult("该运营商下含有角色不能删除");
        }

        String username = userMapper.hasRecordByProperty("agentId", agentId);
        if (StringUtils.isNotEmpty(username)) {
            return ExtResult.failResult("该运营商下已有用户不能删除");
        }

        amount = terminalStrategyMapper.hasRecordByProperty("agentId", agentId);
        if (amount > 0) {
            return ExtResult.failResult("该运营商下已有终端策略,请选删除策略");
        }

        String msg = cabinetMapper.hasRecordByProperty("agentId", agentId);
        if (StringUtils.isNotEmpty(msg)) {
            return ExtResult.failResult("该运营商下已有设备不能删除");
        }

        amount = batteryOrderMapper.hasRecordByProperty("agentId", agentId);
        if (amount > 0) {
            return ExtResult.failResult("该运营商已有订单,不能删除");
        }

        return ExtResult.successResult();
    }


    @Transactional(rollbackFor = Throwable.class)
    public void insert(Agent agent) {
        agent.setCreateTime(new Date());
        agent.setBalance(ConstEnum.Flag.FALSE.getValue());
        agent.setForegiftBalance(0);
        agent.setForegiftRemainMoney(0);
        agent.setForegiftBalanceRatio(0);
        agent.setZdForegiftBalance(0);
        agent.setZdForegiftRemainMoney(0);
        agent.setZdForegiftBalanceRatio(0);
        agent.setOrderNum(10);
        agentMapper.insert(agent);

        List<AgentSystemConfig> agentSystemConfigList = agentSystemConfigMapper.findByAgentId(Constant.TEST_AGENT_ID);
        for (AgentSystemConfig agentSystemConfig : agentSystemConfigList) {
            agentSystemConfig.setAgentId(agent.getId());
            agentSystemConfigMapper.insertOne(agentSystemConfig);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult delete(int id) {
        ExtResult result = hasRef(id);
        if (!result.isSuccess()) {
            return result;
        }
        agentSystemConfigMapper.deleteByAgentId(id);
        agentMapper.delete(id);
        return ExtResult.successResult();
    }

    public int updateOrderNum(int id, int orderNum) {
        return agentMapper.updateOrderNum(id, orderNum);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult updteExchangeWithdrawal(int id, int money, String operator) {
        Date now = new Date();

        Agent agent = agentMapper.find(id);
        if (agent == null){
            return ExtResult.failResult("运营商不存在");
        }

        //计算出不可提金额
        int ratio = 100;
        String systemRatio =  agentSystemConfigMapper.findConfigValue(ConstEnum.AgentSystemConfigKey.BATTERY_FOREGIFT_KEEP_RATIO.getValue(), agent.getId() );
        if(StringUtils.isNotEmpty(systemRatio)){
            ratio = Integer.parseInt(systemRatio);
        }

        int floorBalance =  agent.getForegiftBalance() * ratio / 100;
        int allowBalane = agent.getForegiftRemainMoney() - floorBalance - money;
        if(allowBalane < 0 ){
            return ExtResult.failResult("运营商余额不足");
        }
        agentMapper.updateBalance(agent.getId(), money);


        //生成提现订单
        AgentForegiftWithdrawOrder order = new AgentForegiftWithdrawOrder();
        order.setId(orderIdService.newOrderId(OrderId.OrderIdType.AGENT_FORGIFT_WITHDRAW_ORDER));
        order.setPartnerId(agent.getPartnerId());
        order.setCategory(ConstEnum.Category.EXCHANGE.getValue());
        order.setAgentId(agent.getId());
        order.setAgentName(agent.getAgentName());
        order.setAgentCode(agent.getAgentCode());
        order.setAccountType(AgentForegiftWithdrawOrder.AccountType.BALANCE.getValue());
        order.setMoney(money);
        order.setRealMoney(money);
        order.setServiceMoney(0);
        order.setStatus(AgentForegiftWithdrawOrder.Status.WITHDRAW_OK.getValue());
        order.setHandleTime(new Date());
        order.setOperator(operator);
        order.setCreateTime(new Date());
        agentForegiftWithdrawOrderMapper.insert(order);

        //运营商流水
        AgentInOutMoney inOutMoney = new AgentInOutMoney();
        inOutMoney.setAgentId(agent.getId());
        inOutMoney.setMoney(money);
        inOutMoney.setBalance(agentMapper.find(agent.getId()).getBalance());
        inOutMoney.setBizType(AgentInOutMoney.BizType.IN_AGENT_FOREGIFT_WITHDRAW.getValue());
        inOutMoney.setType(AgentInOutMoney.Type.IN.getValue());
        inOutMoney.setBizId(order.getId());
        inOutMoney.setOperator(operator);
        inOutMoney.setCreateTime(now);
        agentInOutMoneyMapper.insert(inOutMoney);


        //更新运营商押金余额 预留金额  押金余额比例
        List<Integer> statusList = Arrays.asList(CustomerForegiftOrder.Status.PAY_OK.getValue(),
                CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
        //押金余额
        int foregiftBalance = customerForegiftOrderMapper.sumMoneyByAgent(order.getAgentId(), statusList);
        //运营商押金充值
        int deposit =  agentForegiftDepositOrderMapper.sumMoney(order.getAgentId(), ConstEnum.Category.EXCHANGE.getValue(), AgentForegiftDepositOrder.Status.HAVE_PAID.getValue());
        //运营商提现
        int withdraw = agentForegiftWithdrawOrderMapper.sumMoney(order.getAgentId(), ConstEnum.Category.EXCHANGE.getValue(), AgentForegiftWithdrawOrder.Status.WITHDRAW_OK.getValue());

        //预留金额 = 押金余额 + 运营商押金充值 - 运营商押金提现
        int foregiftRemainMoney = foregiftBalance + deposit - withdraw;

        int foregiftBalanceRatio = 100;
        if(foregiftBalance != 0 ){
            foregiftBalanceRatio = foregiftRemainMoney * 100 / foregiftBalance;
        }
        if(foregiftRemainMoney < 0 ){
            foregiftBalanceRatio = 0;
        }

        //更新运营商押金
        if(agentMapper.updateForegift(order.getAgentId(), foregiftBalance, foregiftRemainMoney, foregiftBalanceRatio) > 0){
            //运营商押金流水
            AgentForegiftInOutMoney foregiftInOutMoney = new AgentForegiftInOutMoney();
            foregiftInOutMoney.setAgentId(order.getAgentId());
            foregiftInOutMoney.setCategory(ConstEnum.Category.EXCHANGE.getValue());
            foregiftInOutMoney.setMoney(-order.getMoney());
            foregiftInOutMoney.setBizType(AgentForegiftInOutMoney.BizType.OUT_AGENT_PAY_WITHDRAW_ORDER.getValue());
            foregiftInOutMoney.setBizId(order.getId());
            foregiftInOutMoney.setType(AgentInOutMoney.Type.OUT.getValue());
            foregiftInOutMoney.setBalance(foregiftBalance);
            foregiftInOutMoney.setRemainMoney(foregiftRemainMoney);
            foregiftInOutMoney.setRatio(foregiftBalanceRatio);
            foregiftInOutMoney.setOperator(operator);
            foregiftInOutMoney.setCreateTime(new Date());
            agentForegiftInOutMoneyMapper.insert(foregiftInOutMoney);
        }
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult updteRentWithdrawal(int id, int money, String operator) {
        Date now = new Date();

        Agent agent = agentMapper.find(id);
        if (agent == null){
            return ExtResult.failResult("运营商不存在");
        }

        //计算出不可提金额
        int ratio = 100;
        String systemRatio =  agentSystemConfigMapper.findConfigValue(ConstEnum.AgentSystemConfigKey.BATTERY_FOREGIFT_KEEP_RATIO.getValue(), agent.getId() );
        if(StringUtils.isNotEmpty(systemRatio)){
            ratio = Integer.parseInt(systemRatio);
        }

        int floorBalance =  agent.getZdForegiftBalance() * ratio / 100;
        int allowBalane = agent.getZdForegiftRemainMoney() - floorBalance - money;
        if(allowBalane < 0 ){
            throw new BalanceNotEnoughException();
        }
        agentMapper.updateBalance(agent.getId(), money);


        //生成提现订单
        AgentForegiftWithdrawOrder order = new AgentForegiftWithdrawOrder();
        order.setId(orderIdService.newOrderId(OrderId.OrderIdType.AGENT_FORGIFT_WITHDRAW_ORDER));
        order.setPartnerId(agent.getPartnerId());
        order.setCategory(ConstEnum.Category.RENT.getValue());
        order.setAgentId(agent.getId());
        order.setAgentName(agent.getAgentName());
        order.setAgentCode(agent.getAgentCode());
        order.setAccountType(AgentForegiftWithdrawOrder.AccountType.BALANCE.getValue());
        order.setMoney(money);
        order.setRealMoney(money);
        order.setServiceMoney(0);
        order.setStatus(AgentForegiftWithdrawOrder.Status.WITHDRAW_OK.getValue());
        order.setHandleTime(new Date());
        order.setOperator(operator);
        order.setCreateTime(new Date());
        agentForegiftWithdrawOrderMapper.insert(order);

        //运营商流水
        AgentInOutMoney inOutMoney = new AgentInOutMoney();
        inOutMoney.setAgentId(agent.getId());
        inOutMoney.setMoney(money);
        inOutMoney.setBalance(agentMapper.find(agent.getId()).getBalance());
        inOutMoney.setBizType(AgentInOutMoney.BizType.IN_AGENT_FOREGIFT_WITHDRAW.getValue());
        inOutMoney.setType(AgentInOutMoney.Type.IN.getValue());
        inOutMoney.setBizId(order.getId());
        inOutMoney.setOperator(operator);
        inOutMoney.setCreateTime(now);
        agentInOutMoneyMapper.insert(inOutMoney);


        //更新运营商押金余额 预留金额  押金余额比例
        List<Integer> statusList = Arrays.asList(CustomerForegiftOrder.Status.PAY_OK.getValue(),
                CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
        //押金余额
        int foregiftBalance = rentForegiftOrderMapper.sumMoneyByAgent(order.getAgentId(), statusList);
        //运营商押金充值
        int deposit =  agentForegiftDepositOrderMapper.sumMoney(order.getAgentId(), ConstEnum.Category.RENT.getValue(), AgentForegiftDepositOrder.Status.HAVE_PAID.getValue());
        //运营商提现
        int withdraw = agentForegiftWithdrawOrderMapper.sumMoney(order.getAgentId(), ConstEnum.Category.RENT.getValue(), AgentForegiftWithdrawOrder.Status.WITHDRAW_OK.getValue());

        //预留金额 = 押金余额 + 运营商押金充值 - 运营商押金提现
        int foregiftRemainMoney = foregiftBalance + deposit - withdraw;

        int foregiftBalanceRatio = 100;
        if(foregiftBalance != 0 ){
            foregiftBalanceRatio = foregiftRemainMoney * 100 / foregiftBalance;
        }
        if(foregiftRemainMoney < 0 ){
            foregiftBalanceRatio = 0;
        }

        //更新运营商押金
        if(agentMapper.updateZdForegift(order.getAgentId(), foregiftBalance, foregiftRemainMoney, foregiftBalanceRatio) > 0){
            //运营商押金流水
            AgentForegiftInOutMoney foregiftInOutMoney = new AgentForegiftInOutMoney();
            foregiftInOutMoney.setAgentId(order.getAgentId());
            foregiftInOutMoney.setCategory(ConstEnum.Category.RENT.getValue());
            foregiftInOutMoney.setMoney(-order.getMoney());
            foregiftInOutMoney.setBizType(AgentForegiftInOutMoney.BizType.OUT_AGENT_PAY_WITHDRAW_ORDER.getValue());
            foregiftInOutMoney.setBizId(order.getId());
            foregiftInOutMoney.setType(AgentInOutMoney.Type.OUT.getValue());
            foregiftInOutMoney.setBalance(foregiftBalance);
            foregiftInOutMoney.setRemainMoney(foregiftRemainMoney);
            foregiftInOutMoney.setRatio(foregiftBalanceRatio);
            foregiftInOutMoney.setOperator(operator);
            foregiftInOutMoney.setCreateTime(new Date());
            agentForegiftInOutMoneyMapper.insert(foregiftInOutMoney);
        }
        return ExtResult.successResult();
    }

    public void updatePayPeople(int id, String payPeopleName, String payPeopleMpOpenId, String payPeopleFwOpenId, String payPeopleMobile, String payPassword) {
        agentMapper.updatePayPeople(id, payPeopleName, payPeopleMpOpenId, payPeopleFwOpenId, payPeopleMobile, payPassword);
    }
    @Transactional(rollbackFor = Throwable.class)
    public ExtResult setPayPeopleUpdate(Agent entity) {
        Agent agentPayPeopleMobile = agentMapper.findByPayPeopleMobile(entity.getPayPeopleMobile());
        if (agentPayPeopleMobile != null){
            return ExtResult.failResult("收款人已存在");
        }
        Customer customer = customerMapper.find(entity.getCustomerId());
        Agent agent = new Agent();
        agent.setId(entity.getId());
        agent.setPayPeopleMobile(entity.getPayPeopleMobile());
        agent.setPayPeopleName(entity.getPayPeopleName());
        if (customer.getMpOpenId() != null) {
            agent.setPayPeopleMpOpenId(customer.getMpOpenId());
        }
        if (customer.getFwOpenId() != null) {
            agent.setPayPeopleFwOpenId(customer.getFwOpenId());
        }
        agentMapper.updatePayPeople(agent);
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult clearing(Integer agentId, String shopId, Integer money, String userName, String[] ids, Date beginTime, Date endTime) {
        for (String id : ids) {
             AgentShopBalanceRecordDetail resout = agentShopBalanceRecordDetailMapper.findByOrderId(id);
             if (resout != null) {
                 return ExtResult.failResult("存在重复支付订单无法提交");
             }
        }
        Agent agent = agentMapper.find(agentId);
        if (money > agent.getBalance()){
            return ExtResult.failResult("运营商余额不足");
        }
        int agentResult = agentMapper.updateBalance(agentId, -money);
        int shopResult = shopMapper.updateBalance(shopId, money);
        Shop shop = shopMapper.find(shopId);
        if (agentResult != 0 && shopResult != 0) {
            AgentInOutMoney agentInOutMoney = new AgentInOutMoney();
            agentInOutMoney.setAgentId(agentId);
            agentInOutMoney.setBizType(AgentInOutMoney.BizType.OUT_SHOP_CLEARING.getValue());
            agentInOutMoney.setType(AgentInOutMoney.Type.OUT.getValue());
            agentInOutMoney.setMoney(-money);
            agentInOutMoney.setBalance(agent.getBalance()-money);
            agentInOutMoney.setOperator(userName);
            agentInOutMoney.setCreateTime(new Date());
            agentInOutMoneyMapper.insert(agentInOutMoney);

            ShopInOutMoney shopInOutMoney = new ShopInOutMoney();
            shopInOutMoney.setShopId(shopId);
            shopInOutMoney.setBizType(ShopInOutMoney.BizType.IN_SHOP_CLEARING.getValue());
            shopInOutMoney.setType(ShopInOutMoney.Type.INCOME.getValue());
            shopInOutMoney.setMoney(money);
            shopInOutMoney.setBalance(shop.getBalance());
            shopInOutMoney.setOperator(userName);
            shopInOutMoney.setCreateTime(new Date());
            shopInOutMoneyMapper.insert(shopInOutMoney);

            AgentShopBalanceRecord agentShopBalanceRecord = new AgentShopBalanceRecord();
            agentShopBalanceRecord.setAgentId(agent.getId());
            agentShopBalanceRecord.setAgentCode(agent.getAgentCode());
            agentShopBalanceRecord.setAgentName(agent.getAgentName());
            agentShopBalanceRecord.setShopId(shopId);
            agentShopBalanceRecord.setShopName(shop.getShopName());
            agentShopBalanceRecord.setMoney(money);
            agentShopBalanceRecord.setBeginTime(beginTime);
            agentShopBalanceRecord.setEndTime(endTime);
            agentShopBalanceRecord.setCreateTime(new Date());
            agentShopBalanceRecordMapper.insert(agentShopBalanceRecord);
            long recordId = agentShopBalanceRecordMapper.findMaxId();
            for (String id : ids) {
                PacketPeriodOrder packetPeriodOrder = packetPeriodOrderMapper.find(id);
                int ratio;
                int shopFixedMoney;
                int intomoney;
                if(packetPeriodOrder.getCabinetId() != null){
                    Cabinet cabinet = cabinetMapper.find(packetPeriodOrder.getCabinetId());
                    ratio = cabinet.getShopRatio();
                    shopFixedMoney = cabinet.getShopFixedMoney() == null ? 0 : cabinet.getShopFixedMoney();
                    int ratioMoney = (int)Math.round(packetPeriodOrder.getMoney() * ratio  * 1d/100 );
                    int fixedMoney = (int)Math.round(shopFixedMoney * 1d / 30 * dealDays(packetPeriodOrder.getDayCount()));
                    intomoney = ratioMoney + fixedMoney;
                    if(intomoney > packetPeriodOrder.getMoney()){
                        intomoney = packetPeriodOrder.getMoney();
                    }
                }else {
                    ratio = shop.getShopRatio();
                    shopFixedMoney = shop.getShopFixedMoney() == null ? 0 : shop.getShopFixedMoney();
                    int ratioMoney = (int)Math.round(packetPeriodOrder.getMoney() * ratio  * 1d/100 );
                    int fixedMoney = (int)Math.round(shopFixedMoney * 1d / 30 * dealDays(packetPeriodOrder.getDayCount()));
                    intomoney = ratioMoney + fixedMoney;
                    if(intomoney > packetPeriodOrder.getMoney()){
                        intomoney = packetPeriodOrder.getMoney();
                    }
                }
                AgentShopBalanceRecordDetail agentShopBalanceRecordDetail = new AgentShopBalanceRecordDetail();
                agentShopBalanceRecordDetail.setRecordId(recordId);
                agentShopBalanceRecordDetail.setOrderId(packetPeriodOrder.getId());
                agentShopBalanceRecordDetail.setAgentId(agentId);
                agentShopBalanceRecordDetail.setAgentCode(agent.getAgentCode());
                agentShopBalanceRecordDetail.setAgentName(agent.getAgentName());
                agentShopBalanceRecordDetail.setShopId(shopId);
                agentShopBalanceRecordDetail.setShopName(shop.getShopName());
                agentShopBalanceRecordDetail.setCabinetId(packetPeriodOrder.getCabinetId());
                agentShopBalanceRecordDetail.setDayCount(packetPeriodOrder.getDayCount());
                agentShopBalanceRecordDetail.setBeginTime(packetPeriodOrder.getBeginTime());
                agentShopBalanceRecordDetail.setEndTime(packetPeriodOrder.getEndTime());
                agentShopBalanceRecordDetail.setStatus(packetPeriodOrder.getStatus());
                agentShopBalanceRecordDetail.setCustomerId(packetPeriodOrder.getCustomerId());
                agentShopBalanceRecordDetail.setCustomerFullname(packetPeriodOrder.getCustomerFullname());
                agentShopBalanceRecordDetail.setCustomerMobile(packetPeriodOrder.getCustomerMobile());
                agentShopBalanceRecordDetail.setPayType(packetPeriodOrder.getPayType());
                agentShopBalanceRecordDetail.setPayTime(packetPeriodOrder.getPayTime());
                agentShopBalanceRecordDetail.setOrderMoney(packetPeriodOrder.getMoney());
                agentShopBalanceRecordDetail.setShopRatio(ratio);
                agentShopBalanceRecordDetail.setShopFixedMoney(shopFixedMoney);
                agentShopBalanceRecordDetail.setMoney(intomoney);
                agentShopBalanceRecordDetailMapper.insert(agentShopBalanceRecordDetail);
            }
            return ExtResult.successResult();
        }else {
            return ExtResult.failResult("操作失败");
        }
    }

    private int dealDays(int days){
        int returnDays = 0;
        if(days < 30){
            returnDays = days;
        }else if(days >= 30 && days < 60){
            returnDays = 30;
        }else if(days >= 60 && days < 90){
            returnDays = 60;
        }else if(days >= 90 && days < 180){
            returnDays = 90;
        }else if(days >= 180 && days < 360){
            returnDays = 180;
        }else if(days >= 360){
            returnDays = 360;
        }

        return returnDays;
    }

}
