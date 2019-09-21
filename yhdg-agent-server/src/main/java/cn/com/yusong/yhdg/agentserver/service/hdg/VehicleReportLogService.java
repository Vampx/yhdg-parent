package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.VehicleReportLog;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.VehicleReportLogMapper;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class VehicleReportLogService {
    @Autowired
    VehicleReportLogMapper vehicleReportLogMapper;

    public VehicleReportLog find(String vehicleId, Date reportTime) {
        String suffix = DateFormatUtils.format(reportTime, "yyyyww");
        VehicleReportLog vehicleReportLog = new VehicleReportLog();
        vehicleReportLog.setVehicleId(vehicleId);
        vehicleReportLog.setSuffix(suffix);
        vehicleReportLog.setReportTime(reportTime);
        return vehicleReportLogMapper.find(vehicleReportLog);
    }

    public Page findPage(VehicleReportLog search) {
        Page page = search.buildPage();
        if (search.getReportTime() == null) {
            search.setReportTime(new Date());
        }
        String suffix = DateFormatUtils.format(search.getReportTime(), "yyyyww");
        search.setSuffix(suffix);
        page.setTotalItems(vehicleReportLogMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<VehicleReportLog> vehicleReportLogList = vehicleReportLogMapper.findPageResult(search);
        page.setResult(vehicleReportLogList);

        return page;
    }
}
