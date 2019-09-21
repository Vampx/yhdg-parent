package cn.com.yusong.yhdg.webserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.WeixinPayOrderRefund;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface WeixinPayOrderRefundMapper extends MasterMapper{
    public int findPageCount(WeixinPayOrderRefund search);
    public List<WeixinPayOrderRefund> findPageResult(WeixinPayOrderRefund search);
    public int insert(WeixinPayOrderRefund weixinPayOrderRefund);
}
