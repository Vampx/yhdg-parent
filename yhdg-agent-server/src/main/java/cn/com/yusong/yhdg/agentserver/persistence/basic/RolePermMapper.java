package cn.com.yusong.yhdg.agentserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.RolePerm;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface RolePermMapper extends MasterMapper {

    List<String> findAll(Integer roleId);
    int insert(RolePerm rolePerm);
    int delete(int roleId);
}
