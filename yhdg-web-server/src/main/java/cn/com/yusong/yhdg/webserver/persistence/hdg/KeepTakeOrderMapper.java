package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.KeepTakeOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 收电订单
 */
public interface KeepTakeOrderMapper extends MasterMapper {
    KeepTakeOrder find(String id);

    int findCountByCabinet(@Param("cabinetId") String cabinetId);

    int findPageCount(KeepTakeOrder keepTakeOrder);

    List findPageResult(KeepTakeOrder keepTakeOrder);
}
