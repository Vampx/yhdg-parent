package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.InsuranceOrder;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrderRefund;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodPrice;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.entity.pagination.PageRequest;
import cn.com.yusong.yhdg.webserver.persistence.hdg.PacketPeriodOrderRefundMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.PacketPeriodPriceMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PacketPeriodOrderRefundService extends AbstractService {

    @Autowired
    PacketPeriodOrderRefundMapper packetPeriodOrderRefundMapper;

    public Page findPage(PacketPeriodOrderRefund packetPeriodOrderRefund) {
        PageRequest pageRequest = new PageRequest(packetPeriodOrderRefund.getPage(), packetPeriodOrderRefund.getRows());
        Page page = new Page(pageRequest);
        page.setTotalItems(packetPeriodOrderRefundMapper.findPageCount(packetPeriodOrderRefund));
        packetPeriodOrderRefund.setBeginIndex(page.getOffset());
        List<PacketPeriodOrderRefund> packetPeriodOrderRefundList = packetPeriodOrderRefundMapper.findPageResult(packetPeriodOrderRefund);
        page.setResult(packetPeriodOrderRefundList);
        return page;
    }
}
