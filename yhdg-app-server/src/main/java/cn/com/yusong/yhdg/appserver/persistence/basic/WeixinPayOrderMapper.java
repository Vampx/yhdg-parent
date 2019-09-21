package cn.com.yusong.yhdg.appserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.WeixinPayOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface WeixinPayOrderMapper extends MasterMapper{
    public int insert(WeixinPayOrder weixinPayOrder);
}
