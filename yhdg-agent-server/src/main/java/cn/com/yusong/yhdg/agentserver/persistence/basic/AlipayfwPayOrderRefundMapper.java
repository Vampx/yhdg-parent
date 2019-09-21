package cn.com.yusong.yhdg.agentserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.AlipayfwPayOrderRefund;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface AlipayfwPayOrderRefundMapper extends MasterMapper{
    public int findPageCount(AlipayfwPayOrderRefund alipayfwPayOrderRefund);

    public List<AlipayfwPayOrderRefund> findPageResult(AlipayfwPayOrderRefund alipayfwPayOrderRefund);

    public int insert(AlipayfwPayOrderRefund alipayfwPayOrderRefund);
}
