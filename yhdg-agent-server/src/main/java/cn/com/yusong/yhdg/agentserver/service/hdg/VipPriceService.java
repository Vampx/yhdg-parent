package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.agentserver.persistence.hdg.*;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.hdg.VipPrice;
import cn.com.yusong.yhdg.common.domain.hdg.VipPriceCabinet;
import cn.com.yusong.yhdg.common.domain.hdg.VipPriceCustomer;
import cn.com.yusong.yhdg.common.domain.hdg.VipPriceShop;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class VipPriceService extends AbstractService {

    @Autowired
    VipPriceMapper vipPriceMapper;
    @Autowired
    VipExchangeBatteryForegiftMapper vipExchangeBatteryForegiftMapper;
    @Autowired
    VipPacketPeriodPriceMapper vipPacketPeriodPriceMapper;
    @Autowired
    VipPriceCustomerMapper vipPriceCustomerMapper;
    @Autowired
    VipPriceCabinetMapper vipPriceCabinetMapper;
    @Autowired
    VipPriceShopMapper vipPriceShopMapper;
    public VipPrice find(long id) {
        return vipPriceMapper.find(id);
    }

    public Page findPage(VipPrice search) {
        Page page = search.buildPage();
        page.setTotalItems(vipPriceMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<VipPrice> list = vipPriceMapper.findPageResult(search);
        for (VipPrice vipPrice: list) {
            vipPrice.setAgentName(findAgentInfo(vipPrice.getAgentId()).getAgentName());
        }
        page.setResult(list);
        return page;
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult create(VipPrice vipPrice) {
        vipPrice.setCreateTime(new Date());
        vipPrice.setCabinetCount(ConstEnum.Flag.FALSE.getValue());
        vipPrice.setCustomerCount(ConstEnum.Flag.FALSE.getValue());
        vipPriceMapper.insert(vipPrice);
        return DataResult.successResult("操作成功",vipPrice.getId());
    }

    public ExtResult update(VipPrice vipPrice) {
        vipPriceMapper.update(vipPrice);
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(long id) {
        List<VipPriceCustomer> customerList = vipPriceCustomerMapper.findListByPriceId(id);
        if(customerList.size() > 0) {
            return ExtResult.failResult("套餐包含骑手，不能删除。");
        }
        List<VipPriceCabinet> cabinetList = vipPriceCabinetMapper.findListByPriceId(id);
        if(cabinetList.size() > 0) {
            return ExtResult.failResult("套餐包含柜子，不能删除。");
        }
        List<VipPriceShop> shopList=vipPriceShopMapper.findListByPriceId(id);
        if(shopList.size() > 0) {
            return ExtResult.failResult("套餐包含门店，不能删除。");
        }

      /*  Date now = new Date();
        if((vipPrice.getBeginTime().getTime() < now.getTime()) && (now.getTime() < vipPrice.getEndTime().getTime())) {
            return ExtResult.failResult("使用中套餐不能删除");
        }*/
        vipExchangeBatteryForegiftMapper.deleteByPriceId(id);
        vipPriceMapper.delete(id);
        return ExtResult.successResult();
    }
}
