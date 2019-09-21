package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.BalanceTransferOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface BalanceTransferOrderMapper extends MasterMapper {
    public List<BalanceTransferOrder> findByStatus(@Param("gtId") String gtId, @Param("status") int status, @Param("limit") int limit);
    public int sumMoney(@Param("idPrefix") String idPrefix, @Param("openId") String openId);
    public int insert(BalanceTransferOrder order);
    public int updateStatus(@Param("id") String id, @Param("status") int status, @Param("handleResult") String handleResult, @Param("handleTime") Date handleTime);
}
