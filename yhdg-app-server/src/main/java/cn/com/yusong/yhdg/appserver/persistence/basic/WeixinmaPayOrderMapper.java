package cn.com.yusong.yhdg.appserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.WeixinmaPayOrder;
import cn.com.yusong.yhdg.common.domain.basic.WeixinmpPayOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface WeixinmaPayOrderMapper extends MasterMapper{
    public int insert(WeixinmaPayOrder weixinmaPayOrder);
}
