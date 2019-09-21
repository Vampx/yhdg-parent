package cn.com.yusong.yhdg.appserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.CustomerRentInfo;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface CustomerRentInfoMapper extends MasterMapper {
    CustomerRentInfo find(long id);
    public int insert(CustomerRentInfo customerRentInfo);
    int updateForegift(@Param("foregiftOrderId") String foregiftOrderId, @Param("foregift") Integer foregift);
    int delete(long id);
}
