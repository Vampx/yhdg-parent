package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CabinetMapper extends MasterMapper {

    public Cabinet find(String id);

    public Cabinet findByTerminalId(@Param("terminalId") String terminalId);

    public List<Cabinet> findNearest(@Param("agentId") List<Integer> agentId,@Param("emptyStatus") int emptyStatus,
                                     @Param("fullStatus") int fullStatus,
                                     @Param("batteryStatus") int batteryStatus,
                                     @Param("geoHash") String geoHash,
                                     @Param("keyword") String keyword,
                                     @Param("lng") double lng,
                                     @Param("lat") double lat,
                                     @Param("provinceId") Integer provinceId,
                                     @Param("cityId") Integer cityId,
                                     @Param("upLineStatus") Integer upLineStatus,
                                     @Param("viewType") Integer viewType,
                                     @Param("unSharedCabinetId") String unSharedCabinetId,
                                     @Param("offset") int offset,
                                     @Param("limit") int limit
    );

    int findFullCount(@Param("fullStatus") int fullStatus,@Param("batteryStatus") int batteryStatus,@Param("cabinetId") String cabinetId);

    public List<Cabinet> findList(@Param("dispatcherId") long dispatcherId);

    public List<Cabinet> findAddressList();

    public int findCountByDispatcher(@Param("dispatcherId") long dispatcherId);

    public int findBatteryCountByCabinet(@Param("cabinetId") String cabinetId);

    public int findNotFullBatteryCountByCabinet(@Param("cabinetId") String cabinetId);

    public int findOnlineSubcabinetCountByCabinet(@Param("cabinetId") String cabinetId);

    public int findOfflineSubcabinetCountByCabinet(@Param("cabinetId") String cabinetId);

    public int findBatteryCountByDispatcher(@Param("dispatcherId") long dispatcherId);

    public int findNotFullBatteryCountByDispatcher(@Param("dispatcherId") long dispatcherId);

    public int updateLocation(@Param("cabinetId") String cabinetId, @Param("provinceId") Integer provinceId, @Param("cityId") Integer cityId, @Param("districtId") Integer districtId, @Param("street") String street, @Param("lng") Double lng, @Param("lat") Double lat, @Param("geoHash") String geoHash, @Param("address") String address, @Param("keyword") String keyword, @Param("cabinetName") String cabinetName);

    int updateOperationFlag(@Param("id") String id, @Param("operationFlag") Integer operationFlag);

    int updateFaultLog1(long id);

    int updateToken(@Param("id") String id, @Param("loginToken") String loginToken);

    int updatePrice(@Param("id") String id, @Param("minPrice") Integer minPrice, @Param("maxPrice") Integer maxPrice);

    int updateFaultLog2(@Param("property") String property, @Param("value") Object value);

    public List<Cabinet> findIdByCabinet(@Param("cabinetId") String cabinetId);

    public List<String> findIdByCabinetId(@Param("cabinetId") String cabinetId);

    int updateUpline(@Param("id") String id, @Param("cabinetName") String cabinetName, @Param("permitExchangeVolume") int permitExchangeVolume, @Param("address") String address, @Param("chargeFullVolume") int chargeFullVolume, @Param("price") double price, @Param("terminalId") String terminalId, @Param("upLineStatus") int upLineStatus, @Param("foregiftMoney") int foregiftMoney, @Param("rentMoney")int rentMoney,
                     @Param("rentPeriodType")int rentPeriodType, @Param("rentExpireTime")Date rentExpireTime);

    int updateUpLineStatus(Cabinet cabinet);

}
