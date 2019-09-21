package cn.com.yusong.yhdg.webserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.WeixinmpPayOrderRefund;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface WeixinmpPayOrderRefundMapper extends MasterMapper{
    public int findPageCount(WeixinmpPayOrderRefund search);
    public List<WeixinmpPayOrderRefund> findPageResult(WeixinmpPayOrderRefund search);
    public int insert(WeixinmpPayOrderRefund weixinmpPayOrderRefund);
}
