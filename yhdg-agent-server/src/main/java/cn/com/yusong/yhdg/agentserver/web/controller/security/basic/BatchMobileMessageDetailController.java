package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.BatchMobileMessageDetail;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.service.basic.BatchMobileMessageDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by chen on 2017/10/30.
 */
@Controller
@RequestMapping(value = "/security/basic/batch_mobile_message_detail")
public class BatchMobileMessageDetailController extends SecurityController {
    @Autowired
    BatchMobileMessageDetailService batchMobileMessageDetailService;

    @RequestMapping(value = "index.htm")
    public void index(Model model, int batchId) {
        model.addAttribute("batchId", batchId);
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page( BatchMobileMessageDetail search) {
        return PageResult.successResult(batchMobileMessageDetailService.findPage(search));
    }
}
