package cn.com.yusong.yhdg.serviceserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.CustomerInOutMoney;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface CustomerInOutMoneyMapper extends MasterMapper {
    public int insert(CustomerInOutMoney record);
}
