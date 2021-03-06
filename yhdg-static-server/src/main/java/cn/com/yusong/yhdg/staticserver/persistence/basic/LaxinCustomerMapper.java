package cn.com.yusong.yhdg.staticserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.LaxinCustomer;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface LaxinCustomerMapper extends MasterMapper {

    public LaxinCustomer findByTargetMobile(@Param("targetMobile") String targetMobile);

    public int insert(LaxinCustomer lc);

    public int updateByTimes(@Param("id") long id,
                             @Param("incomeType") int incomeType,
                             @Param("laxinMoney") int laxinMoney,
                             @Param("targetCustomerId") long targetCustomerId,
                             @Param("targetFullname") String targetFullname,
                             @Param("foregiftTime") Date foregiftTime);
    public int updateByMonth(@Param("id") long id,
                             @Param("incomeType") int incomeType,
                             @Param("packetPeriodMoney") int packetPeriodMoney,
                             @Param("packetPeriodMonth") int packetPeriodMonth,
                             @Param("packetPeriodExpireTime") Date packetPeriodExpireTime,
                             @Param("targetCustomerId") long targetCustomerId,
                             @Param("targetFullname") String targetFullname,
                             @Param("foregiftTime") Date foregiftTime);


}
