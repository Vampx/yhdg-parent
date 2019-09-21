package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface PacketPeriodOrderMapper extends MasterMapper {

    public PacketPeriodOrder find(String id);

    public int findCustomerOrderCount(@Param("customerMobile") String customerMobile, @Param("agentCompanyId") String agentCompanyId, @Param("statusList") List<Integer> statusList);

    public List<PacketPeriodOrder> findShopPacketPeriodOrderList(@Param("shopId") String shopId,
                                                                 @Param("offset") int offset,
                                                                 @Param("limit") int limit);

    public PacketPeriodOrder findRemainingTime(@Param("customerId") long customerId, @Param("status") Integer status);

    PacketPeriodOrder findByCustomer(@Param("customerId") long customerId, @Param("status") Integer status);

    public PacketPeriodOrder findOneEnabled(@Param("customerId") long customerId,
                                            @Param("status") Integer status,
                                            @Param("agentId") int agentId);

    public PacketPeriodOrder findLastEndTime(@Param("customerId") long customerId, @Param("status") Integer status);

    List<PacketPeriodOrder>  findListByNoUsed(@Param("customerId") long customerId, @Param("status") Integer status);

    List<PacketPeriodOrder> findListByCabinetId(@Param("agentId") Integer agentId,
                                                @Param("cabinetId") String cabinetId,
                                                @Param("keyword") String keyword,
                                                @Param("beginTime") Date beginTime,
                                                @Param("endTime") Date endTime);

    List<PacketPeriodOrder> findExpireList(@Param("expireTime") Date expireTime, @Param("agentId") int agentId);

    List<PacketPeriodOrder> findListByShop(@Param("shopId") String shopId,
                                           @Param("keyword") String keyword,
                                           @Param("offset") int offset,
                                           @Param("limit") int limit);

    int countShopTodayOrderMoney(@Param("shopId") String shopId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    int findCountByShopAndStatus(@Param("shopId") String shopId, @Param("statusList") List<Integer> statusList);
}
