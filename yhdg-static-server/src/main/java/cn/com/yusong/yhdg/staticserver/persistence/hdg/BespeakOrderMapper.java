package cn.com.yusong.yhdg.staticserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.BespeakOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * Created by ruanjian5 on 2017/11/24.
 */
public interface BespeakOrderMapper extends MasterMapper {
    public BespeakOrder find(@Param("id") String id);

    public BespeakOrder findSuccessByCustomer(@Param("customerId") long customerId, @Param("status") Integer status);

    public int take(BespeakOrder bespeakOrder);
}
