package cn.com.yusong.yhdg.agentserver.persistence.zc;


import cn.com.yusong.yhdg.common.domain.zc.Vehicle;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface VehicleMapper extends MasterMapper {
    int findCountByModelId(@Param("modelId") Integer modelId);
    Vehicle find(@Param("id") int id);
    Vehicle findByVinNo(@Param("vinNo") String vinNo);
    int findPageCount(Vehicle vehicle);
    List<Vehicle> findPageResult(Vehicle vehicle);
    int findLeisure(Vehicle vehicle);
    int findInShop(Vehicle vehicle);
    int findInUse(Vehicle vehicle);
    int findByShopPageCount(Vehicle vehicle);
    List<Vehicle> findByShopPageResult(Vehicle vehicle);
    int findByShopLeisure(Vehicle vehicle);
    int findByShopInShop(Vehicle vehicle);
    int findByShopInUse(Vehicle vehicle);
    int insert(Vehicle vehicle);
    int clearUpLineTime(@Param("id") int id);
    int update(Vehicle vehicle);
    int updateLockStatus(Vehicle vehicle);
    int updateUpLineStatus(@Param("id") int id, @Param("modelId") Integer modelId, @Param("agentId") Integer agentId, @Param("agentName") String agentName, @Param("upLineStatus") Integer upLineStatus, @Param("upLineTime") Date upLineTime);
    int setShopId(@Param("id") int id, @Param("shopId") String ShopId, @Param("shopName") String shopName, @Param("status") int status);
    int delete(int id);
    int clearShop(int id);
    int clearCustomer(@Param("id") Integer id, @Param("status") int status);
}
