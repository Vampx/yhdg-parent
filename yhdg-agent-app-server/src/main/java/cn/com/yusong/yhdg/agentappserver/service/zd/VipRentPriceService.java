package cn.com.yusong.yhdg.agentappserver.service.zd;

import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.AgentCompanyMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.*;
import cn.com.yusong.yhdg.agentappserver.persistence.zd.*;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.zd.VipRentPriceController;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeBatteryForegift;
import cn.com.yusong.yhdg.common.domain.hdg.VipPriceAgentCompany;
import cn.com.yusong.yhdg.common.domain.zd.*;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class VipRentPriceService extends AbstractService {
    @Autowired
    VipRentBatteryForegiftMapper vipRentBatteryForegiftMapper;
    @Autowired
    RentBatteryForegiftMapper rentBatteryForegiftMapper;
    @Autowired
    ShopMapper shopMapper;
    @Autowired
    AgentCompanyMapper agentCompanyMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    VipRentPriceMapper vipRentPriceMapper;
    @Autowired
    VipRentPeriodPriceMapper vipRentPeriodPriceMapper;
    @Autowired
    VipRentPriceShopMapper vipRentPriceShopMapper;
    @Autowired
    VipRentPriceAgentCompanyMapper vipRentPriceAgentCompanyMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    VipRentPriceCustomerMapper vipRentPriceCustomerMapper;
    @Autowired
    RentBatteryTypeMapper rentBatteryTypeMapper;

    public VipRentPrice find(Long id) {
        return vipRentPriceMapper.find(id);
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult list(Integer agentId, String keyword, int offset, int limit) {

        List<VipRentPrice> list = vipRentPriceMapper.findListByAgentId(agentId, keyword, offset, limit);
        List<Map> result = new ArrayList<Map>();
        for (VipRentPrice vipPrice : list) {
            NotNullMap data = new NotNullMap();

            data.putLong("id", vipPrice.getId());
            data.putString("priceName", vipPrice.getPriceName());
            data.putInteger("isActive", vipPrice.getIsActive());
            data.putInteger("customerCount", vipPrice.getCustomerCount());
            data.putInteger("shopCount", vipPrice.getShopCount());
            data.putInteger("agentCompanyCount", vipPrice.getAgentCompanyCount());

            result.add(data);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);

    }

    @Transactional(rollbackFor=Throwable.class)
    public RestResult create(VipRentPrice vipPrice,
                             Integer agentId,
                             VipRentPriceController.CreateParam.VipPacketPeriodPrice[] packetPeriodList,
                             String[] customerMobileList,
                             String[] shopIdList,
                             String[] agentCompanyIdList) {


        vipRentPriceMapper.insert(vipPrice);

        //biza
        if (packetPeriodList != null) {

            for (int i = 0; i < packetPeriodList.length; i++) {

                VipRentPriceController.CreateParam.VipPacketPeriodPrice bizaForegift = packetPeriodList[i];
                VipRentBatteryForegift vipRentBatteryForegift = new VipRentBatteryForegift();

                vipRentBatteryForegift.setAgentId(agentId);
                vipRentBatteryForegift.setPriceId(vipPrice.getId());
                vipRentBatteryForegift.setReduceMoney(bizaForegift.reduceMoney);
                vipRentBatteryForegift.setForegiftId(bizaForegift.foregiftId);
                vipRentBatteryForegift.setCreateTime(new Date());
                vipRentBatteryForegiftMapper.insert(vipRentBatteryForegift);

                for (int j = 0; j < packetPeriodList[i].priceList.length; j++) {
                    VipRentPriceController.CreateParam.VipPacketPeriodPrice.GroupBizaPrice bizaPrice = packetPeriodList[i].priceList[j];
                    VipRentPeriodPrice vipRentPeriodPrice = new VipRentPeriodPrice();

                    vipRentPeriodPrice.setPriceId(vipRentBatteryForegift.getPriceId());
                    vipRentPeriodPrice.setForegiftId(bizaForegift.foregiftId);
                    vipRentPeriodPrice.setDayCount(bizaPrice.dayCount);
                    vipRentPeriodPrice.setPrice(bizaPrice.price);
                    vipRentPeriodPrice.setMemo(bizaPrice.memo);
                    vipRentPeriodPrice.setAgentId(agentId);
                    vipRentPeriodPrice.setAgentName(agentMapper.find(agentId).getAgentName());
                    vipRentPeriodPrice.setBatteryType(vipPrice.getBatteryType());
                    vipRentPeriodPrice.setCreateTime(new Date());
                    vipRentPeriodPriceMapper.insert(vipRentPeriodPrice);

                }

            }
        }

        if (customerMobileList.length > 0) {
            for (String mobile : customerMobileList) {
                VipRentPriceCustomer vipRentPriceCustomer = vipRentPriceCustomerMapper.findByAgentIdAndMobile(agentId, mobile);
                if (vipRentPriceCustomer != null) {
                    return  RestResult.result(RespCode.CODE_2.getValue(),"该骑手手机号已存在");
                }
                VipRentPriceCustomer cct = new VipRentPriceCustomer();
                cct.setMobile(mobile);
                cct.setPriceId(vipPrice.getId());
                cct.setCreateTime(new Date());
                vipRentPriceCustomerMapper.insert(cct);
            }
            List<VipRentPriceCustomer> customerList = vipRentPriceCustomerMapper.findListByPriceId(vipPrice.getId());
            vipRentPriceMapper.updateCustomerCount(vipPrice.getId(),customerList.size());
        }

        if (shopIdList.length > 0) {
            for (String shopId : shopIdList) {
                VipRentPriceShop vipPriceShop = vipRentPriceShopMapper.findByPriceId(vipPrice.getId(), shopId);
                if (vipPriceShop != null) {
                    return  RestResult.result(RespCode.CODE_2.getValue(),"包含已存在的门店");
                }
                VipRentPriceShop cct = new VipRentPriceShop();
                cct.setPriceId(vipPrice.getId());
                cct.setShopId(shopId);
                vipRentPriceShopMapper.insert(cct);
            }
            List<VipRentPriceShop> shopList = vipRentPriceShopMapper.findListByPriceId(vipPrice.getId());
            vipRentPriceMapper.updateShopCount(vipPrice.getId(), shopList.size());
        }

        if (agentCompanyIdList.length > 0) {
            for (String agentCompanyId : agentCompanyIdList) {
                VipRentPriceAgentCompany vipRentPriceAgentCompany = vipRentPriceAgentCompanyMapper.findByPriceId(vipPrice.getId(), agentCompanyId);
                if (vipRentPriceAgentCompany != null) {
                    return  RestResult.result(RespCode.CODE_2.getValue(),"包含已存在的运营公司");
                }
                VipRentPriceAgentCompany cct = new VipRentPriceAgentCompany();
                cct.setPriceId(vipPrice.getId());
                cct.setAgentCompanyId(agentCompanyId);
                vipRentPriceAgentCompanyMapper.insert(cct);
            }
            List<VipRentPriceAgentCompany> agentCompanyList = vipRentPriceAgentCompanyMapper.findListByPriceId(vipPrice.getId());
            vipRentPriceMapper.updateAgentCompanyCount(vipPrice.getId(), agentCompanyList.size());
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);

    }


    @Transactional(rollbackFor=Throwable.class)
    public RestResult update(VipRentPrice vipRentPrice,
                             Integer agentId,
                             VipRentPriceController.UpdateParam.VipPacketPeriodPrice[] packetPeriodList,
                             String[] customerMobileList,
                             String[] shopIdList,
                             String[] agentCompanyIdList) {

        vipRentPriceMapper.update(vipRentPrice);
        //biza
        if (packetPeriodList != null) {

            vipRentBatteryForegiftMapper.deleteByPriceId(vipRentPrice.getId());
            vipRentPeriodPriceMapper.deleteByPriceId(vipRentPrice.getId());

            for (int i = 0; i < packetPeriodList.length; i++) {
                VipRentPriceController.UpdateParam.VipPacketPeriodPrice bizaForegift = packetPeriodList[i];
                VipRentBatteryForegift vipForegift = new VipRentBatteryForegift();

                vipForegift.setAgentId(agentId);
                vipForegift.setPriceId(vipRentPrice.getId());
                vipForegift.setReduceMoney(bizaForegift.reduceMoney);
                vipForegift.setForegiftId(bizaForegift.foregiftId);
                vipForegift.setCreateTime(new Date());
                vipRentBatteryForegiftMapper.insert(vipForegift);

                for (int j = 0; j < packetPeriodList[i].priceList.length; j++) {
                    VipRentPriceController.UpdateParam.VipPacketPeriodPrice.GroupBizaPrice bizaPrice = packetPeriodList[i].priceList[j];
                    VipRentPeriodPrice vipRentPeriodPrice = new VipRentPeriodPrice();

                    vipRentPeriodPrice.setPriceId(vipForegift.getPriceId());
                    vipRentPeriodPrice.setForegiftId(bizaForegift.foregiftId);
                    vipRentPeriodPrice.setDayCount(bizaPrice.dayCount);
                    vipRentPeriodPrice.setPrice(bizaPrice.price);
                    vipRentPeriodPrice.setMemo(bizaPrice.memo);
                    vipRentPeriodPrice.setAgentId(agentId);
                    vipRentPeriodPrice.setAgentName(agentMapper.find(agentId).getAgentName());
                    vipRentPeriodPrice.setBatteryType(vipRentPrice.getBatteryType());
                    vipRentPeriodPrice.setCreateTime(new Date());
                    vipRentPeriodPriceMapper.insert(vipRentPeriodPrice);

                }

            }
        }

        if (customerMobileList.length > 0) {


            Set<String> mobileSet = new HashSet<String>();
            for (String mobile : customerMobileList) {
                mobileSet.add(mobile);
            }

            vipRentPriceCustomerMapper.deleteByPriceId(vipRentPrice.getId());

            for (String mobile : mobileSet) {

                VipRentPriceCustomer cct = new VipRentPriceCustomer();
                cct.setMobile(mobile);
                cct.setPriceId(vipRentPrice.getId());
                cct.setCreateTime(new Date());
                vipRentPriceCustomerMapper.insert(cct);
            }
        }

        List<VipRentPriceCustomer> customerList = vipRentPriceCustomerMapper.findListByPriceId(vipRentPrice.getId());
        vipRentPriceMapper.updateCustomerCount(vipRentPrice.getId(),customerList.size());

        if (shopIdList.length > 0) {

            Set<String> shopSet = new HashSet<String>();
            for (String shopId : shopIdList) {
                shopSet.add(shopId);
            }

            vipRentPriceShopMapper.deleteByPriceId(vipRentPrice.getId());

            for (String shopId : shopSet) {

                VipRentPriceShop cct = new VipRentPriceShop();
                cct.setPriceId(vipRentPrice.getId());
                cct.setShopId(shopId);
                vipRentPriceShopMapper.insert(cct);
            }
        }

        List<VipRentPriceShop> shopList = vipRentPriceShopMapper.findListByPriceId(vipRentPrice.getId());
        vipRentPriceMapper.updateShopCount(vipRentPrice.getId(), shopList.size());

        if (agentCompanyIdList.length > 0) {

            Set<String> agentCompanySet = new HashSet<String>();
            for (String agentCompanyId : agentCompanyIdList) {
                agentCompanySet.add(agentCompanyId);
            }

            vipRentPriceAgentCompanyMapper.deleteByPriceId(vipRentPrice.getId());

            for (String agentCompanyId : agentCompanySet) {

                VipRentPriceAgentCompany cct = new VipRentPriceAgentCompany();
                cct.setPriceId(vipRentPrice.getId());
                cct.setAgentCompanyId(agentCompanyId);
                vipRentPriceAgentCompanyMapper.insert(cct);
            }
        }

        List<VipRentPriceAgentCompany> agentCompanyList = vipRentPriceAgentCompanyMapper.findListByPriceId(vipRentPrice.getId());
        vipRentPriceMapper.updateAgentCompanyCount(vipRentPrice.getId(), agentCompanyList.size());

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }



    @Transactional(rollbackFor = Throwable.class)
    public RestResult detail(Long id, Integer agentId) {
        NotNullMap data = new NotNullMap();

        VipRentPrice vipRentPrice = vipRentPriceMapper.find(id);
        if (vipRentPrice == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "记录不存在!");
        }
        RentBatteryType rentBatteryType = rentBatteryTypeMapper.find(vipRentPrice.getBatteryType(), agentId);
        data.putLong("id", vipRentPrice.getId());
        data.putInteger("batteryType", vipRentPrice.getBatteryType());
        data.putString("batteryTypeName", rentBatteryType.getTypeName());
        data.putString("priceName", vipRentPrice.getPriceName());
        data.putDateTime("beginTime", vipRentPrice.getBeginTime());
        data.putDateTime("endTime", vipRentPrice.getEndTime());
        data.putInteger("isActive", vipRentPrice.getIsActive());

        //biza
        List<VipRentBatteryForegift> vipRentBatteryForegiftList = vipRentBatteryForegiftMapper.findListByPriceId(vipRentPrice.getId());
        List<NotNullMap> packetPeriodList = new ArrayList<NotNullMap>();

        for (VipRentBatteryForegift bizaForegift : vipRentBatteryForegiftList) {
            NotNullMap notNullMap = new NotNullMap();
            notNullMap.put("reduceMoney", bizaForegift.getReduceMoney());
            notNullMap.put("foregiftId", bizaForegift.getForegiftId());
            RentBatteryForegift foregift = rentBatteryForegiftMapper.find(bizaForegift.getForegiftId());
            if (foregift != null) {
                notNullMap.put("foregift", foregift.getMoney());
            } else {
                notNullMap.put("foregift", 0);
            }

            List<VipRentPeriodPrice> vipPeriodPriceList = vipRentPeriodPriceMapper.findListByForegiftId(
                    bizaForegift.getForegiftId(), vipRentPrice.getBatteryType(), agentId, bizaForegift.getPriceId());
            List<NotNullMap> bizaPriceList = new ArrayList<NotNullMap>();
            for (VipRentPeriodPrice bizaPacketPeriodPrice : vipPeriodPriceList) {
                NotNullMap notNullMap2 = new NotNullMap();

                notNullMap2.put("dayCount", bizaPacketPeriodPrice.getDayCount());
                notNullMap2.put("price", bizaPacketPeriodPrice.getPrice());
                notNullMap2.put("memo", bizaPacketPeriodPrice.getMemo());

                bizaPriceList.add(notNullMap2);
            }
            notNullMap.put("priceList", bizaPriceList);

            packetPeriodList.add(notNullMap);
        }
        data.put("packetPeriodList", packetPeriodList);


        List<VipRentPriceCustomer> vipRentPriceCustomerList = vipRentPriceCustomerMapper.findListByPriceId(vipRentPrice.getId());

        List<NotNullMap> vipPriceCustomers = new ArrayList<NotNullMap>();
        for (VipRentPriceCustomer vipRentPriceCustomer : vipRentPriceCustomerList) {
            NotNullMap notNullMap2 = new NotNullMap();
            notNullMap2.put("id", vipRentPriceCustomer.getId());
            notNullMap2.put("mobile", vipRentPriceCustomer.getMobile());
            vipPriceCustomers.add(notNullMap2);
        }
        data.put("customerMobileList", vipPriceCustomers);

        List<VipRentPriceShop> vipPriceShopList = vipRentPriceShopMapper.findListByPriceId(vipRentPrice.getId());

        List<NotNullMap> vipPriceShops = new ArrayList<NotNullMap>();
        for (VipRentPriceShop vipRentPriceShop : vipPriceShopList) {
            NotNullMap notNullMap4 = new NotNullMap();
            notNullMap4.put("id", vipRentPriceShop.getId());
            notNullMap4.put("shopId", vipRentPriceShop.getShopId());
            notNullMap4.put("shopName", shopMapper.find(vipRentPriceShop.getShopId()).getShopName());
            vipPriceShops.add(notNullMap4);
        }
        data.put("shopIdList", vipPriceShops);

        List<VipRentPriceAgentCompany> vipRentPriceAgentCompanyList = vipRentPriceAgentCompanyMapper.findListByPriceId(vipRentPrice.getId());

        List<NotNullMap> vipRentPriceAgentCompanys = new ArrayList<NotNullMap>();
        for (VipRentPriceAgentCompany agentCompany : vipRentPriceAgentCompanyList) {
            NotNullMap notNullMap5 = new NotNullMap();
            notNullMap5.put("id", agentCompany.getId());
            notNullMap5.put("agentCompanyId", agentCompany.getAgentCompanyId());
            notNullMap5.put("agentCompanyName", agentCompanyMapper.find(agentCompany.getAgentCompanyId()).getCompanyName());
            vipRentPriceAgentCompanys.add(notNullMap5);
        }
        data.put("agentCompanyIdList", vipRentPriceAgentCompanys);


        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @Transactional(rollbackFor=Throwable.class)
    public RestResult delete(Long id) {
        List<VipRentPriceCustomer> customerList = vipRentPriceCustomerMapper.findListByPriceId(id);
        if(customerList.size() > 0) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"套餐包含骑手，不能删除。",null);
        }
        List<VipRentPriceShop> shopList = vipRentPriceShopMapper.findListByPriceId(id);
        if(shopList.size() > 0) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"套餐包含门店，不能删除。",null);
        }
        List<VipRentPriceAgentCompany> agentCompanyList = vipRentPriceAgentCompanyMapper.findListByPriceId(id);
        if(agentCompanyList.size() > 0) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"套餐包含运营公司，不能删除。",null);
        }
        vipRentBatteryForegiftMapper.deleteByPriceId(id);
        vipRentPriceMapper.delete(id);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

    @Transactional(rollbackFor=Throwable.class)
    public RestResult vipShopDelete(Long id) {
        vipRentPriceShopMapper.delete(id);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

    @Transactional(rollbackFor=Throwable.class)
    public RestResult vipAgentCompanyDelete(Long id) {
        vipRentPriceAgentCompanyMapper.delete(id);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

    @Transactional(rollbackFor=Throwable.class)
    public RestResult vipCustomerDelete(Long id) {
        vipRentPriceCustomerMapper.delete(id);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult foregiftList(Integer batteryType, Integer agentId) {

        List<Map> list = new ArrayList<Map>();

        List<RentBatteryForegift> rentBatteryForegiftList = rentBatteryForegiftMapper.findList(batteryType, agentId);
        for(RentBatteryForegift rentBatteryForegift : rentBatteryForegiftList){
            NotNullMap notNullMap = new NotNullMap();
            notNullMap.put("batteryType", rentBatteryForegift.getBatteryType());
            notNullMap.put("batteryTypeName", findBatteryType(rentBatteryForegift.getBatteryType()).getTypeName());
            notNullMap.put("foregiftId", rentBatteryForegift.getId());
            notNullMap.put("money", rentBatteryForegift.getMoney());
            notNullMap.put("memo", rentBatteryForegift.getMemo());
            list.add(notNullMap);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);

    }
}
