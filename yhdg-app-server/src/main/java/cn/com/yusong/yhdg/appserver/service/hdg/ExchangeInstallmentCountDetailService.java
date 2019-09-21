package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.ExchangeInstallmentCountDetailMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.ExchangeInstallmentCountMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.ExchangeInstallmentSettingMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentCount;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentCountDetail;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentSetting;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExchangeInstallmentCountDetailService extends AbstractService {

    @Autowired
    ExchangeInstallmentCountDetailMapper exchangeInstallmentCountDetailMapper;
    @Autowired
    ExchangeInstallmentSettingMapper exchangeInstallmentSettingMapper;
    @Autowired
    ExchangeInstallmentCountMapper exchangeInstallmentCountMapper;


    public ExchangeInstallmentCountDetail find (long id){
        return exchangeInstallmentCountDetailMapper.find(id);
    }

    public List<ExchangeInstallmentCountDetail> findCountId (long countId){
        return exchangeInstallmentCountDetailMapper.findCountId(countId);
    }
}
