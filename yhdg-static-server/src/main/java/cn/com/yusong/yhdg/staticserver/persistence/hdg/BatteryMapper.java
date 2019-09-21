package cn.com.yusong.yhdg.staticserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


public interface BatteryMapper extends MasterMapper {

    public Battery find(String id);

    public int clearCustomer(@Param("id") String id, @Param("status") int status);

    public int updateChargeStatus(@Param("id")String id,@Param("orderId")String orderId, @Param("fromChargeStatus")int fromChargeStatus, @Param("toChargeStatus")int toChargeStatus);


    public int updateCustomerUse(@Param("id") String id,
                                 @Param("status") int status,
                                 @Param("orderId") String orderId,
                                 @Param("customerOutTime") Date customerOutTime,
                                 @Param("customerId") long customerId,
                                 @Param("customerMobile") String customerMobile,
                                 @Param("customerFullname") String customerFullname
    );
}
