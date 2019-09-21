package cn.com.yusong.yhdg.agentserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.MaterialGroup;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MaterialGroupMapper extends MasterMapper {
    public MaterialGroup find(long id);

    public List<MaterialGroup> findByAgent(@Param("agentId") Integer agentId);

    public int findPageCount(MaterialGroup search);

    public List<MaterialGroup> findPageResult(MaterialGroup search);

    public String hasRecordByProperty(@Param("property") String parentId, @Param("value") Object value);

    List<MaterialGroup> findAll();

    public int insert(MaterialGroup group);

    public int update(MaterialGroup group);

    public int delete(long id);

}
