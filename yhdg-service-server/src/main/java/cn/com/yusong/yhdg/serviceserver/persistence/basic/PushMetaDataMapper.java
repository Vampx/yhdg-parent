package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.PushMetaData;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PushMetaDataMapper extends MasterMapper {
    public List<PushMetaData> findList(@Param("offset") int offset, @Param("limit") int limit);
    public int insert(PushMetaData data);
    public int delete(@Param("id") long id);
}
