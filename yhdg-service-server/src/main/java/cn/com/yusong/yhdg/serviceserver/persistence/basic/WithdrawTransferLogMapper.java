package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.WithdrawTransferLog;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface WithdrawTransferLogMapper extends MasterMapper {
    WithdrawTransferLog find(long id);

    List<WithdrawTransferLog> findAll();

    int findPageCount(WithdrawTransferLog search);

    List<WithdrawTransferLog> findPageResult(WithdrawTransferLog search);

    int insert(WithdrawTransferLog entity);

    int update(WithdrawTransferLog entity);

    int delete(long id);
}