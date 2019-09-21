package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by zhoub on 2017/10/24.
 */
public interface CabinetMapper extends MasterMapper {
    Cabinet find(String id);

    List<Cabinet> findIds (String [] array);

    List<Cabinet> findListByShopId(@Param("shopId") String shopId);

    List<Cabinet> findListByAgentAndBatteryType(@Param("agentId")Integer agnetId,@Param("batteryType") Integer batteryType);

    List<Cabinet> findListByEstateId(@Param("estateId") long estateId);

    List<Cabinet> findListByBatteryType(Integer batteryType);

    List<Cabinet> findByAgent(@Param("agentId") long agentId,
                              @Param("dispatcherId") Long dispatcherId,
                              @Param("provinceId") Integer provinceId,
                              @Param("cityId") Integer cityId,
                              @Param("districtId") Integer districtId);

    List<Cabinet> findByAgentIdList(@Param("agentId") Integer agentId);

    List<Cabinet> findAll();

    int findPageCount(Cabinet cabinet);

    List<Cabinet> findPageResult(Cabinet cabinet);

    int findPageCountByStats(Cabinet cabinet);

    int findCountByAgent(@Param("agentId") Integer agentId);

    List<Cabinet> findPageResultByStats(Cabinet cabinet);

    List<Cabinet> findCabinetList(@Param("cabinetCompanyId") Long cabinetCompanyId);

    int delete(String id);

    Cabinet findUnique(String id);

    String hasRecordByProperty(@Param("property") String property, @Param("value") Object value);

    Cabinet findByTerminalId(@Param("terminalId") String terminalId);

    int insert(Cabinet entity);

    int update(Cabinet entity);

    int updateRatio(@Param("id") String id, @Param("platformRatio") Integer platformRatio, @Param("agentRatio") Integer agentRatio, @Param("provinceAgentRatio") Integer provinceAgentRatio, @Param("cityAgentRatio") Integer cityAgentRatio, @Param("shopRatio") Integer shopRatio, @Param("shopFixedMoney") Integer shopFixedMoney);

    int cleanDispatcherId(@Param("id") String id);

    int findCabinetCount();

    int updateDispatcher(@Param("dispatcherId") Long dispatcherId, @Param("provinceId") Integer provinceId, @Param("cityId") Integer cityId, @Param("districtId") Integer districtId);

    int updateDispatcherById(@Param("id") String id, @Param("dispatcherId") Long dispatcherId);

    int updateTerminalId(@Param("id") String id, @Param("terminalId") String terminalId);

    int clearTerminalId(@Param("fromTerminalId") String fromTerminalId, @Param("toTerminalId") String toTerminalId);

    int updateLocation(@Param("cabinetId") String cabinetId, @Param("provinceId") Integer provinceId, @Param("cityId") Integer cityId, @Param("districtId") Integer districtId, @Param("street") String street, @Param("lng") Double lng, @Param("lat") Double lat, @Param("geoHash") String geoHash, @Param("address") String address, @Param("keyword") String keyword, @Param("cabinetName") String cabinetName);

    int updateFaultLog(Long id);

    int updateFaultLog(@Param("property") String property, @Param("value") Object value);

    int updateAgentId(@Param("agentId") Integer agentId, @Param("cabinetId") String cabinetId);

    int updateMac(@Param("id") String id, @Param("mac") String mac);

    int updateUpline(@Param("id") String id, @Param("foregiftMoney") Integer foregiftMoney, @Param("rentMoney") Integer rentMoney, @Param("rentPeriodType") Integer rentPeriodType, @Param("rentExpireTime") Date rentExpireTime,
                     @Param("activePlatformDeduct") Integer activePlatformDeduct, @Param("platformDeductMoney") Integer platformDeductMoney, @Param("platformDeductExpireTime") Date platformDeductExpireTime);

    int updateUplineStatus(@Param("agentId") Integer agentId, @Param("id") String id, @Param("uplineStatus") int uplineStatus);

    int bindShop(@Param("id") String id, @Param("shopId") String shopId);

    int bindEstate(@Param("id") String id, @Param("estateId") String estateId);

    int bindIsAllowOpenBox(@Param("id") String id, @Param("isAllowOpenBox") Integer isAllowOpenBox);

    int unbindShop(@Param("id") String id);

    int unbindEstate(@Param("id") String id);

    int updatePrice(@Param("id") String id, @Param("minPrice") Integer minPrice, @Param("maxPrice") Integer maxPrice);

    int findPageMentCabinetCount(Cabinet cabinet);

    List<Cabinet> findPageMentCabinetResult(Cabinet cabinet);

    int findPageMentCabinetCountNum(Cabinet cabinet);

    List<Cabinet> findPageMentCabinetResultNum(Cabinet cabinet);
}
