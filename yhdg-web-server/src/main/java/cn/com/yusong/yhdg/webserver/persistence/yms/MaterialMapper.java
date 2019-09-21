package cn.com.yusong.yhdg.webserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.Material;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MaterialMapper extends MasterMapper {
    Material find(Long id);

    public int findPageCount(Material search);

    public List<Material> findPageResult(Material search);

    int findUnique(@Param("agentId") int agentId, @Param("filePath") String filePath);

    public String hasRecordByProperty(@Param("property") String property, @Param("value") Object value);

    int insert(Material material);

    int update(Material material);

    int updateBasicInfo(Material material);

    int updateGroup(@Param("id") long id, @Param("groupId") long groupId);

    int delete(long id);
}
