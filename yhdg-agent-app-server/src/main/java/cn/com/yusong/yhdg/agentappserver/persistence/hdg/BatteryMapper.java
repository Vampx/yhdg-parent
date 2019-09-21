package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface BatteryMapper extends MasterMapper {

	List<Battery> findAgentBatteryList(@Param("agentId") Integer agentId,
									   @Param("category") Integer category,
                                       @Param("keyword") String keyword,
									   @Param("isNormal") List<Integer> isNormal,
									   @Param("status") List<Integer> status,
									   @Param("upLineStatus") List<Integer> upLineStatus,
									   @Param("lowDay") Integer lowDay,
									   @Param("highDay") Integer highDay,
									   @Param("lowCircle") Integer lowCircle,
									   @Param("highCircle") Integer highCircle,
                                       @Param("offset") Integer offset,
                                       @Param("limit") Integer limit
    );

	Battery findByImei(@Param("code") String code);

	Battery findByShellCode(@Param("shellCode") String shellCode);

	//查询门店库存电池
	List<Battery> shopStoreList(@Param("shopId") String shopId,
								@Param("keyword") String keyword,
								@Param("offset") Integer offset,
								@Param("limit") Integer limit
	);
	int shopStoreCount(@Param("agentId") Integer agentId, @Param("shopId") String shopId);
	//查询门店客户使用电池
	List<Battery> shopCustomerUseList(@Param("shopId") String shopId,
								      @Param("keyword") String keyword,
								      @Param("offset") Integer offset,
								      @Param("limit") Integer limit
	);

	int shopCustomerUseCount(@Param("agentId") Integer agentId, @Param("shopId") String shopId);
	//查询门店柜子电池
	List<Battery> shopCabinetList(@Param("shopId") String shopId,
								  @Param("keyword") String keyword,
								  @Param("offset") Integer offset,
								  @Param("limit") Integer limit
	);
	//查询库存电池
	List<Battery> agentShopList(@Param("agentId") Integer agentId,
								@Param("category") Integer category,
								@Param("keyword") String keyword,
								@Param("offset") Integer offset,
								@Param("limit") Integer limit
	);

	//查询其他电池
	List<Battery> agentRestsList(@Param("agentId") Integer agentId,
								 @Param("category") Integer category,
								 @Param("keyword") String keyword,
								 @Param("offset") Integer offset,
								 @Param("limit") Integer limit
	);

	Battery find(@Param("id") String id);

	List<Battery> findByCode(@Param("code") String code);

	int findUniqueCode(@Param("code") String code, @Param("id") String id);

	int findQrcode(@Param("qrcode") String qrcode, @Param("id") String id);

	int findFreeOutTimeCount(@Param("agentId") int agentId, @Param("status") Integer status, @Param("freeOutTime") Date freeOutTime);

	List<Battery>  findFreeOutTimeList(@Param("agentId") int agentId, @Param("status") Integer status, @Param("freeOutTime") Date freeOutTime,
							 @Param("offset") Integer offset,
							 @Param("limit") Integer limit);

	int updateUpLineStatus(@Param("type") int type, @Param("agentId") int agentId, @Param("id") String id, @Param("upLineStatus") int upLineStatus, @Param("category")int category, @Param("upLineTime") Date upLineTime);

	int updateLockSwitch(@Param("id")String id, @Param("lockSwitch")int lockSwitch);

	int clearCustomer(@Param("id") String id, @Param("status") int status);

	int updateOrderId(@Param("id") String id,
					  @Param("status") int status,
					  @Param("orderId") String orderId,
					  @Param("customerOutTime") Date customerOutTime,
					  @Param("customerId") long customerId,
					  @Param("customerMobile") String customerMobile,
					  @Param("customerFullname") String customerFullname
	);

	int updateShopInfo(@Param("id") String id, @Param("shopId") String shopId, @Param("shopName") String shopName);

    int countShopCabinetBattery(String shopId);

	int countShopCustomerUseNum(String shopId);

	int changeIsNormal(@Param("id") String id,
					   @Param("isNormal") Integer isNormal,
					   @Param("abnormalCause") String abnormalCause,
					   @Param("operator") String operator,
					   @Param("operatorTime") Date operatorTime);

}
