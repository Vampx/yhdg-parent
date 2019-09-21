package cn.com.yusong.yhdg.agentappserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface RentForegiftOrderMapper extends MasterMapper {

    RentForegiftOrder find(String id);
    List<RentForegiftOrder> findByForegiftIdAndStatus(@Param("foregiftId") Long foregiftId, @Param("foregiftList") List<Integer> foregiftList);
    int sumMoneyByAgent(@Param("agentId") int agentId, @Param("status") List<Integer> status);
    int findCountByCustomerId(@Param("id") String id, @Param("agentId") int agentId, @Param("customerId") long customerId, @Param("status") int status);
    List<RentForegiftOrder> findListByShopId(@Param("agentId") Integer agentId, @Param("shopId") String shopId, @Param("keyword") String keyword, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
    List<RentForegiftOrder> findByShopId(@Param("agentId") Integer agentId,
                                         @Param("shopId") String shopId,
                                         @Param("status") Integer status,
                                         @Param("keyword") String keyword,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);
    List<RentForegiftOrder> findListByCustomerIdAndStatus(@Param("customerId") long customerId, @Param("status") int status);
    int insert(RentForegiftOrder RentForegiftOrder);
    int refund(@Param("id") String id, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus, @Param("applyRefundTime") Date applyRefundTime, @Param("refundTime") Date refundTime, @Param("refundMoney") Integer refundMoney, @Param("refundOperator") String refundOperator);
    int applyRefund(@Param("id") String id, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus, @Param("applyRefundTime") Date applyRefundTime, @Param("refundMoney") Integer refundMoney);
    List<RentForegiftOrder> findIncrement(@Param("agentId") Integer agentId,
                                          @Param("status") Integer status,
                                          @Param("beginTime") Date beginTime,
                                          @Param("endTime") Date endTime,
                                          @Param("offset") int offset,
                                          @Param("limit") int limit);
    List<RentForegiftOrder> findRefund(@Param("agentId") Integer agentId,
                                       @Param("status") Integer status,
                                       @Param("beginTime") Date beginTime,
                                       @Param("endTime") Date endTime,
                                       @Param("offset") int offset,
                                       @Param("limit") int limit);
    int countShopTodayOrderNum(@Param("shopId") String shopId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
    List<RentForegiftOrder> findByShopId(@Param("agentId") Integer agentId,
                                         @Param("shopId") String shopId,
                                         @Param("status") List<Integer> status,
                                         @Param("keyword") String keyword,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);
    int findCountByShopId(@Param("shopId") String shopId,
                          @Param("agentId") Integer agentId,
                          @Param("status") List<Integer> status,
                          @Param("beginTime") Date beginTime,
                          @Param("endTime") Date endTime);
    RentForegiftOrder findLastEndTime(@Param("customerId") long customerId, @Param("status") Integer status);

}
