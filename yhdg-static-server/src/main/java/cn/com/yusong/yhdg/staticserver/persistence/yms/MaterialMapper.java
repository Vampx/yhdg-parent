package cn.com.yusong.yhdg.staticserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.Material;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface MaterialMapper extends MasterMapper {
    Material find(long id);

    int findUnique(@Param("agentId") int agentId, @Param("filePath") String filePath);

    int insert(Material material);

    int update(Material material);

    int updateConvertStatus(@Param("id") long id, @Param("convertStatus") int convertStatus, @Param("md5Sum") String md5Sum);

    int updateFilePathSize(@Param("id") long id, @Param("materialName") String materialName, @Param("size") long size, @Param("filePath") String filePath);

    int updateProgress(@Param("id") long id, @Param("convertProgress") int convertProgress);
}
