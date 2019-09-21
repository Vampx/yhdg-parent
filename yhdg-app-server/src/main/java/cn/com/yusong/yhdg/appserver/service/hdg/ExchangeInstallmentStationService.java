package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.CabinetMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.ExchangeInstallmentCabinetMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.ExchangeInstallmentSettingMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.ExchangeInstallmentStationMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentCabinet;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentStation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ExchangeInstallmentStationService extends AbstractService {


    @Autowired
    ExchangeInstallmentStationMapper exchangeInstallmentStationMapper;
    @Autowired
    ExchangeInstallmentSettingMapper exchangeInstallmentSettingMapper;
    @Autowired
    CabinetMapper cabinetMapper;

    public ExchangeInstallmentStation findStationId(String stationId){
        return exchangeInstallmentStationMapper.findStationId(stationId);
    }


    public List<ExchangeInstallmentStation> findSettingId(Long settingId){
        return exchangeInstallmentStationMapper.findSettingId(settingId);
    }
}
