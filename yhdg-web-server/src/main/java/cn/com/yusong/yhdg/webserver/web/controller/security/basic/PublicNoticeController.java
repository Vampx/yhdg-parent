package cn.com.yusong.yhdg.webserver.web.controller.security.basic;


import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.PublicNotice;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.basic.PublicNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/basic/public_notice")
public class PublicNoticeController extends SecurityController {

    @Autowired
    PublicNoticeService publicNoticeService;

    @SecurityControl(limits = "basic.PublicNotic:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute("noticeTypeEnum", PublicNotice.NoticeType.values());
        model.addAttribute(MENU_CODE_NAME, "basic.PublicNotic:list");
    }
    
    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(PublicNotice search) {
        return PageResult.successResult(publicNoticeService.findPage(search));
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
        model.addAttribute("noticeTypeEnum", PublicNotice.NoticeType.values());
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, long id) {
        PublicNotice entity = publicNoticeService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("noticeTypeEnum", PublicNotice.NoticeType.values());
            model.addAttribute("entity", entity);
        }
        return "/security/basic/public_notice/edit";
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(PublicNotice publicNotice) {
        return publicNoticeService.create(publicNotice);
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(PublicNotice publicNotice) {
        return publicNoticeService.update(publicNotice);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(long id) {
        return publicNoticeService.delete(id);
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, long id) {
        PublicNotice entity = publicNoticeService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("noticeTypeEnum", PublicNotice.NoticeType.values());
            model.addAttribute("entity", entity);
        }
        return "/security/basic/public_notice/view";
    }
}
