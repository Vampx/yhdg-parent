package cn.com.yusong.yhdg.frontserver.persistence.yms;

import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface BigContentMapper extends MasterMapper {
    String find(@Param("type") int type, @Param("id") long id);
}
