package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryBarcode;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryFormat;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.BatteryBarcodeMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.BatteryFormatMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class BatteryFormatService extends AbstractService {

    @Autowired
    BatteryFormatMapper batteryFormatMapper;
    @Autowired
    BatteryBarcodeMapper batteryBarcodeMapper;

    public BatteryFormat find(long id) {
        return batteryFormatMapper.find(id);
    }

    public Page findPage(BatteryFormat search) {
        Page page = search.buildPage();
        page.setTotalItems(batteryFormatMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<BatteryFormat> list = batteryFormatMapper.findPageResult(search);
        page.setResult(list);
        return page;
    }


    @Transactional(rollbackFor=Throwable.class)
    public ExtResult create(BatteryFormat batteryFormat) {
        batteryFormat.setCreateTime(new Date());
        int result = batteryFormatMapper.insert(batteryFormat);
        if (result > 0) {
            return DataResult.successResult(batteryFormat.getId());
        }
        return DataResult.failResult("对不起! 保存失败", null);
    }

    public ExtResult update(BatteryFormat batteryFormat) {
        batteryFormatMapper.update(batteryFormat);
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(long id) {
        List<BatteryBarcode> list = batteryBarcodeMapper.findList(id);
        if (list.size() > 0) {
            return ExtResult.failResult("该规格已生成电芯条码，不可删除！");
        }
        batteryFormatMapper.delete(id);
        return ExtResult.successResult();
    }
}
