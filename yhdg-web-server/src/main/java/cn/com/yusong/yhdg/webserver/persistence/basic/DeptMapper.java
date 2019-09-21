package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Dept;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeptMapper extends MasterMapper {

    public int findCount(@Param("agentId") Integer agentId);
    int hasRecordByProperty(@Param("property") String property, @Param("value") Object value);
    public Dept find(int id);
    public Dept findByParent(int id);
    public List<Dept> findByAgent(@Param("agentId") Integer agentId);
    public int findPageCount(Dept search);
    public List<Dept> findPageResult(Dept search);
    public int insert(Dept entity);
    public int update(Dept entity);
    public int delete(int id);
}
