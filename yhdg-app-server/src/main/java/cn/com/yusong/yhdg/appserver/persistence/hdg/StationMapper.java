package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Station;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StationMapper extends MasterMapper {
    Station find(String id);

    List<Station> findListByStationId(String id);

    List<Station> findByAgent(@Param("agentId") Integer agentId);

    List<Station> findVipStationList(@Param("agentId") Integer agentId,
                                     @Param("keyword") String keyword,
                                     @Param("offset") int offset,
                                     @Param("limit") int limit
    );

    List<Station> findBatteryStationList(@Param("agentId") Integer agentId,
                                         @Param("keyword") String keyword,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit
    );

    int update(Station entity);

    int delete(String id);

    int updatePayPeople(@Param("id") String id, @Param("payPeopleName") String payPeopleName,
                        @Param("payPeopleMpOpenId") String payPeopleMpOpenId, @Param("payPeopleFwOpenId") String payPeopleFwOpenId,
                        @Param("payPeopleMobile") String payPeopleMobile, @Param("payPassword") String payPassword);

}
