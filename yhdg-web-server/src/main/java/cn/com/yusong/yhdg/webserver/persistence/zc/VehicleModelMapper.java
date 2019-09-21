package cn.com.yusong.yhdg.webserver.persistence.zc;


import cn.com.yusong.yhdg.common.domain.zc.VehicleModel;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VehicleModelMapper extends MasterMapper {
    VehicleModel find(@Param("id") Integer id);
    int findPageCount(VehicleModel vehicle);
    List<VehicleModel> findPageResult(VehicleModel vehicle);
    List<VehicleModel> findList(VehicleModel vehicleModel);
    List<VehicleModel> findByAgent(Integer agentId);
    List<VehicleModel> findAll();
    int insert(VehicleModel vehicle);
    int update(VehicleModel vehicle);
    int delete(int id);
}
