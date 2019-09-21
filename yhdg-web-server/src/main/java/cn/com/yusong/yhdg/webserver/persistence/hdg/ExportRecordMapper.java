package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ExportRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExportRecordMapper extends MasterMapper {

	ExportRecord find(@Param("id") Integer id);

	int findPageCount(ExportRecord batteryExportDetail);

	List<ExportRecord> findPageResult(ExportRecord batteryExportDetail);

	int insert(ExportRecord batteryExportDetail);

	ExportRecord findLastByBattery(@Param("shellCode") String shellCode);

	ExportRecord findLastByCabinet(@Param("cabinetId") String cabinetId);

	int delete(@Param("id") Integer id);

}
