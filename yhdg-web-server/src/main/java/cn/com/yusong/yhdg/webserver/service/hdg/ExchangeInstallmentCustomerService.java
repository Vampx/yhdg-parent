package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.WeixinPayOrder;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentCustomer;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ExchangeInstallmentCustomerMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExchangeInstallmentCustomerService extends AbstractService{

    @Autowired
    ExchangeInstallmentCustomerMapper exchangeInstallmentCustomerMapper;

    public ExchangeInstallmentCustomer findCustomerMobile(ExchangeInstallmentCustomer customer){
        return exchangeInstallmentCustomerMapper.findCustomerMobile(customer.getCustomerMobile());
    }

    public  int insert(ExchangeInstallmentCustomer customer){
        return exchangeInstallmentCustomerMapper.insert(customer);
    }

    public int update(ExchangeInstallmentCustomer customer){
        return exchangeInstallmentCustomerMapper.update(customer);
    }

    public Page findPage(ExchangeInstallmentCustomer search) {
        Page page = search.buildPage();
        page.setTotalItems(exchangeInstallmentCustomerMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(exchangeInstallmentCustomerMapper.findPageResult(search));
        return page;
    }

    public ExtResult deleteCustomerMobile( String customerMobile,Long settingId){
        if(StringUtils.isBlank(customerMobile)){
            return ExtResult.failResult("手机号不能为空！");
        }
        if(settingId == null){
            return ExtResult.failResult("分期设置ID为空！");
        }
        ExchangeInstallmentCustomer customerMobile11= new ExchangeInstallmentCustomer();
        customerMobile11.setSettingId(settingId);
        customerMobile11.setCustomerMobile(customerMobile);
        ExchangeInstallmentCustomer customerMobile1 = exchangeInstallmentCustomerMapper.findCustomerMobile(customerMobile11.getCustomerMobile());
        if(customerMobile1==null){
            return ExtResult.failResult("未找到绑定骑手！");
        }
        int i = exchangeInstallmentCustomerMapper.deleteCustomerMobile(customerMobile1);
        if (i==0){
            return ExtResult.failResult("解绑骑手失败！");
        }

        return ExtResult.successResult("解绑成功");
    }

    public List<ExchangeInstallmentCustomer> findSettingId (long settingId){
        return exchangeInstallmentCustomerMapper.findSettingId(settingId);
    }


}
