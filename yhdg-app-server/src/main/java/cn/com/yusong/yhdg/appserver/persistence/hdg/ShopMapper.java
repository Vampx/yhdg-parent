package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopMapper extends MasterMapper {
	Shop find(@Param("id") String id);

	public List<Shop> findNearestInfo(@Param("geoHash") String geoHash,
								  @Param("keyword") String keyword,
								  @Param("lng") double lng,
								  @Param("lat") double lat,
								  @Param("cityId") Integer cityId,
								  @Param("offset") int offset,
								  @Param("limit") int limit
	);

	public List<Shop> findNearest(@Param("geoHash") String geoHash,
								  @Param("keyword") String keyword,
								  @Param("lng") double lng,
								  @Param("lat") double lat,
								  @Param("cityId") Integer cityId,
								  @Param("offset") int offset,
								  @Param("limit") int limit
	);

	public Shop findShopIdDistance(
				@Param("ShopId") String ShopId,
				@Param("geoHash") String geoHash,
				@Param("lng") double lng,
				@Param("lat") double lat
	);

	public List<Shop> findAddressList();

	public List<Shop> findAddVehicleShopList();

	public int updateLocation(@Param("shopId") String shopId, @Param("provinceId") Integer provinceId, @Param("cityId") Integer cityId, @Param("districtId") Integer districtId, @Param("street") String street, @Param("lng") Double lng, @Param("lat") Double lat, @Param("geoHash") String geoHash, @Param("address") String address, @Param("keyword") String keyword, @Param("shopName") String shopName);
}
