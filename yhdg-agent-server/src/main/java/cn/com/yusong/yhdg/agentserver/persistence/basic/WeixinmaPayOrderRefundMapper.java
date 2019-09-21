package cn.com.yusong.yhdg.agentserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.WeixinmaPayOrderRefund;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface WeixinmaPayOrderRefundMapper extends MasterMapper{
    public int findPageCount(WeixinmaPayOrderRefund search);
    public List<WeixinmaPayOrderRefund> findPageResult(WeixinmaPayOrderRefund search);
    public int insert(WeixinmaPayOrderRefund weixinmaPayOrderRefund);
}
