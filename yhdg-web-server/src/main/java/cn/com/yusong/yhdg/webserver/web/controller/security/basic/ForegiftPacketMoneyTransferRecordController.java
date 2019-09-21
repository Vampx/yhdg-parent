package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInOutMoney;
import cn.com.yusong.yhdg.common.domain.basic.ForegiftPacketMoneyTransferRecord;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.ForegiftPacketMoneyTransferRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/basic/foregift_packet_money_transfer_record")
public class ForegiftPacketMoneyTransferRecordController extends SecurityController {

    @Autowired
    ForegiftPacketMoneyTransferRecordService foregiftPacketMoneyTransferRecordService;

    @RequestMapping(value = "index.htm")
    public void index(Model model, String customerId) {
        model.addAttribute("customerId", customerId);
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(ForegiftPacketMoneyTransferRecord search) {
        return PageResult.successResult(foregiftPacketMoneyTransferRecordService.findPage(search));
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, Long id) {
        ForegiftPacketMoneyTransferRecord record = foregiftPacketMoneyTransferRecordService.find(id);
        if(record == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", record);
        }
        return "/security/basic/foregift_packet_money_transfer_record/view";
    }

}
