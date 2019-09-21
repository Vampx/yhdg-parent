package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Station;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StationMapper extends MasterMapper {
    Station find(String id);

    List<Station> findIds (String [] array);

    List<Station> findListByStationId(String id);

    String findMaxId(String id);

    int findPageCount(Station station);

    List<Station> findPageResult(Station station);

    List<Station> findByAgent(@Param("agentId") Integer agentId);

    List<Station> findAll();

    Station findUnique(String id);

    int insert(Station entity);

    int update(Station entity);

    int updateLocation(@Param("stationId") String stationId, @Param("stationName") String stationName, @Param("provinceId") Integer provinceId, @Param("cityId") Integer cityId, @Param("districtId") Integer districtId, @Param("street") String street, @Param("lng") Double lng, @Param("lat") Double lat, @Param("geoHash") String geoHash, @Param("address") String address);

    int delete(String id);

    int updatePayPeople(@Param("id") String id, @Param("payPeopleName") String payPeopleName,
                        @Param("payPeopleMpOpenId") String payPeopleMpOpenId, @Param("payPeopleFwOpenId") String payPeopleFwOpenId,
                        @Param("payPeopleMobile") String payPeopleMobile, @Param("payPassword") String payPassword);

    int findPageMentStationCount(Station station);

    List<Station> findPageMentStationResult(Station station);

    int findPageMentStationCountNum(Station station);

    List<Station> findPageMentStationResultNum(Station station);
}
