package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerCouponTicketGift;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecordPayDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CustomerCouponTicketGiftMapper extends MasterMapper {

    CustomerCouponTicketGift find(@Param("agentId") int agentId, @Param("type") int type, @Param("category") int category);

    CustomerCouponTicketGift findById(@Param("id") Long id);

    public List<CustomerCouponTicketGift> findByType(@Param("type") int type, @Param("offset") int offset, @Param("limit") int limit);

    public List<CustomerCouponTicketGift> findByTypeCategoryWagesDayAll(@Param("type") int type,
                                                        @Param("wagesDay") String wagesDay,@Param("offset") int offset, @Param("limit") int limit);

}
