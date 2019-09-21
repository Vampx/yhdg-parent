package cn.com.yusong.yhdg.webserver.service.zc;

import cn.com.yusong.yhdg.webserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.webserver.persistence.zc.GroupOrderMapper;
import cn.com.yusong.yhdg.webserver.persistence.zc.PriceSettingMapper;
import cn.com.yusong.yhdg.webserver.persistence.zc.RentPriceMapper;
import cn.com.yusong.yhdg.webserver.persistence.zc.VehicleModelMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.zc.GroupOrder;
import cn.com.yusong.yhdg.common.domain.zc.PriceSetting;
import cn.com.yusong.yhdg.common.domain.zc.RentPrice;
import cn.com.yusong.yhdg.common.domain.zc.VehicleModel;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.entity.tree.Node;
import cn.com.yusong.yhdg.common.entity.tree.NodeModel;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class PriceSettingService extends AbstractService {

    @Autowired
    PriceSettingMapper priceSettingMapper;
    @Autowired
    RentPriceMapper rentPriceMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    VehicleModelMapper vehicleModelMapper;
    @Autowired
    GroupOrderMapper groupOrderMapper;

    public void tree(Set<Integer> checked, String dummy, Integer agentId, ServletOutputStream stream) throws IOException {

        List<PriceSetting> list = priceSettingMapper.findListByAgentId(agentId);
        List<Node<NodeModel>> roots = new ArrayList<Node<NodeModel>>();
        for (PriceSetting priceSetting : list) {
            NodeModel nodeModel = new NodeModel();
            Node<NodeModel> root = new Node<NodeModel>(nodeModel);
            nodeModel.setId(priceSetting.getId());
            nodeModel.setName(priceSetting.getSettingName());
            roots.add(root);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator json = objectMapper.getJsonFactory().createJsonGenerator(stream, JsonEncoding.UTF8);
        json.writeStartArray();
        AppUtils.writeJson(roots, json);
        json.writeEndArray();
        json.flush();
        json.close();
    }

    public PriceSetting find(Long id) {
        return priceSettingMapper.find(id);
    }

    public Page findPage(PriceSetting search) {
        Page page = search.buildPage();
        page.setTotalItems(priceSettingMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<PriceSetting> list = priceSettingMapper.findPageResult(search);
        for (PriceSetting priceSetting : list) {
            if (priceSetting.getModelId() != null) {
                VehicleModel vehicleModel = vehicleModelMapper.find(priceSetting.getModelId());
                if (vehicleModel != null) {
                    priceSetting.setModelName(vehicleModel.getModelName());
                }
            }
        }
        page.setResult(list);
        return page;
    }

    public Page findNotShopPricePage(PriceSetting search) {
        Page page = search.buildPage();
        page.setTotalItems(priceSettingMapper.findNotShopPricePageCount(search));
        search.setBeginIndex(page.getOffset());
        List<PriceSetting> list = priceSettingMapper.findNotShopPricePageResult(search);
        for (PriceSetting priceSetting : list) {
            VehicleModel vehicleModel = vehicleModelMapper.find(priceSetting.getModelId());
            if (vehicleModel != null) {
                priceSetting.setModelName(vehicleModel.getModelName());
            }
        }
        page.setResult(list);
        return page;
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult create(PriceSetting entity) {
        if (priceSettingMapper.findByAgentId(entity.getAgentId(), entity.getCategory(), entity.getModelId()) != null) {
            return ExtResult.failResult("该运营商下存在同车型套餐！");
        }
        entity.setCreateTime(new Date());
        entity.setMinPrice(0);
        entity.setMaxPrice(0);
        Agent agent = agentMapper.find(entity.getAgentId());
        entity.setAgentName(agent.getAgentName());
        entity.setAgentCode(agent.getAgentCode());
        priceSettingMapper.insert(entity);
        return DataResult.successResult("操作成功",entity.getId());
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult update(PriceSetting entity, Integer batteryType,
                            String [] priceNameList,
                            Double [] foregiftPriceList,
                            Double [] vehicleForegiftPriceList,
                            Double [] batteryForegiftPriceList,
                            Double [] rentPriceList,
                            Integer [] dayCountList,
                            Double [] vehicleRentPriceList,
                            Double [] batteryRentPriceList,
                            Long [] priceIdList) {

        if (priceNameList == null || priceNameList.length == 0) {
            return ExtResult.failResult("请输入套餐名称");
        }

        for(int i = 0; i < priceNameList.length; i++) {
            if (priceNameList == null || priceNameList[i].equals("")) {
                return ExtResult.failResult("请输入套餐名称");
            }
            if (foregiftPriceList.length == 0 || foregiftPriceList[i].isNaN()) {
                return ExtResult.failResult("请输入套餐押金金额");
            }
            if (vehicleForegiftPriceList.length == 0 || vehicleForegiftPriceList[i].isNaN()) {
                return ExtResult.failResult("请输入套餐车辆押金金额");
            }
            if (entity.getCategory() != 3) {
                if (batteryForegiftPriceList.length == 0 || batteryForegiftPriceList[i].isNaN()) {
                    return ExtResult.failResult("请输入套餐电池押金金额");
                }
                BigDecimal bd1 = new BigDecimal(Double.toString(batteryForegiftPriceList[i]));
                BigDecimal bd2 = new BigDecimal(Double.toString(vehicleForegiftPriceList[i]));
                BigDecimal bd3 = new BigDecimal(Double.toString(foregiftPriceList[i]));
                if (bd1.add(bd2).doubleValue() != bd3.doubleValue()) {
                    return ExtResult.failResult("车辆或电池押金金额之和应等于套餐押金金额");
                }
            } else {
                if (!vehicleForegiftPriceList[i].equals(foregiftPriceList[i])) {
                    return ExtResult.failResult("车辆押金金额应等于套餐押金金额");
                }
            }

            if (rentPriceList.length == 0 || rentPriceList[i].isNaN()) {
                return ExtResult.failResult("请输入套餐租金金额");
            }
            if (dayCountList.length == 0 || dayCountList[i] == null) {
                return ExtResult.failResult("请输入套餐租金天数");
            }
            if (vehicleRentPriceList.length == 0 || vehicleRentPriceList[i].isNaN()) {
                return ExtResult.failResult("请输入套餐租金车辆金额");
            }
            if (entity.getCategory() != 3) {
                if (batteryRentPriceList.length == 0 || batteryRentPriceList[i].isNaN()) {
                    return ExtResult.failResult("请输入套餐租金电池金额");
                }
                BigDecimal bd1 = new BigDecimal(Double.toString(batteryRentPriceList[i]));
                BigDecimal bd2 = new BigDecimal(Double.toString(vehicleRentPriceList[i]));
                BigDecimal bd3 = new BigDecimal(Double.toString(rentPriceList[i]));
                if (bd1.add(bd2).doubleValue() != bd3.doubleValue()) {
                    return ExtResult.failResult("车辆或电池租金金额之和应等于套餐租金金额");
                }
            } else {
                if (!vehicleRentPriceList[i].equals(rentPriceList[i])) {
                    return ExtResult.failResult("车辆租金金额应等于套餐租金金额");
                }
            }
        }

        Agent agent = agentMapper.find(entity.getAgentId());
        entity.setAgentName(agent.getAgentName());
        entity.setAgentCode(agent.getAgentCode());
        entity.setBatteryType(batteryType);

        priceSettingMapper.update(entity);


        List<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < priceNameList.length; i++) {
            RentPrice rentPrice = new RentPrice();
            if (priceIdList[i] == 0) {
                rentPrice.setPriceSettingId(entity.getId());
                rentPrice.setBatteryType(batteryType);
                rentPrice.setAgentId(entity.getAgentId());
                rentPrice.setAgentName(entity.getAgentName());
                rentPrice.setAgentCode(entity.getAgentCode());
                rentPrice.setPriceName(priceNameList[i]);
                rentPrice.setForegiftPrice((int) (foregiftPriceList[i] * 100));
                rentPrice.setVehicleForegiftPrice((int) (vehicleForegiftPriceList[i] * 100));
                if (entity.getCategory() != 3) {
                    rentPrice.setBatteryForegiftPrice((int) (batteryForegiftPriceList[i] * 100));
                }
                rentPrice.setRentPrice((int) (rentPriceList[i] * 100));
                rentPrice.setDayCount(dayCountList[i]);
                rentPrice.setVehicleRentPrice((int) (vehicleRentPriceList[i] * 100));
                if (entity.getCategory() != 3) {
                    rentPrice.setBatteryRentPrice((int) (batteryRentPriceList[i] * 100));
                }
                rentPriceMapper.insert(rentPrice);
            } else {
                rentPrice.setId(priceIdList[i]);
                rentPrice.setPriceSettingId(entity.getId());
                rentPrice.setBatteryType(batteryType);
                rentPrice.setAgentId(entity.getAgentId());
                rentPrice.setAgentName(entity.getAgentName());
                rentPrice.setAgentCode(entity.getAgentCode());
                rentPrice.setPriceName(priceNameList[i]);
                rentPrice.setForegiftPrice((int) (foregiftPriceList[i] * 100));
                rentPrice.setVehicleForegiftPrice((int) (vehicleForegiftPriceList[i] * 100));
                if (entity.getCategory() != 3) {
                    rentPrice.setBatteryForegiftPrice((int) (batteryForegiftPriceList[i] * 100));
                }
                rentPrice.setRentPrice((int) (rentPriceList[i] * 100));
                rentPrice.setDayCount(dayCountList[i]);
                rentPrice.setVehicleRentPrice((int) (vehicleRentPriceList[i] * 100));
                if (entity.getCategory() != 3) {
                    rentPrice.setBatteryRentPrice((int) (batteryRentPriceList[i] * 100));
                }
                rentPriceMapper.update(rentPrice);
            }

            int price = (int) (foregiftPriceList[i] * 100 + rentPriceList[i] * 100);
            list.add(price);
        }
        Integer minPrice = 0;
        Integer maxPrice = 0;
        if(list.size() > 0){
            Collections.sort(list);//对于list排序
            minPrice = list.get(0);
            maxPrice = list.get(list.size()-1);
        }
        PriceSetting priceSetting = priceSettingMapper.find(entity.getId());
        if(priceSetting != null){
            priceSettingMapper.updatePrice(entity.getId(), minPrice, maxPrice);
        }
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(long id) {
        List<RentPrice> list = rentPriceMapper.findListBySetting(id);
        for (RentPrice rentPrice : list) {
            List<GroupOrder> groupOrderList = groupOrderMapper.findByRentPriceId(rentPrice.getId());
            if (groupOrderList.size() > 0) {
                return ExtResult.failResult("该套餐已生成订单不能删除");
            }
        }
        priceSettingMapper.delete(id);
        rentPriceMapper.deleteBySettingId(id);
        return ExtResult.successResult();
    }
}
