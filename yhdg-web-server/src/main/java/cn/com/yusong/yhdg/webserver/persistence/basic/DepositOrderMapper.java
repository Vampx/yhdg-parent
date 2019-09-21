package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.DepositOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface DepositOrderMapper extends MasterMapper {
    public DepositOrder find(String id);
    public int findPageCount(DepositOrder search);
    public List<DepositOrder> findPageResult(DepositOrder search);
    public int insert(DepositOrder deposit);
    public int payOk(@Param("id") String id, @Param("handleTime") Date handleTime, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus);
}
