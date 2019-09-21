package cn.com.yusong.yhdg.webserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.AlipayPayOrderRefund;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface AlipayPayOrderRefundMapper extends MasterMapper{
    public int findPageCount(AlipayPayOrderRefund alipayPayOrderRefund);

    public List<AlipayPayOrderRefund> findPageResult(AlipayPayOrderRefund alipayPayOrderRefund);

    public int insert(AlipayPayOrderRefund alipayPayOrderRefund);
}
