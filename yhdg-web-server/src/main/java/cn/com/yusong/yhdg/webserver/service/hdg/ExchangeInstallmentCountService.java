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
public class ExchangeInstallmentCountService extends AbstractService{

    @Autowired
    ExchangeInstallmentCountMapper exchangeInstallmentCountMapper;

    @Autowired
    ExchangeInstallmentSettingMapper exchangeInstallmentSettingMapper;

    @Autowired
    ExchangeInstallmentCountDetailMapper exchangeInstallmentCountDetailMapper;
    public ExchangeInstallmentCount find(long id){
        return exchangeInstallmentCountMapper.find(id);
    }

    public List<ExchangeInstallmentCount> findSettingId(Long settingId){
        return exchangeInstallmentCountMapper.findSettingId(settingId);
    }

    public ExtResult insert(ExchangeInstallmentCount installmentCount){

        if(installmentCount.getSettingId()==null){
            return ExtResult.failResult("分期设置ID为空！");
        }
        ExchangeInstallmentSetting exchangeInstallmentSetting = exchangeInstallmentSettingMapper.find(installmentCount.getSettingId());
        if(exchangeInstallmentSetting == null){
            return ExtResult.failResult("未找到此分期设置！");
        }
        if(installmentCount.getFeeType() ==null){
            return ExtResult.failResult("标准分期类型不能为空！");
        }
        if(installmentCount.getFeeType()==ExchangeInstallmentCount.FeeType.RATE.getValue()){
            if(installmentCount.getFeePercentage()==null){
                return ExtResult.failResult("标准分期手续费百分比为空！");
            }
            installmentCount.setFeeMoney(0);
        }else if (installmentCount.getFeeType()==ExchangeInstallmentCount.FeeType.FIXED_HANDLING_FEE.getValue()){
            if(installmentCount.getFeeMoney()==null){
                return ExtResult.failResult("标准分期手续费金额为空！");
            }
            installmentCount.setFeePercentage(0);
        }
        int insert = exchangeInstallmentCountMapper.insert(installmentCount);
        if(insert==0){
            return ExtResult.failResult("新建标准分期失败！");
        }

        return DataResult.successResult(installmentCount.getId());
    }
    @Transactional(rollbackFor = Throwable.class)
    public ExtResult update(ExchangeInstallmentCount installmentCount){
        ExchangeInstallmentCount exchangeInstallmentCount = exchangeInstallmentCountMapper.find(installmentCount.getId());
        if(exchangeInstallmentCount == null){
            return ExtResult.failResult("未找到此标准分期！");
        }
        if(installmentCount.getFeeType() ==null){
            return ExtResult.failResult("标准分期类型不能为空！");
        }
        if(installmentCount.getFeeType()==ExchangeInstallmentCount.FeeType.RATE.getValue()){
            if(installmentCount.getFeePercentage()==null){
                return ExtResult.failResult("标准分期手续费百分比为空！");
            }
            installmentCount.setFeeMoney(0);
        }else if (installmentCount.getFeeType()==ExchangeInstallmentCount.FeeType.FIXED_HANDLING_FEE.getValue()){
            if(installmentCount.getFeeMoney()==null){
                return ExtResult.failResult("标准分期手续费金额为空！");
            }
            installmentCount.setFeePercentage(0);
        }
        int update = exchangeInstallmentCountMapper.update(installmentCount);
        if(update==0){
            return ExtResult.failResult("修改标准分期失败！");
        }

        return DataResult.successResult(installmentCount.getId());
    }
    @Transactional(rollbackFor = Throwable.class)
    public ExtResult delete(@Param("id") long id){
        ExchangeInstallmentCount exchangeInstallmentCount = exchangeInstallmentCountMapper.find(id);
        if(exchangeInstallmentCount==null){
            return ExtResult.failResult("未找到此分期！");
        }
        ExchangeInstallmentSetting exchangeInstallmentSetting = exchangeInstallmentSettingMapper.find(exchangeInstallmentCount.getSettingId());
        if(exchangeInstallmentSetting == null){
            return ExtResult.failResult("设置ID不存在！");
        }
        if(exchangeInstallmentSetting.getSettingType() == ExchangeInstallmentSetting.SettingType.CUSTOM_STAGING.getValue()){
            List<ExchangeInstallmentCountDetail> countId = exchangeInstallmentCountDetailMapper.findCountId(exchangeInstallmentCount.getId());
            int size = countId.size();
            int delete =0;
            if(size!=0){
                for (ExchangeInstallmentCountDetail exchangeInstallmentCountDetail : countId) {
                    delete += exchangeInstallmentCountDetailMapper.delete(exchangeInstallmentCountDetail.getId());
                }
            }
            if(size!=delete){
                return ExtResult.failResult("删除分期详细失败！");
            }

        }
        int delete = exchangeInstallmentCountMapper.delete(exchangeInstallmentCount.getId());
        if(delete==0){
            return ExtResult.failResult("删除失败！");
        }
        return ExtResult.successResult("删除成功");
    }


}
