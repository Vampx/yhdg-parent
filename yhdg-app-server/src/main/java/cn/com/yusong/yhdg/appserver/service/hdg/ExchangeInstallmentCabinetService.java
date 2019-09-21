package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.CabinetMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.ExchangeInstallmentCabinetMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.ExchangeInstallmentSettingMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentCabinet;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentSetting;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ExchangeInstallmentCabinetService  extends AbstractService {


    @Autowired
    ExchangeInstallmentCabinetMapper exchangeInstallmentCabinetMapper;
    @Autowired
    ExchangeInstallmentSettingMapper exchangeInstallmentSettingMapper;
    @Autowired
    CabinetMapper cabinetMapper;

    public ExchangeInstallmentCabinet findCabinetId( String cabinetId){
        return exchangeInstallmentCabinetMapper.findCabinetId(cabinetId);
    }


    public List<ExchangeInstallmentCabinet> findSettingId(Long settingId){
        return exchangeInstallmentCabinetMapper.findSettingId(settingId);
    }
}
