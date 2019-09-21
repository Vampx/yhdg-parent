package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.agentserver.persistence.hdg.*;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.VipPriceCabinet;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
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
            VipPriceCabinet vipPriceCabinet = vipPriceCabinetMapper.findByPriceId(entity.getPriceId(), cabinetId);
            if (vipPriceCabinet != null) {
                return ExtResult.failResult("包含已存在的柜子");
            }
            VipPriceCabinet cct = new VipPriceCabinet();
            cct.setPriceId(entity.getPriceId());
            cct.setCabinetId(cabinetId);
            vipPriceCabinetMapper.insert(cct);
            updatePrice(cabinetMapper.find(cabinetId).getAgentId(), cabinetId);
        }
        List<VipPriceCabinet> cabinetList = vipPriceCabinetMapper.findListByPriceId(entity.getPriceId());
        vipPriceMapper.updateCabinetCount(entity.getPriceId(), cabinetList.size());
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(Long id, Long priceId) {
        List<VipPriceCabinet> vipPriceCabinetList = vipPriceCabinetMapper.findListByPriceId(priceId);
        vipPriceCabinetMapper.delete(id);
        List<VipPriceCabinet> cabinetList = vipPriceCabinetMapper.findListByPriceId(priceId);
        vipPriceMapper.updateCabinetCount(priceId, cabinetList.size());
        for (VipPriceCabinet cabinet : vipPriceCabinetList) {
            updatePrice(cabinetMapper.find(cabinet.getCabinetId()).getAgentId(), cabinet.getCabinetId());
        }
        return ExtResult.successResult();
    }
}
