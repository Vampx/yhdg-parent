package cn.com.yusong.yhdg.staticserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.CustomerInOutMoney;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface CustomerInOutMoneyMapper extends MasterMapper {
    public int insert(CustomerInOutMoney record);
}
