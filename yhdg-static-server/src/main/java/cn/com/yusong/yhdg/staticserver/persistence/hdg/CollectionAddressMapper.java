package cn.com.yusong.yhdg.staticserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CollectionAddress;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface CollectionAddressMapper extends MasterMapper {

    public CollectionAddress find(@Param("addressId") Long addressId, @Param("customerId") Long customerId);

    public int insert(@Param("addressId") Long addressId, @Param("customerId") Long customerId);

    public int updateTime(@Param("addressId") Long addressId, @Param("customerId") Long customerId);

}
