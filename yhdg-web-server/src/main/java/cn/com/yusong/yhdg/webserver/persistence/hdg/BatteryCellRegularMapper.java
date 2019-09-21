package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryCellRegular;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface BatteryCellRegularMapper extends MasterMapper {
    BatteryCellRegular find(long id);
    List<BatteryCellRegular> findByRegularAndType(@Param("regular") String regular, @Param("regularType") int regularType);
    BatteryCellRegular findByCellFormatIdAndType(@Param("cellFormatId") long cellFormatId, @Param("regularType") int regularType);
    BatteryCellRegular findByBatteryFormatIdAndType(@Param("batteryFormatId") long batteryFormatId, @Param("regularType") int regularType);
    int insert(BatteryCellRegular batteryCellRegular);
    int update(BatteryCellRegular batteryCellRegular);
    int updateNumByCellFormatId(@Param("cellFormatId") long cellFormatId, @Param("num") int num);
    int updateNumByBatteryFormatId(@Param("batteryFormatId") long batteryFormatId, @Param("num") int num);
    int delete(long id);
}
