package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetReportBattery;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.hdg.CabinetReportBatteryMapper;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CabinetReportBatteryService {
    @Autowired
    CabinetReportBatteryMapper cabinetReportBatteryMapper;

    public CabinetReportBattery find(long id, Date createTime) {
        String suffix = DateFormatUtils.format(createTime, Constant.DATE_FORMAT_NO_LINE);
        CabinetReportBattery cabinetReportBattery = new CabinetReportBattery();
        cabinetReportBattery.setId(id);
        cabinetReportBattery.setSuffix(suffix);
        cabinetReportBattery.setCreateTime(createTime);
        return cabinetReportBatteryMapper.find(cabinetReportBattery);
    }

    public Page findPage(CabinetReportBattery search) {

        Page page = search.buildPage();
        if (search.getCreateTime() == null) {
            search.setCreateTime(new Date());
        }
        String suffix = DateFormatUtils.format(search.getCreateTime(), Constant.DATE_FORMAT_NO_LINE);
        search.setSuffix(suffix);
        page.setTotalItems(cabinetReportBatteryMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<CabinetReportBattery> cabinetReportBatterys = cabinetReportBatteryMapper.findPageResult(search);
        page.setResult(cabinetReportBatterys);

        return page;
    }

}
