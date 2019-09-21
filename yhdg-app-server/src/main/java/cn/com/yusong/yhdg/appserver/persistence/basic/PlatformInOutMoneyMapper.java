package cn.com.yusong.yhdg.appserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.PlatformInOutMoney;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface PlatformInOutMoneyMapper extends MasterMapper {
    public int insert(PlatformInOutMoney record);
}
