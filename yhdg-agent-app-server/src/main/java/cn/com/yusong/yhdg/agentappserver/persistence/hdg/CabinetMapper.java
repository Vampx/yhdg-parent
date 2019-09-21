package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface CabinetMapper extends MasterMapper {
	Cabinet find(@Param("id") String id);

	List<Cabinet> findList(@Param("agentId") Integer agentId,
                           @Param("shopId") String shopId,
                           @Param("keyword") String keyword,
                           @Param("offset") Integer offset,
                           @Param("limit") Integer limit
    );

	int findCountByAgentId(@Param("agentId") Integer agentId,
						   @Param("upLineStatus") List<Integer> upLineStatus,
						   @Param("isOnLine") List<Integer> isOnLine);

	public List<Cabinet> findNearest(@Param("agentId") Integer agentId,
									 @Param("fullStatus") int fullStatus,
									 @Param("batteryStatus") int batteryStatus,
									 @Param("geoHash") String geoHash,
									 @Param("keyword") String keyword,
									 @Param("lng") double lng,
									 @Param("lat") double lat,
									 @Param("provinceId") Integer provinceId,
									 @Param("cityId") Integer cityId,
									 @Param("upLineStatus") List<Integer> upLineStatus,
									 @Param("isOnLine") List<Integer> isOnLine,
									 @Param("offset") Integer offset,
									 @Param("limit") Integer limit
	);

	List<Cabinet> findVipCabinetList(@Param("agentId") Integer agentId,
						   @Param("keyword") String keyword,
						   @Param("offset") int offset,
						   @Param("limit") int limit
	);

	List<Cabinet> findBatteryCabinetList(@Param("agentId") Integer agentId,
									 @Param("keyword") String keyword,
									 @Param("offset") int offset,
									 @Param("limit") int limit
	);

	Cabinet findUnique(String id);

	int update(Cabinet entity);

	int updateCabinetByAgent(Cabinet entity);

	int updateDynamicCode(@Param("id") String id, @Param("dynamicCode") String dynamicCode);

	int updateUpLineStatus(@Param("id") String id, @Param("upLineStatus") int upLineStatus);

	int updateLocation(@Param("cabinetId") String cabinetId, @Param("provinceId") Integer provinceId, @Param("cityId") Integer cityId, @Param("districtId") Integer districtId, @Param("street") String street, @Param("lng") Double lng, @Param("lat") Double lat, @Param("geoHash") String geoHash, @Param("address") String address, @Param("keyword") String keyword, @Param("cabinetName") String cabinetName);

	int updateLastCharger(@Param("cabinetId")String cabinetId,
						  @Param("inputDegreeNum")Integer inputDegreeNum,
						  @Param("inputDegreeMoney")Integer inputDegreeMoney,
						  @Param("inputDegreeTime")Date inputDegreeTime);

}
