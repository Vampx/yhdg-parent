package cn.com.yusong.yhdg.webserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.BigContent;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface BigContentMapper extends MasterMapper {
    String find(@Param("type") int type, @Param("id") long id);

    int insert(BigContent bigContent);

    int update(BigContent bigContent);

    int delete(@Param("type") int type, @Param("id") long id);
}
