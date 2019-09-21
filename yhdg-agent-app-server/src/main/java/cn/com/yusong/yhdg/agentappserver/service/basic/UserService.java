
package cn.com.yusong.yhdg.agentappserver.service.basic;


import cn.com.yusong.yhdg.agentappserver.persistence.basic.AgentCompanyMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.UserMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.EstateMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.ShopMapper;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompany;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.domain.hdg.Estate;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    ShopMapper shopMapper;
    @Autowired
    EstateMapper estateMapper;
    @Autowired
    AgentCompanyMapper agentCompanyMapper;

    public User find(long id) {
        return userMapper.find(id);
    }

    public User findByLoginName(String loginName) {
        return userMapper.findByLoginName(loginName);
    }

    public User findByMobile(String mobile) {
        return userMapper.findByMobile(mobile);
    }

    public List<User> findListByMobile(String mobile, int accountType) {
        List<User> userList = userMapper.findListByMobile(mobile, accountType);
        for(User user : userList) {
            if (user.getAgentId() != null) {
                user.setAgentName(agentMapper.find(user.getAgentId()).getAgentName());
            }
        }
        return userList;
    }

    public List<User> findTypeUserList(String mobile) {
        List<User> userList = userMapper.findTypeUserList(mobile);
        for(User user : userList) {
            if (user.getAgentId() != null) {
                user.setAgentName(agentMapper.find(user.getAgentId()).getAgentName());
            }
            if (user.getShopId() != null) {
                Shop shop = shopMapper.find(user.getShopId());
                if (shop != null) {
                    user.setShopName(shop.getShopName());
                }
            }
            if (user.getAgentCompanyId() != null) {
                AgentCompany agentCompany = agentCompanyMapper.find(user.getAgentCompanyId());
                if (agentCompany != null) {
                    user.setAgentCompanyName(agentCompany.getCompanyName());
                }
            }
            if (user.getEstateId() != null) {
                Estate estate = estateMapper.find(user.getEstateId());
                if (estate != null) {
                    user.setEstateName(estate.getEstateName());
                }
            }
        }
        return userList;
    }

    public List<User> findShopUserListByMobile(String mobile, int accountType) {
        List<User> userList = userMapper.findShopListByMobile(mobile, accountType);
        for(User user : userList) {
            if (user.getShopId() != null) {
                user.setShopName(shopMapper.find(user.getShopId()).getShopName());
            }
        }
        return userList;
    }

    public List<User> findAgentCompanyUserListByMobile(String mobile, int accountType) {
        List<User> userList = userMapper.findAgentCompanyListByMobile(mobile, accountType);
        for(User user : userList) {
            if (user.getAgentCompanyId() != null) {
                user.setAgentCompanyName(agentCompanyMapper.find(user.getAgentCompanyId()).getCompanyName());
            }
        }
        return userList;
    }

    public List<User> findEstateUserListByMobile(String mobile, int accountType) {
        List<User> userList = userMapper.findEstateListByMobile(mobile, accountType);
        for(User user : userList) {
            if (user.getEstateId() != null) {
                user.setEstateName(estateMapper.find(user.getEstateId()).getEstateName());
            }
        }
        return userList;
    }

    public List<User> findListByAgentId(Integer agentId, Integer accountType, String shopId, String agentCompanyId) {
        return userMapper.findListByAgentId(agentId, accountType, shopId, agentCompanyId);
    }

    public List<User> findList(Integer agentId, int offset, int limit) {
        return userMapper.findList(agentId, offset, limit);
    }

    public int insert(User user){
        return userMapper.insert(user);
    }

    public int findAgentUserCount(Integer agentId) {
        return userMapper.findAgentUserCount(agentId);
    }

    public int updatePassword(long id, String oldPassword, String newPassword) {
        return userMapper.updatePassword(id, oldPassword, newPassword);
    }

    public int updatePassword2(long id, String newPassword) {
        return userMapper.updatePassword2(id, newPassword);
    }

    public int updateMobile(long id, String mobile) {
        return userMapper.updateMobile(id, mobile);
    }

    public int updateLoginTime(long id, Date loginTime) {
        return userMapper.updateLoginTime(id, loginTime);
    }

}


