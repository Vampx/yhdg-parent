package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Withdraw;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WithdrawMapper extends MasterMapper {
    Withdraw find(@Param("id") String id);

    List<Withdraw> findByCustomer(@Param("type")int type, @Param("customerId")long customerId, @Param("offset")int offset, @Param("limit")int limit);

    int insert(Withdraw entity);
}
