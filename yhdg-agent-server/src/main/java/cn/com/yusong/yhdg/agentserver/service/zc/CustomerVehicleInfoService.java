package cn.com.yusong.yhdg.agentserver.service.zc;

import cn.com.yusong.yhdg.agentserver.persistence.hdg.BatteryMapper;
import cn.com.yusong.yhdg.agentserver.persistence.zc.CustomerVehicleInfoMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.zc.CustomerVehicleInfo;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerVehicleInfoService extends AbstractService {

    @Autowired
    CustomerVehicleInfoMapper customerVehicleInfoMapper;
    @Autowired
    BatteryMapper batteryMapper;

    public CustomerVehicleInfo find(Long id) {
        return customerVehicleInfoMapper.find(id);
    }

    public Page findPage(CustomerVehicleInfo search) {
        Page page = search.buildPage();
        page.setTotalItems(customerVehicleInfoMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<CustomerVehicleInfo> list = customerVehicleInfoMapper.findPageResult(search);
        for (CustomerVehicleInfo customerVehicleInfo : list) {
            if(customerVehicleInfo.getBatteryType() != null) {
                customerVehicleInfo.setBatteryTypeName(findBatteryType(customerVehicleInfo.getBatteryType()).getTypeName());
            }
            String batteryId = "";
            List<Battery> batteryList = batteryMapper.findByCustomer(customerVehicleInfo.getId());
            for (Battery battery : batteryList){
                batteryId+= battery.getId() + " ";
            }
            customerVehicleInfo.setBatteryId(batteryId);
        }
        page.setResult(list);
        return page;
    }

}
