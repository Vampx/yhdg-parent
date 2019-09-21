package cn.com.yusong.yhdg.webserver.service.zd;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.hdg.VipPrice;
import cn.com.yusong.yhdg.common.domain.hdg.VipPriceCabinet;
import cn.com.yusong.yhdg.common.domain.hdg.VipPriceCustomer;
import cn.com.yusong.yhdg.common.domain.hdg.VipPriceShop;
import cn.com.yusong.yhdg.common.domain.zd.VipRentPrice;
import cn.com.yusong.yhdg.common.domain.zd.VipRentPriceCustomer;
import cn.com.yusong.yhdg.common.domain.zd.VipRentPriceShop;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.hdg.*;
import cn.com.yusong.yhdg.webserver.persistence.zd.*;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class VipRentPriceService extends AbstractService {

    @Autowired
    VipRentPriceMapper vipRentPriceMapper;
    @Autowired
    VipRentBatteryForegiftMapper vipRentBatteryForegiftMapper;
    @Autowired
    VipRentPacketPeriodPriceMapper vipRentPacketPeriodPriceMapper;
    @Autowired
    VipRentPriceCustomerMapper vipRentPriceCustomerMapper;
    @Autowired
    VipRentPriceShopMapper vipRentPriceShopMapper;
    public VipRentPrice find(long id) {
        return vipRentPriceMapper.find(id);
    }

    public Page findPage(VipRentPrice search) {
        Page page = search.buildPage();
        page.setTotalItems(vipRentPriceMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<VipRentPrice> list = vipRentPriceMapper.findPageResult(search);
        for (VipRentPrice vipPrice: list) {
            vipPrice.setAgentName(findAgentInfo(vipPrice.getAgentId()).getAgentName());
        }
        page.setResult(list);
        return page;
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult create(VipRentPrice vipPrice) {
        vipPrice.setCreateTime(new Date());
        vipPrice.setCustomerCount(ConstEnum.Flag.FALSE.getValue());
        vipRentPriceMapper.insert(vipPrice);
        return DataResult.successResult("操作成功",vipPrice.getId());
    }

    public ExtResult update(VipRentPrice vipPrice) {

        vipRentPriceMapper.update(vipPrice);
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(long id) {
        List<VipRentPriceCustomer> customerList = vipRentPriceCustomerMapper.findListByPriceId(id);
        if(customerList.size() > 0) {
            return ExtResult.failResult("套餐包含骑手，不能删除。");
        }
        List<VipRentPriceShop> shopList=vipRentPriceShopMapper.findListByPriceId(id);
        if(shopList.size() > 0) {
            return ExtResult.failResult("套餐包含门店，不能删除。");
        }
      /*  Date now = new Date();
        if((vipPrice.getBeginTime().getTime() < now.getTime()) && (now.getTime() < vipPrice.getEndTime().getTime())) {
            return ExtResult.failResult("使用中套餐不能删除");
        }*/
        vipRentBatteryForegiftMapper.deleteByPriceId(id);
        vipRentPriceMapper.delete(id);
        return ExtResult.successResult();
    }
}
