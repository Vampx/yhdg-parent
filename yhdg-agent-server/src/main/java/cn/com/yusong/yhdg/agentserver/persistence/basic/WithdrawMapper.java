package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Withdraw;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface WithdrawMapper extends MasterMapper {
    Withdraw find(String id);

    int findPageCount(Withdraw search);

    List<Withdraw> findPageResult(Withdraw search);

    int audit(@Param("id") String id, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus, @Param("auditTime") Date auditTime,
              @Param("auditUser") String auditUser, @Param("auditMemo") String auditMemo);

    int reset(@Param("id") String id, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus, @Param("auditUser") String auditUser,
                    @Param("auditTime") Date auditTime, @Param("accountName") String accountName,
                    @Param("weixinAccount") String weixinAccount, @Param("alipayAccount") String alipayAccount, @Param("wxOpenId") String wxOpenId);

    int insert(Withdraw withdraw);
}