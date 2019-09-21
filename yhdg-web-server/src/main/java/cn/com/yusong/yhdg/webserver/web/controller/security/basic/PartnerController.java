package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.PartnerInOutMoney;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.basic.PartnerInOutMoneyService;
import cn.com.yusong.yhdg.webserver.service.basic.PartnerService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/security/basic/partner")
public class PartnerController extends SecurityController {

    @Autowired
    PartnerService partnerService;
    @Autowired
    PartnerInOutMoneyService partnerInOutMoneyService;

    @SecurityControl(limits = "basic.Partner:list")
    @RequestMapping("index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "basic.Partner:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(Partner search) {
        return PageResult.successResult(partnerService.findPage(search));
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "find.htm")
    public Partner find(int id) {
        return partnerService.find(id);
    }
    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "list.htm")
    public List list(String dummy) {
        List<Map> list = new ArrayList<Map>();

        if (StringUtils.isNotEmpty(dummy)) {
            Map line = new HashMap();
            line.put("id", "");
            line.put("partnerName", dummy);
            list.add(line);
        }

        for (Partner partner : partnerService.findList(null)) {
            Map line = new HashMap();
            line.put("id", partner.getId());
            line.put("partnerName", partner.getPartnerName());
            list.add(line);
        }

        return list;
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("add.htm")
    public void add(Model model) {
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("edit.htm")
    public void edit(Integer id, Model model) {
        Partner partner = partnerService.find(id);
        model.addAttribute("entity", partner);
    }


    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("add_basic.htm")
    public void addBasic(Integer id, Model model) {
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("edit_basic.htm")
    public void editBasic(Integer id, Model model) {
        Partner partner = partnerService.find(id);
        model.addAttribute("entity", partner);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("edit_mp.htm")
    public void editMp(Integer id, Model model) {
        Partner partner = partnerService.find(id);
        model.addAttribute("entity", partner);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("edit_ma.htm")
    public void editMa(Integer id, Model model) {
        Partner partner = partnerService.find(id);
        model.addAttribute("entity", partner);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("edit_fw.htm")
    public void editFw(Integer id, Model model) {
        Partner partner = partnerService.find(id);
        model.addAttribute("entity", partner);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("edit_alipay.htm")
    public void editAlipay(Integer id, Model model) {
        Partner partner = partnerService.find(id);
        model.addAttribute("entity", partner);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("edit_weixin.htm")
    public void editWeixin(Integer id, Model model) {
        Partner partner = partnerService.find(id);
        model.addAttribute("entity", partner);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view_in_out_money.htm")
    public void viewInOutMoney(Integer id, Model model) {
        Partner partner = partnerService.find(id);
        model.addAttribute("entity", partner);
    }

    @RequestMapping("in_out_money_page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult inOutMoneyPage(PartnerInOutMoney search) {
        return PageResult.successResult(partnerInOutMoneyService.findPage(search));
    }

    @ResponseBody
    @RequestMapping(value = "create.htm")
    public ExtResult create(Partner partner) {
        if (partner.getId() == null) {
            return partnerService.create(partner, appConfig.mobileMessageTemplateSql);
        } else {
            return partnerService.update(partner);
        }
    }

    @ResponseBody
    @RequestMapping(value = "update.htm")
    public ExtResult update(Partner partner) {
        return partnerService.update(partner);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view.htm")
    public void view(int id, Model model) {
        Partner partner = partnerService.find(id);

        model.addAttribute("entity", partner);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(int id) {
        return partnerService.delete(id);
    }
}
