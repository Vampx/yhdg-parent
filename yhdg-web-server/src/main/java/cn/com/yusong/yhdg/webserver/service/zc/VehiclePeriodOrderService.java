package cn.com.yusong.yhdg.webserver.service.zc;

import cn.com.yusong.yhdg.common.domain.zc.VehicleModel;
import cn.com.yusong.yhdg.common.domain.zc.VehicleOrder;
import cn.com.yusong.yhdg.common.domain.zc.VehiclePeriodOrder;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.zc.VehicleModelMapper;
import cn.com.yusong.yhdg.webserver.persistence.zc.VehiclePeriodOrderMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehiclePeriodOrderService extends AbstractService {

    @Autowired
    VehiclePeriodOrderMapper rentPeriodOrderMapper;
    @Autowired
    VehicleModelMapper vehicleModelMapper;

    public VehiclePeriodOrder find(String id) {
        VehiclePeriodOrder vehicleOrder =rentPeriodOrderMapper.find(id);
        if (vehicleOrder.getModelId() != null) {
            VehicleModel vehicleModel = vehicleModelMapper.find(vehicleOrder.getModelId());
            if (vehicleModel != null) {
                vehicleOrder.setModelName(vehicleModel.getModelName());
            }
        }
        return vehicleOrder;
    }

    public Page findPage(VehiclePeriodOrder search) {
        Page page = search.buildPage();
        page.setTotalItems(rentPeriodOrderMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<VehiclePeriodOrder> list = rentPeriodOrderMapper.findPageResult(search);
        page.setResult(list);
        return page;
    }
}
