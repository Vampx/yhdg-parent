package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetReport;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CabinetReportMapper extends HistoryMapper {
    CabinetReport find(CabinetReport cabinetReport);
    int findPageCount(CabinetReport cabinetReport);
    List<CabinetReport> findPageResult(CabinetReport cabinetReport);

}
