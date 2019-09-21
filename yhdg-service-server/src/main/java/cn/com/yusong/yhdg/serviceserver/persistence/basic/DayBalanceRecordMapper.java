package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.DayBalanceRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface DayBalanceRecordMapper extends MasterMapper {
    public List<Integer> sumWaitTransferRecord(@Param("status") int status);
    public List<DayBalanceRecord> findByOrg(@Param("orgId") int orgId, @Param("status") int status);
    public DayBalanceRecord findByBalanceDate(@Param("orgId") int orgId, @Param("balanceDate") String balanceDate);
    public int insert(DayBalanceRecord record);
    public int updateOrderId(@Param("id") long id, @Param("orderId") String orderId);
    public int clearOrderId(@Param("id") long id);
    public int updateHandleResult(@Param("orderId") String orderId, @Param("status") int status, @Param("handleTime") Date handleTime);
}
