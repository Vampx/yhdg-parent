//package cn.com.yusong.yhdg.agentserver.persistence.hdg;
//
//import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
//import org.apache.ibatis.annotations.Param;
//
//import java.util.List;
//
//public interface VehicleOrderHistoryMapper extends HistoryMapper {
//    List<String> findTable(@Param("tableName") String tableName);
//
//    int findPageCount(VehicleOrder vehicleOrder);
//
//    List<VehicleOrder> findPageResult(VehicleOrder vehicleOrder);
//
//    VehicleOrder find(@Param("id") String id, @Param("suffix") String suffix);
//
//}