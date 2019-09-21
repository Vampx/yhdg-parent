package cn.com.yusong.yhdg.appserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.BackBatteryOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * Created by ruanjian5 on 2017/11/24.
 */
public interface BackBatteryOrderMapper extends MasterMapper {
    public BackBatteryOrder find(@Param("id") String id);

    public int existLastOrder(@Param("agentId") Integer agentId, @Param("customerId") long customerId, @Param("orderStatus") Integer orderStatus);

    public BackBatteryOrder findBatteryOrder(@Param("customerId") long customerId, @Param("orderStatus") Integer orderStatus);

    public void insert(BackBatteryOrder backBatteryOrder);

    public int updateStatus(@Param("id") String id ,
                            @Param("fromStatus") Integer fromStatus,
                            @Param("toStatus") Integer toStatus,
                            @Param("cancelTime") Date cancelTime);
}
