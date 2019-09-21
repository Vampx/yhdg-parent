package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.WithdrawTransferLog;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface WithdrawTransferLogMapper extends MasterMapper {
    WithdrawTransferLog find(long id);

    int findPageCount(WithdrawTransferLog search);

    List<WithdrawTransferLog> findPageResult(WithdrawTransferLog search);

    int insert(WithdrawTransferLog entity);
}