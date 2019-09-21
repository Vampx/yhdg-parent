package cn.com.yusong.yhdg.appserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.AlipayfwPayOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface AlipayfwPayOrderMapper extends MasterMapper {

    public AlipayfwPayOrder find(String id);

    public int insert(AlipayfwPayOrder alipayfwPayOrder);

}
