package cn.com.yusong.yhdg.agentappserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.WeixinmpPayOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface WeixinmpPayOrderMapper extends MasterMapper{
    public int insert(WeixinmpPayOrder weixinmpPayOrder);
}
