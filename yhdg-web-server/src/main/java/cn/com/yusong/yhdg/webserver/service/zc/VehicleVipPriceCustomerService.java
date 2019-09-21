package cn.com.yusong.yhdg.webserver.service.zc;

import cn.com.yusong.yhdg.common.domain.hdg.VipPriceCustomer;
import cn.com.yusong.yhdg.common.domain.zc.VehicleVipPriceCustomer;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.VipPriceCustomerMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.VipPriceMapper;
import cn.com.yusong.yhdg.webserver.persistence.zc.VehicleVipPriceCustomerMapper;
import cn.com.yusong.yhdg.webserver.persistence.zc.VehicleVipPriceMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class VehicleVipPriceCustomerService extends AbstractService {

    @Autowired
    VehicleVipPriceMapper vehicleVipPriceMapper;
    @Autowired
    VehicleVipPriceCustomerMapper vehicleVipPriceCustomerMapper;
    @Autowired
    CustomerMapper customerMapper;

    public List<VehicleVipPriceCustomer> findListByPriceId(Long priceId) {
        return vehicleVipPriceCustomerMapper.findListByPriceId(priceId);
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult create(VehicleVipPriceCustomer entity) {
        VehicleVipPriceCustomer vehicleVipPriceCustomer = vehicleVipPriceCustomerMapper.findByAgentIdAndMobile(entity.getAgentId(), entity.getMobile());
        if (vehicleVipPriceCustomer != null) {
            return ExtResult.failResult("该骑手手机号已存在");
        }
        VehicleVipPriceCustomer newVehicleVipPriceCustomer = new VehicleVipPriceCustomer();
        newVehicleVipPriceCustomer.setMobile(entity.getMobile());
        newVehicleVipPriceCustomer.setPriceId(entity.getPriceId());
        newVehicleVipPriceCustomer.setCreateTime(new Date());
        vehicleVipPriceCustomerMapper.insert(newVehicleVipPriceCustomer);
        List<VehicleVipPriceCustomer> customerList = vehicleVipPriceCustomerMapper.findListByPriceId(entity.getPriceId());
        vehicleVipPriceMapper.updateCustomerCount(entity.getPriceId().longValue(), customerList.size());
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(Long id, Long priceId) {
        vehicleVipPriceCustomerMapper.delete(id);
        List<VehicleVipPriceCustomer> customerList = vehicleVipPriceCustomerMapper.findListByPriceId(priceId);
        vehicleVipPriceMapper.updateCustomerCount(priceId, customerList.size());
        return ExtResult.successResult();
    }
}
