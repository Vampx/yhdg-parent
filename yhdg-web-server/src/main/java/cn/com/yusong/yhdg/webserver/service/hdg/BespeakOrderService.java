package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BespeakOrder;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetBox;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.hdg.BespeakOrderMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.BatteryMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.CabinetBoxMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class BespeakOrderService extends AbstractService {
    @Autowired
    BespeakOrderMapper bespeakOrderMapper;
    @Autowired
    CabinetBoxMapper cabinetBoxMapper;
    @Autowired
    BatteryMapper batteryMapper;

    public Page findPage(BespeakOrder search) {
        Page page = search.buildPage();
        page.setTotalItems(bespeakOrderMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<BespeakOrder> batteryList = bespeakOrderMapper.findPageResult(search);
        page.setResult(batteryList);
        return page;
    }

    public BespeakOrder find(String id) {
        return bespeakOrderMapper.find(id);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult complete(String id) {
        BespeakOrder order = bespeakOrderMapper.find(id);
        if (order == null) {
            return ExtResult.failResult("订单不存在");
        }
        if (order.getStatus() == BespeakOrder.Status.TAKE.getValue() || order.getStatus() == BespeakOrder.Status.OTHER_TAKE.getValue()
                || order.getStatus() == BespeakOrder.Status.CANCEL.getValue() || order.getStatus() == BespeakOrder.Status.EXPIRE.getValue()
                || order.getStatus() == BespeakOrder.Status.MANUAL_COMPLETE.getValue()) {
            return ExtResult.failResult("订单已结束");
        }
        int effect = bespeakOrderMapper.updateStatus(order.getId(), BespeakOrder.Status.MANUAL_COMPLETE.getValue(), new Date());
        if (effect == 1) {
            cabinetBoxMapper.unlockBox(order.getBespeakCabinetId(),order.getBespeakBoxNum(), CabinetBox.BoxStatus.BESPEAK.getValue(), CabinetBox.BoxStatus.FULL.getValue());
        }
        return ExtResult.successResult();
    }

}
