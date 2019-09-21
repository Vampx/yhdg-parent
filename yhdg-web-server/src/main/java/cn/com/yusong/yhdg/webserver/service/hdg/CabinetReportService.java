package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetReport;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.hdg.CabinetReportMapper;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CabinetReportService {
    @Autowired
    CabinetReportMapper cabinetReportMapper;

    public CabinetReport find(long id, Date createTime) {
        String suffix = DateFormatUtils.format(createTime, Constant.DATE_FORMAT_NO_LINE);
        CabinetReport cabinetReport = new CabinetReport();
        cabinetReport.setId(id);
        cabinetReport.setSuffix(suffix);
        cabinetReport.setCreateTime(createTime);
        return cabinetReportMapper.find(cabinetReport);
    }

    public Page findPage(CabinetReport search) {

        Page page = search.buildPage();
        if (search.getCreateTime() == null) {
            search.setCreateTime(new Date());
        }
        String suffix = DateFormatUtils.format(search.getCreateTime(), Constant.DATE_FORMAT_NO_LINE);
        search.setSuffix(suffix);
        page.setTotalItems(cabinetReportMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<CabinetReport> cabinetReports = cabinetReportMapper.findPageResult(search);
        page.setResult(cabinetReports);

        return page;
    }

}
