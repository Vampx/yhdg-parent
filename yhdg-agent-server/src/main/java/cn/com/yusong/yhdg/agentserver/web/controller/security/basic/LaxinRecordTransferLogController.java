package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.LaxinRecordTransferLog;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.service.basic.LaxinRecordTransferLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/basic/laxin_record_transfer_log")
public class LaxinRecordTransferLogController extends SecurityController {

    @Autowired
    LaxinRecordTransferLogService laxinRecordTransferLogService;

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(LaxinRecordTransferLog search) {
        return PageResult.successResult(laxinRecordTransferLogService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view.htm")
    public String view(long id, Model model) {
        LaxinRecordTransferLog log = laxinRecordTransferLogService.find(id);
        if(log == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", log);
        return "/security/basic/laxin_record_transfer_log/view";
    }
}
