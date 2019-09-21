package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ReliefStation;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/7.
 */
public interface ReliefStationMapper extends MasterMapper {
    public List<ReliefStation> findList(@Param("partnerId") Integer partnerId,
                                        @Param("cityId") Integer cityId,
                                        @Param("lng") double lng,
                                        @Param("lat") double lat,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit
    );
}
