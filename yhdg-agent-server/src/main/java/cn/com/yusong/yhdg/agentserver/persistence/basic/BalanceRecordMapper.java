package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentDayBalanceRecord;
import cn.com.yusong.yhdg.common.domain.basic.BalanceRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by chen on 2017/7/9.
 */
public interface BalanceRecordMapper extends MasterMapper {
    BalanceRecord find(@Param("id") Long id);
    int findPageCount(BalanceRecord balanceRecord);
    List<BalanceRecord> findPageResult(BalanceRecord balanceRecord);
    int confirm(@Param("id") long id, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus,
                @Param("confirmTime") Date confirmTime, @Param("confirmOperator") String confirmOperator);
}
