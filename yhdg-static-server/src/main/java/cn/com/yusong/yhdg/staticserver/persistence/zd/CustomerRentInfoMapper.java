package cn.com.yusong.yhdg.staticserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.CustomerRentInfo;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface CustomerRentInfoMapper extends MasterMapper {
    CustomerRentInfo find(long id);
    public int insert(CustomerRentInfo customerRentInfo);
    int updateForegift(@Param("foregiftOrderId") String foregiftOrderId, @Param("foregift") Integer foregift);
    int delete(int id);
}
