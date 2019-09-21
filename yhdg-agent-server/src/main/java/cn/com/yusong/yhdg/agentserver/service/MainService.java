package cn.com.yusong.yhdg.agentserver.service;

import cn.com.yusong.yhdg.agentserver.entity.SessionUser;
import cn.com.yusong.yhdg.agentserver.persistence.basic.UserMapper;
import cn.com.yusong.yhdg.agentserver.service.basic.AgentService;
import cn.com.yusong.yhdg.agentserver.service.basic.RoleService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Role;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.service.AbstractService;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * Created by zhoub on 2017/10/23.
 */
@Service
public class MainService extends AbstractService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    AgentService agentService;
    @Autowired
    RoleService roleService;

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult login(String username, String password, String authCode, HttpSession session) {
        //获取session中的验证码
    /*    String storedAuthCode = (String) session.getAttribute(Constant.SESSION_AUTH_CODE);
        if(StringUtils.isEmpty(storedAuthCode) || !storedAuthCode.equalsIgnoreCase(authCode)) {
            return ExtResult.failResult("验证码错误");
        }*/
        User Info = new User();
        Info.setLoginName(username);
        Info.setPassword(CodecUtils.password(password));
        User user = userMapper.findByLoginInfo(Info);
        if(user == null) {
            return ExtResult.failResult("用户名或密码错误");
        }
        if(user.getIsActive() == null || user.getIsActive() == ConstEnum.Flag.FALSE.getValue()) {
            return ExtResult.failResult("用户未启用");
        }
        //过滤掉门店用户、运营公司用户、物业用户
        if (!user.getLoginName().equals("admin") && user.getAccountType() != User.AccountType.AGENT.getValue()) {
            return ExtResult.failResult("当前账户不是运营商账户<br/>禁止登录");
        }
        //过滤掉运营商id为null的用户
        if (!user.getLoginName().equals("admin") && user.getAgentId() == null) {
            return ExtResult.failResult("当前账户不是运营商账户<br/>禁止登录");
        }
        userMapper.updateLoginTime(user.getId(), new Date());
        SessionUser sessionUser = new SessionUser();
        sessionUser.setId(user.getId());
        //sessionUser.setUsername(user.getLoginName());2019年9月18日17:32:13 将登陆账号LoginName改为全名Fullname
        sessionUser.setUsername(user.getFullname());
        sessionUser.setRealName(user.getFullname());
        sessionUser.setAdmin(user.getIsAdmin() == ConstEnum.Flag.TRUE.getValue());
        if(user.getAgentId() != null) {
            Agent agent = agentService.find(user.getAgentId());
            sessionUser.setAgentId(user.getAgentId());
            sessionUser.setIsExchange(agent.getIsExchange());
            sessionUser.setIsRent(agent.getIsRent());
            sessionUser.setIsVehicle(agent.getIsVehicle());
        }
        //运营商管理员用户
//        if (user.getIsProtected() == ConstEnum.Flag.TRUE.getValue()) {
//            sessionUser.setAgentProtected(Constant.AGENT_ADMIN_USER_ID == ConstEnum.Flag.TRUE.getValue());
//        }
        if (user.getLoginName().equals("admin") && user.getAgentId() == null) {
            sessionUser.setAgentProtected(Constant.AGENT_ADMIN_USER_ID == ConstEnum.Flag.TRUE.getValue());
        }

        agentService.updateAgentData(sessionUser);

        session.setAttribute(Constant.SESSION_KEY_USER, sessionUser);

        if(user.getRoleId() != null) {
            //运营商普通用户
            Role role = roleService.find(user.getRoleId());
            sessionUser.setRoleName(role.getRoleName());
            sessionUser.setPermCodeSet(roleService.loadOperCode(user.getRoleId()));
        }

        return ExtResult.successResult();
    }
}
