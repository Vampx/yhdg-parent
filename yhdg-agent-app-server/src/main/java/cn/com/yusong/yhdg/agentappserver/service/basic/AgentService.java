package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.config.AppConfig;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.AgentSystemConfigMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.UserMapper;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.entity.tree.Node;
import cn.com.yusong.yhdg.common.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

@Service
public class AgentService extends AbstractService {

    @Autowired
    AgentMapper agentMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    AreaCache areaCache;
    @Autowired
    AppConfig config;
    @Autowired
    AgentSystemConfigMapper agentSystemConfigMapper;

    public Agent find(int id) {
        Agent agent = (Agent) setAreaProperties(areaCache, agentMapper.find(id));
        return agent;
    }

    public List<Agent> findAll(String agentName) {
        return agentMapper.findAll(agentName);
    }

    public List<Agent> findByParent(int parentId) {
        return agentMapper.findByParent(parentId);
    }

    public List<Integer> findAllId() {
        return agentMapper.findAllId();
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult create(Agent agent, User[] userList) {
        agentMapper.insert(agent);

        String agentId = agent.getId().toString();
        for(String sql : config.agentSystemConfigSql) {
            agentSystemConfigMapper.insert(sql.replace("1/*agent_id*/", agentId));
        }

        if (userList != null && userList.length > 0) {
            for (User agentUser : userList) {
                agentUser.setAgentId(agent.getId());
                agentUser.setAccountType(User.AccountType.AGENT.getValue());
                agentUser.setIsAdmin(ConstEnum.Flag.TRUE.getValue());
                agentUser.setIsProtected(ConstEnum.Flag.FALSE.getValue());
                agentUser.setIsActive(ConstEnum.Flag.TRUE.getValue());
                agentUser.setCreateTime(new Date());
                userMapper.insert(agentUser);
            }

        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult update(Agent agent, User[] addUserList, User[] updateUserList, Long[] deleteUserList) {
        agentMapper.update(agent);

        if (addUserList != null && addUserList.length > 0) {
            for (User agentUser : addUserList) {
                agentUser.setAgentId(agent.getId());
                agentUser.setAccountType(User.AccountType.AGENT.getValue());
                agentUser.setIsAdmin(ConstEnum.Flag.TRUE.getValue());
                agentUser.setIsProtected(ConstEnum.Flag.FALSE.getValue());
                agentUser.setIsActive(ConstEnum.Flag.TRUE.getValue());
                agentUser.setCreateTime(new Date());
                userMapper.insert(agentUser);
            }
        }

        if (updateUserList != null && updateUserList.length > 0) {
            for (User agentUser : updateUserList) {
                User user = userMapper.findByLoginName(agentUser.getLoginName());
                if (user != null && user.getId().longValue() != agentUser.getId().longValue()) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "登录名" + agentUser.getLoginName() + "已存在");
                }
                userMapper.update(agentUser);
            }
        }

        if (deleteUserList != null && deleteUserList.length > 0) {
            for (Long userId : deleteUserList) {
                userMapper.delete(userId);
            }
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

    @Transactional(rollbackFor = Throwable.class)
    public int setPayPassword(Integer id, String password) {
        return agentMapper.updatePayPassword(id, password);
    }


    public List<Node<Agent>> buildLevelList(Integer agentId) {
        Agent agent = agentMapper.find(agentId);
        Agent e = agent;
        Agent parent = null;
        Stack<Agent> agentStack = new Stack<Agent>();

        while (e != null) {
            agentStack.add(e);
            if(e.getParentId() != null) {
                Agent temp = agentMapper.find(e.getParentId());
                if(e == agent) {
                    parent = temp;
                }

                e = temp;
            } else {
                e = null;
            }
        }

        int level = 0;
        while (!agentStack.isEmpty()) {
            agentStack.pop();
            level++;
        }
        agent.setLevel(level);

        Node<Agent> parentNode = new Node<Agent>(parent);
        Node<Agent> root = null;
        if(parent == null) {
            root = new Node<Agent>(agent);
        } else {
            root = new Node<Agent>(agent, parentNode);
        }

        privateBuildLevelTree(root);

        List<Node<Agent>> list = new ArrayList<Node<Agent>>();
        buildLevelList0(root, list);
        return list;
    }

    private void buildLevelList0(Node<Agent> node, List<Node<Agent>> list) {
        list.add(node);
        for(Node<Agent> child : node.getChildren()) {
            buildLevelList0(child, list);
        }
    }

    private void privateBuildLevelTree(Node<Agent> parent) {
        List<Agent> agentList = agentMapper.findByParent(parent.getData().getId());
        for(Agent agent : agentList) {
            agent.setLevel(parent.getData().getLevel() + 1);
            Node<Agent> node = new Node(agent, parent);
            privateBuildLevelTree(node);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult updatePayPeople(Integer id, String payPeopleMobile, String payPeopleName, String payPeopleMpOpenId, String payPeopleFwOpenId) {
        Agent agent = new Agent();
        agent.setId(id);
        agent.setPayPeopleMobile(payPeopleMobile);
        agent.setPayPeopleName(payPeopleName);
        if (payPeopleMpOpenId != null) {
            agent.setPayPeopleMpOpenId(payPeopleMpOpenId);
        }
        if (payPeopleFwOpenId != null) {
            agent.setPayPeopleFwOpenId(payPeopleFwOpenId);
        }
        agentMapper.updatePayPeople(agent);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }
}
