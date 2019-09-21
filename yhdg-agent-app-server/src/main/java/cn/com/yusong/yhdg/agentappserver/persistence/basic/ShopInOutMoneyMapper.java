package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.ShopInOutMoney;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ShopInOutMoneyMapper extends MasterMapper {
    List<ShopInOutMoney> findList(@Param("shopId") String shopId,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);
    List<ShopInOutMoney> findIncome(@Param("shopId") String shopId,
                                    @Param("type") int type,
                                    @Param("offset") int offset,
                                    @Param("limit") int limit);
    int findIncomeCount(@Param("shopId") String shopId,
                                    @Param("type") int type);
    int insert(ShopInOutMoney shopInOutMoney);
}
