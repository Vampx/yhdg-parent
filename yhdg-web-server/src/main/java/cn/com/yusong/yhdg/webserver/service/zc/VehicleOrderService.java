package cn.com.yusong.yhdg.webserver.service.zc;

import cn.com.yusong.yhdg.common.domain.zc.GroupOrder;
import cn.com.yusong.yhdg.common.domain.zc.VehicleModel;
import cn.com.yusong.yhdg.common.domain.zc.VehicleOrder;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.zc.VehicleModelMapper;
import cn.com.yusong.yhdg.webserver.persistence.zc.VehicleOrderMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleOrderService extends AbstractService {
    @Autowired
    VehicleOrderMapper vehicleOrderMapper;
    @Autowired
    VehicleModelMapper vehicleModelMapper;

    public VehicleOrder find(String id) {
        VehicleOrder vehicleOrder = vehicleOrderMapper.find(id);
        if (vehicleOrder.getModelId() != null) {
            VehicleModel vehicleModel = vehicleModelMapper.find(vehicleOrder.getModelId());
            if (vehicleModel != null) {
                vehicleOrder.setModelName(vehicleModel.getModelName());
            }
        }
        return vehicleOrder;
    }

    public Page findPage(VehicleOrder search) {
        Page page = search.buildPage();
        page.setTotalItems(vehicleOrderMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<VehicleOrder> list = vehicleOrderMapper.findPageResult(search);
        page.setResult(list);
        return page;
    }

}
