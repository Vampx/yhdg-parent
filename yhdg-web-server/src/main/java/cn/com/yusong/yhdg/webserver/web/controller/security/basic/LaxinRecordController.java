package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.LaxinRecord;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.entity.SessionUser;
import cn.com.yusong.yhdg.webserver.service.basic.LaxinRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/security/basic/laxin_record")
public class LaxinRecordController extends SecurityController {
    @Autowired
    LaxinRecordService laxinRecordService;

    @SecurityControl(limits = "basic.LaxinRecord:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "basic.LaxinRecord:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(LaxinRecord search) {
        return PageResult.successResult(laxinRecordService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view.htm")
    public String view(String id, Model model) {
        LaxinRecord laxinRecord = laxinRecordService.find(id);
        if(laxinRecord == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("id", id);
        return "/security/basic/laxin_record/view";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view_basic_info.htm")
    public String view_basic_info(String id, Model model) {
        LaxinRecord laxinRecord = laxinRecordService.find(id);
        if(laxinRecord == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", laxinRecord);
        return "/security/basic/laxin_record/view_basic_info";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view_transfer_log.htm")
    public String view_laxin_record(String id, Model model) {
        LaxinRecord laxinRecord = laxinRecordService.find(id);
        if(laxinRecord == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", laxinRecord);
        return "/security/basic/laxin_record/view_transfer_log";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "reset_account.htm", method = RequestMethod.GET)
    public String reset_account(String id, Model model) {
        LaxinRecord laxinRecord = laxinRecordService.find(id);
        if(laxinRecord == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", laxinRecord);
        return "/security/basic/laxin_record/reset_account";
    }

    @RequestMapping(value = "reset_account.htm", method = RequestMethod.POST)
    @ResponseBody
    public ExtResult reset_account(HttpSession httpSession, String id, String mpOpenId, String accountName) {
        SessionUser sessionUser = getSessionUser(httpSession);

        LaxinRecord laxinRecord = laxinRecordService.find(id);
        if (laxinRecord == null) {
            return ExtResult.failResult("拉新记录不存在");
        }
        if (laxinRecord.getStatus() != LaxinRecord.Status.FAIL.getValue()) {
            return ExtResult.failResult("只有付款失败才能重置");
        }
        laxinRecordService.resetAccount(laxinRecord.getId(), mpOpenId, accountName, sessionUser.getUsername());
        return ExtResult.successResult();
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("cancel.htm")
    public String cancel(String id, Model model) {
        LaxinRecord laxinRecord = laxinRecordService.find(id);
        if(laxinRecord == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("id", id);
        return "/security/basic/laxin_record/cancel";
    }

    @RequestMapping("update_cancel.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateCancel(LaxinRecord laxinRecord) {
        return laxinRecordService.updateCancel(laxinRecord);
    }

}
