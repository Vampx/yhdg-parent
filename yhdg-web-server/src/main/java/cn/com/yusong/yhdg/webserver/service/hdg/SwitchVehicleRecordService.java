package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.SwitchVehicleRecord;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.hdg.SwitchVehicleRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SwitchVehicleRecordService {
    @Autowired
    SwitchVehicleRecordMapper switchVehicleRecordMapper;

    public Page findPage(SwitchVehicleRecord search) {
        Page page = search.buildPage();
        page.setTotalItems(switchVehicleRecordMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<SwitchVehicleRecord> list = switchVehicleRecordMapper.findPageResult(search);
        page.setResult(list);
        return page;
    }

}
