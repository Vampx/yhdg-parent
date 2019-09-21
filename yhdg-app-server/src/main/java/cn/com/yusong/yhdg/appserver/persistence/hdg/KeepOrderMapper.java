package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.KeepOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/11.
 */
public interface KeepOrderMapper extends MasterMapper {

    public List<KeepOrder> findTakeOrder(@Param("takeOrderId") String takeOrderId);

    public List<KeepOrder> findPutOrder(@Param("putOrderId") String putOrderId);
}
