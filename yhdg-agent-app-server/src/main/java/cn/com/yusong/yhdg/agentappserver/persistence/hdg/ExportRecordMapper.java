package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ExportRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExportRecordMapper extends MasterMapper {

	List<ExportRecord> findByPersonId(@Param("personId") Long personId);

	List<ExportRecord> findBatteryListByPersonId(@Param("personId") Long personId, @Param("offset") int offset, @Param("limit") int limit);

	List<ExportRecord> findCabinetListByPersonId(@Param("personId") Long personId, @Param("offset") int offset, @Param("limit") int limit);

	List<ExportRecord> findList(@Param("personId") Long personId, @Param("exportType") Integer exportType, @Param("offset") int offset, @Param("limit") int limit);

	ExportRecord findLastByBattery(@Param("shellCode") String shellCode);

	ExportRecord findLastByCabinet(@Param("cabinetId") String cabinetId);

	ExportRecord find(@Param("id") Integer id);

	int insert(ExportRecord exportRecord);

	int delete(@Param("id") int id);

}
