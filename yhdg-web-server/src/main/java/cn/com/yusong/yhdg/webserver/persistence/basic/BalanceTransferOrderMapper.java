package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.BalanceTransferOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by chen on 2017/7/7.
 */
public interface BalanceTransferOrderMapper extends MasterMapper {
    BalanceTransferOrder find(String id);
    int findPageCount(BalanceTransferOrder balanceTransferOrder);
    List<BalanceTransferOrder> findPageResult(BalanceTransferOrder balanceTransferOrder);
    int reset(@Param("id") String id, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus, @Param("openId") String openId, @Param("fullName") String fullName);
    int insert(BalanceTransferOrder balanceTransferOrder);
}
