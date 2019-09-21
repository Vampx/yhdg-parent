package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerDepositOrder;
import cn.com.yusong.yhdg.common.domain.hdg.PlatformDayStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface CustomerDepositOrderMapper extends MasterMapper {
    public CustomerDepositOrder find(@Param("id") String id);

    public PlatformDayStats findIncrement(@Param("status") Integer status, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    public PlatformDayStats findTotal(@Param("status") Integer status);

    public PlatformDayStats findIncrementRefund(@Param("status") Integer status, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    public PlatformDayStats findTotalRefund(@Param("status") Integer status);
}
