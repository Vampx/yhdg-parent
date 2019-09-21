package cn.com.yusong.yhdg.staticserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.MobileMessage;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface MobileMessageMapper extends MasterMapper {
    public int insert(MobileMessage entity);
}
