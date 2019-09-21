package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.VipPriceCabinet;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.persistence.hdg.*;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VipPriceCabinetService extends AbstractService {

    @Autowired
    VipPriceCabinetMapper vipPriceCabinetMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    VipPriceMapper vipPriceMapper;

    public List<VipPriceCabinet> findListByPriceId(Long priceId) {
        List<VipPriceCabinet> list = vipPriceCabinetMapper.findListByPriceId(priceId);
        for (VipPriceCabinet vipPriceCabinet : list) {
            Cabinet cabinet = cabinetMapper.find(vipPriceCabinet.getCabinetId());
            vipPriceCabinet.setCabinetName(cabinet.getCabinetName());
        }
        return list;
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult create(VipPriceCabinet entity) {
        String[] cabinetIdArr = entity.getIds().split(",");
        for (String cabinetId : cabinetIdArr) {
            VipPriceCabinet cct = new VipPriceCabinet();
            cct.setPriceId(entity.getPriceId());
            cct.setCabinetId(cabinetId);
            vipPriceCabinetMapper.insert(cct);
        }
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(Long id, Long priceId) {
        vipPriceCabinetMapper.delete(id);
        List<VipPriceCabinet> cabinetList = vipPriceCabinetMapper.findListByPriceId(priceId);
        vipPriceMapper.updateCabinetCount(priceId, cabinetList.size());
        return ExtResult.successResult();
    }
}
