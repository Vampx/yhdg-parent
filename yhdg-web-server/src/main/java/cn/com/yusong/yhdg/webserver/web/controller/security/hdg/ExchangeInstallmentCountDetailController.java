package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentCount;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentCountDetail;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentSetting;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.webserver.service.hdg.ExchangeInstallmentCountDetailService;
import cn.com.yusong.yhdg.webserver.service.hdg.ExchangeInstallmentCountService;
import cn.com.yusong.yhdg.webserver.service.hdg.ExchangeInstallmentSettingService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/security/hdg/exchange_installment_count_detail")
public class ExchangeInstallmentCountDetailController extends SecurityController {

    @Autowired
    ExchangeInstallmentCountDetailService exchangeInstallmentCountDetailService;

    @Autowired
    ExchangeInstallmentCountService exchangeInstallmentCountService;

    @Autowired
    ExchangeInstallmentSettingService exchangeInstallmentSettingService;

    @RequestMapping(value = "add.htm")
    public void add(Model model,String urlPid) {
        model.addAttribute("StagingTime", ExchangeInstallmentCountDetail.StagingTime.values());
        model.addAttribute("urlPid",urlPid);
    }

    @RequestMapping(value = "edit.htm")
    public void edit(Model model,String urlPid,
         Integer num,Integer countId,Integer feeType,Double feeMoney,Double feePercentage,
         Integer[]customNums, Integer[]minForegiftPercentages,Integer[]minPacketPeriodPercentages) {
        model.addAttribute("StagingTime", ExchangeInstallmentCountDetail.StagingTime.values());
        model.addAttribute("urlPid",urlPid);
        model.addAttribute("countId",countId);
        model.addAttribute("num",num);
        model.addAttribute("customNums",customNums);
        model.addAttribute("feeType",feeType);
        model.addAttribute("feeMoney",feeMoney);
        model.addAttribute("feePercentage",feePercentage);
        model.addAttribute("minForegiftPercentages",minForegiftPercentages);
        model.addAttribute("minPacketPeriodPercentages",minPacketPeriodPercentages);
    }


    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(Long countId) {
        if(countId == null){
            return ExtResult.failResult("分期ID为空！");
        }
        return exchangeInstallmentCountService.delete(countId);
    }
    @RequestMapping("update_installment_count_detail.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateInstallmentCountDetail(Long countId,
     Integer feeType,Double feeMoney,Double feePercentage,Integer[]minForegiftPercentages,Integer[]minPacketPeriodPercentages) {
        if(countId == null){
            return ExtResult.failResult("分期ID为空！");
        }
        ExchangeInstallmentCount exchangeInstallmentCount = exchangeInstallmentCountService.find(countId);
        if(exchangeInstallmentCount == null){
            return ExtResult.failResult("未找到此分期！");
        }
        if(minForegiftPercentages.length!=minPacketPeriodPercentages.length){
            return ExtResult.failResult("数据传输错误请检查！");
        }
        List<Long> longs = new ArrayList<Long>(); //需要删除的
        List<ExchangeInstallmentCountDetail> countId1 = exchangeInstallmentCountDetailService.findCountId(countId);

        int array = minForegiftPercentages.length;
        if(array > countId1.size()){//需要新增的
            for (int j = countId1.size(); j < array; j++) {
                ExchangeInstallmentCountDetail exchangeInstallmentCountDetail =new ExchangeInstallmentCountDetail();
                exchangeInstallmentCountDetail.setFeeType(ExchangeInstallmentCountDetail.FeeType.NO_HANDLING_FEE.getValue());
                exchangeInstallmentCountDetail.setFeeMoney(0);
                exchangeInstallmentCountDetail.setFeePercentage(0);
                exchangeInstallmentCountDetail.setMinForegiftPercentage(minForegiftPercentages[j] == null? 0 :minForegiftPercentages[j]);
                exchangeInstallmentCountDetail.setMinPacketPeriodPercentage(minPacketPeriodPercentages[j] == null? 0 :minPacketPeriodPercentages[j]);
                exchangeInstallmentCountDetail.setCountId(countId);
                exchangeInstallmentCountDetail.setNum(j+1);
                exchangeInstallmentCountDetail.setMinPacketPeriodMoney(0);
                exchangeInstallmentCountDetail.setMinForegiftMoney(0);
                exchangeInstallmentCountDetailService.insert(exchangeInstallmentCountDetail);
            }


        }else if(array < countId1.size()){
            int z =1;
            for(ExchangeInstallmentCountDetail exchangeInstallmentCountDetail2: countId1) {
                if(z<=array){
                    z++;
                    continue;
                }
                longs.add(exchangeInstallmentCountDetail2.getId());
            }
            for (Long  detailId: longs) {
                exchangeInstallmentCountDetailService.delete(detailId);
            }
        }
        Integer i=0;
        List<ExchangeInstallmentCountDetail> countId2 = exchangeInstallmentCountDetailService.findCountId(countId);
        for(ExchangeInstallmentCountDetail exchangeInstallmentCountDetail: countId2) {
            exchangeInstallmentCountDetail.setMinForegiftPercentage(minForegiftPercentages[i] == null? 0 :minForegiftPercentages[i]);
            exchangeInstallmentCountDetail.setMinPacketPeriodPercentage(minPacketPeriodPercentages[i] == null? 0 :minPacketPeriodPercentages[i]);
            i += exchangeInstallmentCountDetailService.update(exchangeInstallmentCountDetail);
        }
        exchangeInstallmentCount.setCount(array);
        exchangeInstallmentCount.setFeeMoney((int)(feeMoney*100));
        exchangeInstallmentCount.setFeeType(feeType);
        exchangeInstallmentCount.setFeePercentage((int)(feePercentage*100));
        exchangeInstallmentCountService.update(exchangeInstallmentCount);

        return ExtResult.successResult("修改成功");
    }



    @RequestMapping("insert_installment_count_detail.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult insertInstallmentCountDetail(Long settingId,Integer num, Integer feeType,Double feeMoney,Double feePercentage,
        Integer[]minForegiftPercentages,Integer[]minPacketPeriodPercentages) {
        if(settingId == null){
            return ExtResult.failResult("分期设置ID为空！");
        }
        ExchangeInstallmentSetting exchangeInstallmentSetting = exchangeInstallmentSettingService.find(settingId);
        if(exchangeInstallmentSetting == null){
            return ExtResult.failResult("此分期设置不存在！");
        }
        if(minPacketPeriodPercentages.length!=minForegiftPercentages.length){
            return ExtResult.failResult("数据传输错误请检查！");
        }
        return exchangeInstallmentCountDetailService.insertDetail(settingId,num,
                feeType,feeMoney,feePercentage,
                minForegiftPercentages,minPacketPeriodPercentages);

    }

}
