package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentCount;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentCountDetail;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentSetting;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ExchangeInstallmentCountDetailMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ExchangeInstallmentCountMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ExchangeInstallmentSettingMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
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

    public int findPageCount(ExchangeInstallmentCountDetail countDetail){
        return exchangeInstallmentCountDetailMapper.findPageCount(countDetail);
    }

    public List<ExchangeInstallmentCountDetail> findPageResult(ExchangeInstallmentCountDetail countDetail){
        return exchangeInstallmentCountDetailMapper.findPageResult(countDetail);
    }

    public ExchangeInstallmentCountDetail find (long id){
        return exchangeInstallmentCountDetailMapper.find(id);
    }

    public List<ExchangeInstallmentCountDetail> findCountId (long countId){
        return exchangeInstallmentCountDetailMapper.findCountId(countId);
    }
    @Transactional(rollbackFor=Throwable.class)
    public int insert(ExchangeInstallmentCountDetail countDetail){
        return exchangeInstallmentCountDetailMapper.insert(countDetail);
    }

    public int update(ExchangeInstallmentCountDetail countDetail){
        return exchangeInstallmentCountDetailMapper.update(countDetail);
    }
    @Transactional(rollbackFor=Throwable.class)
    public ExtResult insertDetail(Long settingId, Integer num,
      Integer feeType, Double feeMoney, Double feePercentage,
      Integer[]minForegiftPercentages, Integer[]minPacketPeriodPercentages){
        int insert =0;
        ExchangeInstallmentSetting exchangeInstallmentSetting = exchangeInstallmentSettingMapper.find(settingId);
        ExchangeInstallmentCount exchangeInstallmentCount =new ExchangeInstallmentCount();
        exchangeInstallmentCount.setFeeType(feeType);
        exchangeInstallmentCount.setCount(num);
        exchangeInstallmentCount.setSettingId(exchangeInstallmentSetting.getId());
        exchangeInstallmentCount.setFeeMoney(feeMoney==null?0:(int)(feeMoney*100));
        exchangeInstallmentCount.setFeePercentage(feePercentage==null?0:(int)(feePercentage*100));
        exchangeInstallmentCountMapper.insert(exchangeInstallmentCount);
        for (int i = 0; i < minPacketPeriodPercentages.length; i++) {
            ExchangeInstallmentCountDetail exchangeInstallmentCountDetail =new ExchangeInstallmentCountDetail();
            exchangeInstallmentCountDetail.setNum(i+1);
            exchangeInstallmentCountDetail.setFeeType(ExchangeInstallmentCount.FeeType.NO_HANDLING_FEE.getValue());
            exchangeInstallmentCountDetail.setCountId(exchangeInstallmentCount.getId());
            exchangeInstallmentCountDetail.setFeeMoney(0);
            exchangeInstallmentCountDetail.setFeePercentage(0);
            exchangeInstallmentCountDetail.setMinForegiftMoney(0);
            exchangeInstallmentCountDetail.setMinForegiftPercentage(minForegiftPercentages[i]==null?0:minForegiftPercentages[i]);
            exchangeInstallmentCountDetail.setMinPacketPeriodMoney(0);
            exchangeInstallmentCountDetail.setMinPacketPeriodPercentage(minPacketPeriodPercentages[i]==null?0:minPacketPeriodPercentages[i]);
            insert += exchangeInstallmentCountDetailMapper.insert(exchangeInstallmentCountDetail);
        }
        if(insert!=minPacketPeriodPercentages.length){
            return ExtResult.failResult("添加自定义分期失败！");
        }else{
            return  DataResult.successResult(exchangeInstallmentCount.getId());
        }
    }

    public int delete(@Param("id") long id){
        return exchangeInstallmentCountDetailMapper.delete(id);
    }

}
