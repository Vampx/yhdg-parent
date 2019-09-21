package cn.com.yusong.yhdg.agentappserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.RentOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


/**
 * Created by ruanjian5 on 2017/11/6.
 */
public interface RentOrderMapper extends MasterMapper {

    RentOrder find(String id);

    List<RentOrder> findListByShop(@Param("agentId") Integer agentId,
                                   @Param("shopId") String shopId,
                                   @Param("keyword") String keyword,
                                   @Param("offset") int offset,
                                   @Param("limit") int limit);
    int findCountByShopId(@Param("agentId") Integer agentId,
                          @Param("shopId") String shopId);

    int findActiveUserCountByShopId(@Param("agentId") Integer agentId,
                                    @Param("shopId") String shopId,
                                    @Param("status") List<Integer> status);

    List<RentOrder> findListByAgent(@Param("agentId") int agentId,
                                   @Param("keyword") String keyword,
                                   @Param("offset") int offset,
                                   @Param("limit") int limit);

    List<RentOrder> findListByBatteryId(@Param("shopId") String shopId,
                                        @Param("batteryId") String batteryId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);

    List<RentOrder> findListByAgentBatteryId(@Param("agentId") int agentId,
                                        @Param("batteryId") String batteryId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);

    int complete(@Param("id") String id,
                 @Param("toStatus") int toStatus,
                 @Param("fromStatus") int fromStatus,
                 @Param("backTime") Date backTime,
                 @Param("backOperator")String backOperator);

    int countShopTodayOrderNum(@Param("shopId") String shopId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
