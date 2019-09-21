package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerForegiftOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/6.
 */
public interface CustomerForegiftOrderMapper extends MasterMapper {

    CustomerForegiftOrder find(@Param("id") String id);

    int sumMoneyByAgent( @Param("agentId") int agentId, @Param("status") List<Integer> status);

    List<CustomerForegiftOrder> findListByCabinetId(@Param("agentId") Integer agentId,
                                                    @Param("cabinetId") String cabinetId,
                                                    @Param("keyword") String keyword,
                                                    @Param("beginTime") Date beginTime,
                                                    @Param("endTime") Date endTime);

    List<CustomerForegiftOrder> findByCabinetId(@Param("agentId") Integer agentId,
                                                @Param("cabinetId") String cabinetId,
                                                @Param("status") List<Integer> status,
                                                @Param("keyword") String keyword,
                                                @Param("offset") int offset,
                                                @Param("limit") int limit);

    List<CustomerForegiftOrder> findByForegiftIdAndStatus(@Param("foregiftId") Long foregiftId, @Param("foregiftList") List<Integer> foregiftList);

    List<CustomerForegiftOrder> findListByCustomerIdAndStatus(@Param("customerId") long customerId, @Param("status") int status);

    List<CustomerForegiftOrder> findIncrement(@Param("agentId") Integer agentId, @Param("status") Integer status, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("offset") int offset,
                                              @Param("limit") int limit);

    List<CustomerForegiftOrder> findRefund(@Param("agentId") Integer agentId, @Param("status") Integer status, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("offset") int offset,
                                           @Param("limit") int limit);

    int refund(@Param("id") String id, @Param("fromStatus")int fromStatus, @Param("toStatus")int toStatus, @Param("applyRefundTime") Date applyRefundTime, @Param("refundTime") Date refundTime, @Param("refundMoney")Integer refundMoney, @Param("refundOperator")String refundOperator);

    int applyRefund(@Param("id") String id, @Param("fromStatus")int fromStatus, @Param("toStatus")int toStatus, @Param("applyRefundTime") Date applyRefundTime, @Param("refundMoney")Integer refundMoney);

    int countShopTodayOrderNum(@Param("shopId") String shopId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    int findCountByCabinetId(@Param("cabinetId") String cabinetId,
                             @Param("agentId") Integer agentId,
                             @Param("status") List<Integer> status,
                             @Param("beginTime") Date beginTime,
                             @Param("endTime") Date endTime);

    public CustomerForegiftOrder findOneEnabled(@Param("customerId") long customerId,
                                                @Param("status") Integer status,
                                                @Param("agentId") int agentId);
}
