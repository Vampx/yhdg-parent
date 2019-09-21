package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zhoub on 2017/10/24.
 */
public interface ShopMapper extends MasterMapper {
    Shop find(String id);

    String findMaxId(String id);

    int findPageCount(Shop shop);

    List<Shop> findPageResult(Shop shop);

    int findUnboundPageCount(Shop shop);

    List<Shop> findUnboundPageResult(Shop shop);

    int findCountByAgent(@Param("agentId") Integer agentId);

    List<Shop> findByAgent(@Param("agentId") Integer agentId);

    List<Shop> findAll();

    List<Shop> findByIsVehicle(@Param("agentId") Integer agentId, @Param("isVehicle") Integer isVehicle);

    Shop findUnique(String id);

    int insert(Shop entity);

    int update(Shop entity);

    int updateLocation(@Param("shopId") String shopId, @Param("shopName") String shopName, @Param("provinceId") Integer provinceId, @Param("cityId") Integer cityId, @Param("districtId") Integer districtId, @Param("street") String street, @Param("lng") Double lng, @Param("lat") Double lat, @Param("geoHash") String geoHash, @Param("address") String address);

    int updateRatio(@Param("id") String id, @Param("platformRatio") Integer platformRatio, @Param("agentRatio") Integer agentRatio, @Param("provinceAgentRatio") Integer provinceAgentRatio, @Param("cityAgentRatio") Integer cityAgentRatio, @Param("shopRatio") Integer shopRatio, @Param("shopFixedMoney") Integer shopFixedMoney);

    int updateBalance(@Param("id") String id, @Param("balance") long balance);

    int delete(String id);

    int clearImage1(String id);

    int clearImage2(String id);

    int updatePayPeople(@Param("id") String id, @Param("payPeopleName") String payPeopleName,
                        @Param("payPeopleMpOpenId") String payPeopleMpOpenId, @Param("payPeopleFwOpenId") String payPeopleFwOpenId,
                        @Param("payPeopleMobile") String payPeopleMobile, @Param("payPassword") String payPassword);

    int updatePrice(@Param("id") String id, @Param("minPrice") Integer minPrice, @Param("maxPrice") Integer maxPrice);
}
