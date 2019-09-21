package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.EstateInOutMoney;
import cn.com.yusong.yhdg.common.domain.basic.ShopInOutMoney;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface EstateInOutMoneyMapper extends MasterMapper {
    List<EstateInOutMoney> findList(@Param("estateId") long estateId,
                                  @Param("offset") int offset,
                                  @Param("limit") int limit);
    List<EstateInOutMoney> findIncome(@Param("estateId") long estateId,
                                    @Param("type") int type,
                                    @Param("offset") int offset,
                                    @Param("limit") int limit);
    int findIncomeCount(@Param("estateId") long estateId, @Param("type") int type);
    int insert(EstateInOutMoney estateInOutMoney);
}
