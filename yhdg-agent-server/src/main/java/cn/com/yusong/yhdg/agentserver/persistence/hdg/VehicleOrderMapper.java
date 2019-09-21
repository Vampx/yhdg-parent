//package cn.com.yusong.yhdg.agentserver.persistence.hdg;
//
//import cn.com.yusong.yhdg.common.domain.zc.VehicleOrder;
//import cn.com.yusong.yhdg.common.persistence.MasterMapper;
//import org.apache.ibatis.annotations.Param;
//
//import java.util.List;
//
//public interface VehicleOrderMapper extends MasterMapper {
//    VehicleOrder find(String id);
//
//    int findPageCount(VehicleOrder search);
//
//    List<VehicleOrder> findPageResult(VehicleOrder search);
////    List<VehicleOrder> findList(long vehicleId);
//    List<VehicleOrder> findList(String vehicleId);
//
//    int findCountByVehicleId(@Param("vehicleId") String vehicleId);
//
//    int findCountByModelId(@Param("modelId") Long modelId);
//
//    int insert(VehicleOrder vehicleOrder);
//
//}
