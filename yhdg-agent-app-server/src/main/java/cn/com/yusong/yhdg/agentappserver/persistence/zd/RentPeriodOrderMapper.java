package cn.com.yusong.yhdg.agentappserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface RentPeriodOrderMapper extends MasterMapper {

    RentPeriodOrder find(String id);

    List<RentPeriodOrder> findListByShop(@Param("shopId") String shopId,
                                         @Param("keyword") String keyword,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);
    RentPeriodOrder findOneEnabled(@Param("customerId") long customerId,
                                   @Param("status") Integer status,
                                   @Param("agentId") int agentId);
    RentPeriodOrder findLastEndTime(@Param("customerId") long customerId, @Param("status") Integer status);

    int countShopTodayOrderMoney(@Param("shopId") String shopId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    int findCountByShopAndStatus(@Param("shopId") String shopId, @Param("statusList") List<Integer> statusList);

    List<RentPeriodOrder> findListByShopId(@Param("agentId") Integer agentId, @Param("shopId") String shopId, @Param("keyword") String keyword, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

}
