package cn.com.yusong.yhdg.staticserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.CustomerInOutMoney;
import cn.com.yusong.yhdg.common.domain.basic.PartnerInOutMoney;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface PartnerInOutMoneyMapper extends MasterMapper {
    public int insert(PartnerInOutMoney record);
}
