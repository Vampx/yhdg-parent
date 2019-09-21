package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecord;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentSetting;
import cn.com.yusong.yhdg.common.domain.zd.RentInstallmentSetting;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerInstallmentRecordMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ExchangeInstallmentSettingMapper;
import cn.com.yusong.yhdg.webserver.persistence.zd.RentInstallmentSettingMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerInstallmentRecordService extends AbstractService{

    @Autowired
    CustomerInstallmentRecordMapper customerInstallmentRecordMapper;
    @Autowired
    ExchangeInstallmentSettingMapper exchangeInstallmentSettingMapper;
    @Autowired
    RentInstallmentSettingMapper rentInstallmentSettingMapper;
    @Autowired
    CustomerService customerService;

    public Page findPage(CustomerInstallmentRecord search) {
        Page page = search.buildPage();
        page.setTotalItems(customerInstallmentRecordMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<CustomerInstallmentRecord> list = customerInstallmentRecordMapper.findPageResult(search);
        for (CustomerInstallmentRecord customerInstallmentRecord : list) {
            Partner partner = findPartner(customerInstallmentRecord.getPartnerId());
            if (partner != null) {
                customerInstallmentRecord.setPartnerName(partner.getPartnerName());
            }
            if (customerInstallmentRecord.getCategory() == ConstEnum.Category.EXCHANGE.getValue()) {
                ExchangeInstallmentSetting exchangeInstallmentSetting = exchangeInstallmentSettingMapper.find(customerInstallmentRecord.getExchangeSettingId());
                if (exchangeInstallmentSetting != null) {
                   /* SystemBatteryType batteryType = findBatteryType(exchangeInstallmentSetting.getBatteryType());
                    if (batteryType != null) {
                        customerInstallmentRecord.setBatteryTypeName(batteryType.getTypeName());
                    }*/
                   /* customerInstallmentRecord.setForegiftMoney(exchangeInstallmentSetting.getForegiftMoney());
                    customerInstallmentRecord.setPacketMoney(exchangeInstallmentSetting.getPacketMoney());
                    customerInstallmentRecord.setInsuranceMoney(exchangeInstallmentSetting.getInsuranceMoney());*/
                }
            } else if (customerInstallmentRecord.getCategory() == ConstEnum.Category.RENT.getValue()){
                RentInstallmentSetting rentInstallmentSetting = rentInstallmentSettingMapper.find(customerInstallmentRecord.getRentSettingId());
                if (rentInstallmentSetting != null) {
                    /*SystemBatteryType batteryType = findBatteryType(rentInstallmentSetting.getBatteryType());
                    if (batteryType != null) {
                        customerInstallmentRecord.setBatteryTypeName(batteryType.getTypeName());
                    }*/
                    customerInstallmentRecord.setForegiftMoney(rentInstallmentSetting.getForegiftMoney());
                    customerInstallmentRecord.setPacketMoney(rentInstallmentSetting.getPacketMoney());
                    customerInstallmentRecord.setInsuranceMoney(rentInstallmentSetting.getInsuranceMoney());
                }
            }

        }
        page.setResult(list);
        return page;
    }

    public CustomerInstallmentRecord find(Long id) {
        CustomerInstallmentRecord customerInstallmentRecord = customerInstallmentRecordMapper.find(id);
        if (customerInstallmentRecord.getCategory() == ConstEnum.Category.EXCHANGE.getValue()) {
            ExchangeInstallmentSetting exchangeInstallmentSetting = exchangeInstallmentSettingMapper.find(customerInstallmentRecord.getExchangeSettingId());
            if (exchangeInstallmentSetting != null) {
                /*SystemBatteryType batteryType = findBatteryType(exchangeInstallmentSetting.getBatteryType());
                if (batteryType != null) {
                    customerInstallmentRecord.setBatteryTypeName(batteryType.getTypeName());
                }*/
                /*customerInstallmentRecord.setForegiftMoney(exchangeInstallmentSetting.getForegiftMoney());
                customerInstallmentRecord.setPacketMoney(exchangeInstallmentSetting.getPacketMoney());
                customerInstallmentRecord.setInsuranceMoney(exchangeInstallmentSetting.getInsuranceMoney());*/
            }
        } else if (customerInstallmentRecord.getCategory() == ConstEnum.Category.RENT.getValue()) {
            RentInstallmentSetting rentInstallmentSetting = rentInstallmentSettingMapper.find(customerInstallmentRecord.getRentSettingId());
            if (rentInstallmentSetting != null) {
               /* SystemBatteryType batteryType = findBatteryType(rentInstallmentSetting.getBatteryType());
                if (batteryType != null) {
                    customerInstallmentRecord.setBatteryTypeName(batteryType.getTypeName());
                }*/
                customerInstallmentRecord.setForegiftMoney(rentInstallmentSetting.getForegiftMoney());
                customerInstallmentRecord.setPacketMoney(rentInstallmentSetting.getPacketMoney());
                customerInstallmentRecord.setInsuranceMoney(rentInstallmentSetting.getInsuranceMoney());
            }
        }

        return customerInstallmentRecord;
    }

    public CustomerInstallmentRecord findByExchangeSettingId(Long exchangeSettingId, Integer category) {
        return customerInstallmentRecordMapper.findByExchangeSettingId(exchangeSettingId, category);
    }

    public CustomerInstallmentRecord findByRentSettingId(Long rentSettingId, Integer category) {
        return customerInstallmentRecordMapper.findByRentSettingId(rentSettingId, category);
    }
}
