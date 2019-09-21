package cn.com.yusong.yhdg.serviceserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.BalanceTransferOrderLog;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface BalanceTransferOrderLogMapper extends MasterMapper {
    public int insert(BalanceTransferOrderLog log);
}
