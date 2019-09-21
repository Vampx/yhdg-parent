package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface BatteryOrderMapper extends MasterMapper {


    public BatteryOrder find(String id);

    public int findCountByCustomer(@Param("customerId") long customerId, @Param("idPrefix")String idPrefix);

    public List<BatteryOrder> findListByCustomer(@Param("customerId") long customerId, @Param("orderStatus") Integer orderStatus, @Param("offset") Integer offset, @Param("limit") Integer limit);

    public List<BatteryOrder> findListByCustomerAndOrderId(@Param("customerId") long customerId, @Param("orderId") String orderId, @Param("offset") int offset, @Param("limit") int limit);

    public List<BatteryOrder> findByPacketOrderId(@Param("packetOrderId") String packetOrderId, @Param("customerId") long customerId, @Param("offset") int offset, @Param("limit") int limit);

    public int insert(BatteryOrder batteryOrder);

    public int updateMoney(@Param("id") String id, @Param("payType") int payType, @Param("price") int price, @Param("money") int money, @Param("ticketMoney") Integer ticketMoney, @Param("ticketName") String ticketName, @Param("couponTicketId") Long couponTicketId);

    public int payOk(@Param("id") String id, @Param("fromStatus") Integer fromStatus, @Param("toStatus") int toStatus,
                     @Param("payType") int payType, @Param("payTime") Date payTime, @Param("price") int price, @Param("money") int money,
                     @Param("ticketMoney") Integer ticketMoney, @Param("ticketName") String ticketName, @Param("couponTicketId") Long couponTicketId);

    public int payOk2(@Param("id") String id,
                      @Param("orderStatus") int orderStatus,
                      @Param("payType") int payType,
                      @Param("payTime") Date payTime,
                      @Param("packetPeriodOrderId") String packetPeriodOrderId,
                      @Param("price") Integer price,
                      @Param("money") Integer money);

    int updateStatus(@Param("id") String id,
                     @Param("refundStatus") int refundStatus,
                     @Param("refundMoney") int refundMoney,
                     @Param("refundTime") Date refundTime,
                     @Param("refundReason") String refundReason);

    public int updatePrice(@Param("id") String id, @Param("price") Integer price, @Param("money") Integer money);

    public int updateErrorMessage(@Param("id") String id, @Param("errorTime") Date errorTime, @Param("errorMessage") String errorMessage);

    int updateFaultLog(@Param("property") String property, @Param("value") Object value);
}
