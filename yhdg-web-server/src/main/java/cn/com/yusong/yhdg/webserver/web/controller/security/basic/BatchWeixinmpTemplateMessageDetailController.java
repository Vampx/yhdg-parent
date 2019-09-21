package cn.com.yusong.yhdg.webserver.web.controller.security.basic;


import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.BatchWeixinmpTemplateMessageDetail;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.BatchWeixinmpTemplateMessageDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/security/basic/batch_weixinmp_template_message_detail")
public class BatchWeixinmpTemplateMessageDetailController extends SecurityController {
    @Autowired
    BatchWeixinmpTemplateMessageDetailService batchWeixinmpTemplateMessageDetailService;

    @RequestMapping(value = "index.htm")
    public void index(Model model, Long batchId) {
        model.addAttribute("batchId", batchId);
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(BatchWeixinmpTemplateMessageDetail search) {
        return PageResult.successResult(batchWeixinmpTemplateMessageDetailService.findPage(search));
    }
}
