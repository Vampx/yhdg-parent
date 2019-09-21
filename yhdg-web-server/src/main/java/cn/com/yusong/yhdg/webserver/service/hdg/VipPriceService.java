package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.hdg.*;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
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
    @Autowired
    VipPriceStationMapper vipPriceStationMapper;

    public VipPrice find(long id) {
        VipPrice vipPrice = vipPriceMapper.find(id);
        SystemBatteryType batteryType = findBatteryType(vipPrice.getBatteryType());
        if (batteryType != null) {
            vipPrice.setBatteryTypeName(batteryType.getTypeName());
        }
        return vipPrice;
    }

    public int updateCustomerCount(long id, Integer customerCount) {
        return vipPriceMapper.updateCustomerCount(id, customerCount);
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
    public ExtResult create(VipPrice vipPrice,
                            Long [] vipExchangeIdList,
                            Long [] vipPacketPriceIdList,
                            String [] customerMobileList,
                            String [] cabinetIdList,
                            String [] stationIdList) {
        if (vipPrice.getId() == null || vipPrice.getId() == 0) {

            vipPrice.setCabinetCount(cabinetIdList == null ? 0 : cabinetIdList.length);
            vipPrice.setCustomerCount(customerMobileList == null ? 0 : customerMobileList.length);
            vipPrice.setStationCount(stationIdList == null ? 0 : stationIdList.length);
            vipPrice.setCreateTime(new Date());
            vipPriceMapper.insert(vipPrice);

            if (vipExchangeIdList != null) {
                for (Long vipExchangeId : vipExchangeIdList) {
                    VipExchangeBatteryForegift vipExchangeBatteryForegift = vipExchangeBatteryForegiftMapper.find(vipExchangeId);
                    // 更新选中的押金减免金额
                    if (vipExchangeBatteryForegift.getForegiftId().equals(vipPrice.getForegiftId())) {
                        vipExchangeBatteryForegiftMapper.updatePriceId(vipExchangeId, vipPrice.getId());
                    } else {
                        vipExchangeBatteryForegiftMapper.delete(vipExchangeId);
                    }
                }
            }

            if (vipPacketPriceIdList != null) {
                for (Long vipPacketPriceId : vipPacketPriceIdList) {
                    VipPacketPeriodPrice vipPacketPeriodPrice = vipPacketPeriodPriceMapper.find(vipPacketPriceId);
                    if (vipPacketPeriodPrice != null) {
                        vipPacketPeriodPriceMapper.updatePriceId(vipPacketPriceId, vipPrice.getId());
                    }
                }
            }

            if (customerMobileList != null) {
                for (String customerMobile : customerMobileList) {
                    VipPriceCustomer cct = new VipPriceCustomer();
                    cct.setMobile(customerMobile);
                    cct.setPriceId(vipPrice.getId());
                    cct.setCreateTime(new Date());
                    vipPriceCustomerMapper.insert(cct);
                }
            }

            if (cabinetIdList != null) {
                List<VipPriceCabinet> vipPriceCabinetList = vipPriceCabinetMapper.findListByPriceId(0l);
                if (vipPriceCabinetList.size() > 0) {
                    vipPriceCabinetMapper.deleteByPriceId(0l);
                }
                for (String cabinetId : cabinetIdList) {
                    VipPriceCabinet cct = new VipPriceCabinet();
                    cct.setPriceId(vipPrice.getId());
                    cct.setCabinetId(cabinetId);
                    vipPriceCabinetMapper.insert(cct);
                }
            } else {
                vipPriceCabinetMapper.deleteByPriceId(0l);
            }

            if (stationIdList != null) {
                List<VipPriceStation> vipPriceStationList = vipPriceStationMapper.findListByPriceId(0l);
                if (vipPriceStationList.size() > 0) {
                    vipPriceStationMapper.deleteByPriceId(0l);
                }
                for (String stationId : stationIdList) {
                    VipPriceStation cct = new VipPriceStation();
                    cct.setPriceId(vipPrice.getId());
                    cct.setStationId(stationId);
                    vipPriceStationMapper.insert(cct);
                }
            } else {
                vipPriceStationMapper.deleteByPriceId(0l);
            }

        } else {
            vipPriceMapper.update(vipPrice);
        }
        return DataResult.successResult(vipPrice);
    }

    public ExtResult update(VipPrice vipPrice,
                            String [] customerMobileList,
                            String [] cabinetIdList,
                            String [] stationIdList) {

        vipPrice.setCabinetCount(cabinetIdList == null ? 0 : cabinetIdList.length);
        vipPrice.setCustomerCount(customerMobileList == null ? 0 : customerMobileList.length);
        vipPrice.setStationCount(stationIdList == null ? 0 : stationIdList.length);
        vipPrice.setCreateTime(new Date());
        vipPriceMapper.update(vipPrice);

        if (customerMobileList != null) {

            vipPriceCustomerMapper.deleteByPriceId(vipPrice.getId());

            for (String customerMobile : customerMobileList) {
                VipPriceCustomer cct = new VipPriceCustomer();
                cct.setMobile(customerMobile);
                cct.setPriceId(vipPrice.getId());
                cct.setCreateTime(new Date());
                vipPriceCustomerMapper.insert(cct);
            }
        }


        if (cabinetIdList != null) {
            List<VipPriceCabinet> vipPriceCabinetList = vipPriceCabinetMapper.findListByPriceId(0l);
            if (vipPriceCabinetList.size() > 0) {
                vipPriceCabinetMapper.deleteByPriceId(0l);
            }
            vipPriceCabinetMapper.deleteByPriceId(vipPrice.getId());

            for (String cabinetId : cabinetIdList) {
                VipPriceCabinet vipPriceCabinet = vipPriceCabinetMapper.findByPriceId(vipPrice.getId(), cabinetId);
                if (vipPriceCabinet == null) {
                    VipPriceCabinet cct = new VipPriceCabinet();
                    cct.setPriceId(vipPrice.getId());
                    cct.setCabinetId(cabinetId);
                    vipPriceCabinetMapper.insert(cct);
                }
            }
        } else {
            vipPriceCabinetMapper.deleteByPriceId(0l);
        }

        if (stationIdList != null) {
            List<VipPriceStation> vipPriceStationList = vipPriceStationMapper.findListByPriceId(0l);
            if (vipPriceStationList.size() > 0) {
                vipPriceStationMapper.deleteByPriceId(0l);
            }
            vipPriceStationMapper.deleteByPriceId(vipPrice.getId());

            for (String stationId : stationIdList) {
                VipPriceStation vipPriceStation = vipPriceStationMapper.findByPriceId(vipPrice.getId(), stationId);
                if (vipPriceStation == null) {
                    VipPriceStation cct = new VipPriceStation();
                    cct.setPriceId(vipPrice.getId());
                    cct.setStationId(stationId);
                    vipPriceStationMapper.insert(cct);
                }
            }
        } else {
            vipPriceStationMapper.deleteByPriceId(0l);
        }

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
        List<VipPriceStation> vipPriceStationList = vipPriceStationMapper.findListByPriceId(id);
        if(vipPriceStationList.size() > 0) {
            return ExtResult.failResult("套餐包含站点，不能删除。");
        }
        vipPacketPeriodPriceMapper.deleteByPriceId(id);
        vipExchangeBatteryForegiftMapper.deleteByPriceId(id);
        vipPriceMapper.delete(id);
        return ExtResult.successResult();
    }
}
