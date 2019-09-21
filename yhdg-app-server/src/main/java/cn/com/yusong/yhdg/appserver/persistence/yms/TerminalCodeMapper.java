package cn.com.yusong.yhdg.appserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.TerminalCode;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface TerminalCodeMapper extends MasterMapper {
    public TerminalCode find(@Param("id") String id);

    public int insert(TerminalCode entity);

}
