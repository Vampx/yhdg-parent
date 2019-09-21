package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Device;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.service.AbstractService;
import cn.com.yusong.yhdg.webserver.persistence.basic.DeviceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceService extends AbstractService {
    @Autowired
    DeviceMapper deviceMapper;

    public Device find(int id) {
        return deviceMapper.find(id);
    }

    public Page findPage(Device search) {
        Page page = search.buildPage();
        page.setTotalItems(deviceMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(deviceMapper.findPageResult(search));
        return page;
    }

}
