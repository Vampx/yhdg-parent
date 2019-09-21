package cn.com.yusong.yhdg.appserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.PartnerInOutMoney;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface PartnerInOutMoneyMapper extends MasterMapper {
    int insert(PartnerInOutMoney record);
}
