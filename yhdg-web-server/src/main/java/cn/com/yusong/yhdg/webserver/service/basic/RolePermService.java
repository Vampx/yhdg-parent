package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.webserver.persistence.basic.RolePermMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolePermService {

    @Autowired
    RolePermMapper rolePermMapper;

    public List<String> findAll(int roleId) {
        return rolePermMapper.findAll(roleId);
    }

    public int delete(int roleId) {
        return rolePermMapper.delete(roleId);
    }
}
