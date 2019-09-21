package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Withdraw;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface WithdrawMapper extends MasterMapper {
    List<Withdraw> findByStatus(@Param("status") int status, @Param("offset") int offset, @Param("limit") int limit);
    List<Withdraw> findPartnerIncrement(@Param("status") int status, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
    public int transfer(@Param("id") String id, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus, @Param("mpOpenId")String mpOpenId, @Param("accountName")String accountName, @Param("handleTime") Date handleTime);
}