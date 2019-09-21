package cn.com.yusong.yhdg.appserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.AlipayPayOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface AlipayPayOrderMapper extends MasterMapper{
    public int insert(AlipayPayOrder alipayPayOrder);
}
