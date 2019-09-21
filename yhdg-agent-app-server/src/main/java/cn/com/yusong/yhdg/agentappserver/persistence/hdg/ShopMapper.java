package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopMapper extends MasterMapper {
	Shop find(@Param("id") String id);

	String findMaxId(String id);

	List<Shop> findByAgentId(@Param("agentId")Integer agentId,
							 @Param("keyword")String keyword,
							 @Param("lat")Double lat,
							 @Param("lng")Double lng,
							 @Param("offset") int offset,
							 @Param("limit") int limit);
	List<Shop> findHdVipShopList(@Param("agentId") Integer agentId,
							   @Param("keyword") String keyword,
							   @Param("offset") int offset,
							   @Param("limit") int limit
	);
	List<Shop> findZdVipShopList(@Param("agentId") Integer agentId,
								 @Param("keyword") String keyword,
								 @Param("offset") int offset,
								 @Param("limit") int limit
	);

	int findShopCount(@Param("agentId") Integer agentId);

	int updatePayPassword(@Param("id") String id, @Param("payPassword")String payPassword);

	int updateBalance(@Param("id") String id, @Param("balance") int balance);

	List<Shop> findList(@Param("agentId") Integer agentId,
                        @Param("keyword") String keyword,
                        @Param("offset") Integer offset,
                        @Param("limit") Integer limit
    );

	int insert(Shop shop);

	int updateInfo(Shop shop);

	int delete(@Param("id") String id);
}
