package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Feedback;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.basic.FeedbackService;
import org.apache.tools.ant.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by chen on 2017/10/31.
 */
@Controller
@RequestMapping(value = "/security/basic/feedback")
public class FeedbackController extends SecurityController {
    @Autowired
    FeedbackService feedbackService;

    @SecurityControl(limits = "basic.Feedback:list")
    @RequestMapping("index.htm")
    public void index(Model model, Integer today) {
        if (today == null) {
            model.addAttribute("today", 0);
        } else {
            model.addAttribute("today", today);
        }
        model.addAttribute("staticUrl", appConfig.getStaticUrl());
        model.addAttribute(MENU_CODE_NAME, "basic.Feedback:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(Feedback search) {
        if (search.getToday() == ConstEnum.Flag.TRUE.getValue()) {
            Calendar begin = Calendar.getInstance();
            begin.set(Calendar.HOUR, 0);
            begin.set(Calendar.MINUTE, 0);
            begin.set(Calendar.SECOND, 0);
            begin.set(Calendar.MILLISECOND, 0);
            Calendar end = Calendar.getInstance();
            end.set(Calendar.HOUR, 23);
            end.set(Calendar.MINUTE, 59);
            end.set(Calendar.SECOND, 59);
            end.set(Calendar.MILLISECOND, 999);
            search.setQueryBeginTime(begin.getTime());
            search.setQueryEndTime(end.getTime());
        }
        return PageResult.successResult(feedbackService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view.htm")
    public String view(long id, Model model) {
        Feedback feedback = feedbackService.find(id);
        if (feedback == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", feedback);
        return "/security/basic/feedback/view";
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(long id) {
        feedbackService.delete(id);
        return ExtResult.successResult();
    }


}
