package cn.com.yusong.yhdg.agentserver.service.zc;

import cn.com.yusong.yhdg.agentserver.persistence.zc.VehicleForegiftOrderMapper;
import cn.com.yusong.yhdg.agentserver.persistence.zc.VehicleModelMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.zc.VehicleForegiftOrder;
import cn.com.yusong.yhdg.common.domain.zc.VehicleModel;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleForegiftOrderService extends AbstractService {

    @Autowired
    VehicleForegiftOrderMapper vehicleForegiftOrderMapper;
    @Autowired
    VehicleModelMapper vehicleModelMapper;

    public VehicleForegiftOrder find(String id) {
        VehicleForegiftOrder vehicleForegiftOrder = vehicleForegiftOrderMapper.find(id);
        if (vehicleForegiftOrder.getModelId() != null) {
            VehicleModel vehicleModel = vehicleModelMapper.find(vehicleForegiftOrder.getModelId());
            if (vehicleModel != null) {
                vehicleForegiftOrder.setModelName(vehicleModel.getModelName());
            }
        }
        return vehicleForegiftOrder;
    }

    public Page findPage(VehicleForegiftOrder search) {
        Page page = search.buildPage();
        page.setTotalItems(vehicleForegiftOrderMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<VehicleForegiftOrder> list = vehicleForegiftOrderMapper.findPageResult(search);
        page.setResult(list);
        return page;
    }
}
