package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Role;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper extends MasterMapper {
    public int findCount(@Param("agentId") Integer agentId);
    public Role find(int id);
    int findUnique(@Param("id") Integer id, @Param("roleName") String roleName);
    int hasRecordByProperty(@Param("property") String property, @Param("value") Object value);
    public List<Role> findByAgent(@Param("agentId") Integer agentId);
    public int findPageCount(Role search);
    public List<Role> findPageResult(Role search);
    public int findAgentPageCount(Role search);
    public List<Role> findAgentPageResult(Role search);
    public int insert(Role role);
    public int update(Role role);
    public int delete(int id);
}
