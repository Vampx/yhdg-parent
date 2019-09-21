package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.PointAddress;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Administrator on 2017/11/27.
 */
public interface PointAddressMapper extends MasterMapper {

    public PointAddress find(@Param("point") String point);

    public int create(@Param("point") String point,@Param("address") String address);
}
