package cn.com.yusong.yhdg.webserver.persistence.zc;

import cn.com.yusong.yhdg.common.domain.zc.VehicleForegiftOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface VehicleForegiftOrderMapper extends MasterMapper {
    VehicleForegiftOrder find(@Param("id") String id);
    int findPageCount(VehicleForegiftOrder vehicleForegiftOrder);
    List<VehicleForegiftOrder> findPageResult(VehicleForegiftOrder vehicleForegiftOrder);
    int updateStatus(@Param("id") String id, @Param("status") int status,
                     @Param("refundMoney") Integer refundMoney,
                     @Param("refundOperator") String refundOperator,
                     @Param("refundTime") Date refundTime,
                     @Param("memo") String memo,
                     @Param("handleTime") Date handleTime);
}
