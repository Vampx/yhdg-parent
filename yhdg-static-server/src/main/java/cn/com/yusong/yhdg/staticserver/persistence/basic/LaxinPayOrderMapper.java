package cn.com.yusong.yhdg.staticserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.LaxinPayOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface LaxinPayOrderMapper extends MasterMapper {
    public LaxinPayOrder find(@Param("id") String id);
    public int payOk(@Param("id") String id, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus, @Param("payTime") Date payTime);

}
