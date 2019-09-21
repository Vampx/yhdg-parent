package cn.com.yusong.yhdg.agentappserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.ShopDepositOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopDepositOrderMapper extends MasterMapper {
    List<ShopDepositOrder> findList(@Param("shopId") String shopId,
                                    @Param("offset") int offset,
                                    @Param("limit") int limit);
    int insert(ShopDepositOrder shopDepositOrder);
}
