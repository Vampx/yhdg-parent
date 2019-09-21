package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetCode;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.hdg.CabinetCodeService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 设备编号
 */
@Controller
@RequestMapping(value = "/security/hdg/cabinet_code")
public class CabinetCodeController extends SecurityController {

    @Autowired
    CabinetCodeService subcabinetCodeService;

//    @SecurityControl(limits = OperCodeConst.CODE_2_3_3_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model, HttpSession session) {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_02_03_03.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CabinetCode search) {
        return PageResult.successResult(subcabinetCodeService.findPage(search));
    }

    @RequestMapping(value = "swap.htm", method = RequestMethod.GET)
    public String swap(Model model, String id) {
        CabinetCode entity = subcabinetCodeService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/cabinet_code/swap";
    }

    @RequestMapping(value = "swap.htm", method = RequestMethod.POST)
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult swap(String from, String to) {
        return subcabinetCodeService.swap(from, to);
    }

}
