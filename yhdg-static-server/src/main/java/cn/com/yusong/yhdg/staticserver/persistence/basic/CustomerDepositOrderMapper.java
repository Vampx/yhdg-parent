package cn.com.yusong.yhdg.staticserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerDepositOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface CustomerDepositOrderMapper extends MasterMapper {
    public CustomerDepositOrder find(String id);
    public int payOk(@Param("id") String id, @Param("handleTime") Date handleTime, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus);
}
