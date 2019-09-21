package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.agentserver.service.basic.AgentService;
import cn.com.yusong.yhdg.agentserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.agentserver.service.hdg.EstateService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.Estate;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(value = "/security/hdg/estate")
public class EstateController extends SecurityController {
    @Autowired
    EstateService estateService;
    @Autowired
    AgentService agentService;
    @Autowired
    CabinetService cabinetService;

    @SecurityControl(limits = "hdg.Estate:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "hdg.Estate:list");
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON_ARRAY)
    @RequestMapping(value = "tree.htm")
    public void tree(String dummy, Integer agentId, HttpServletResponse response) throws Exception {
        response.setContentType(ConstEnum.ContentType.JSON.getValue());
        Set<Integer> checkedSet = Collections.emptySet();
        estateService.tree(checkedSet, dummy, agentId, response.getOutputStream());
    }

    @ResponseBody
    @RequestMapping("find_estate.htm")
    @ViewModel(ViewModel.JSON)
    public ExtResult findEstate(long estateId) {
        Estate estate = estateService.find(estateId);
        return DataResult.successResult(estate);
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(Estate search) {
        return PageResult.successResult(estateService.findPage(search));
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model,Integer agentId) {
        model.addAttribute("activeStatusEnum", Estate.IsActive.values());
        model.addAttribute("AuthTypeEnum", Estate.AuthType.values());
        model.addAttribute("agentId", agentId);
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(Estate entity) {
        return estateService.insert(entity);
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, long id) {
        model.addAttribute("id", id);
        Estate entity = estateService.find(id);
        model.addAttribute("agentId", entity.getAgentId());
        return "/security/hdg/estate/edit";
    }

    @RequestMapping(value = "edit_basic.htm")
    public String editBasic(Model model, long id) {
        Estate entity = estateService.find(id);
        List<Cabinet> cabinetList = cabinetService.findListByEstateId(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            String pattern= Constant.HOUR_MINUTE;
            if (StringUtils.isNotEmpty(entity.getWorkTime()) && !StringUtils.trim(entity.getWorkTime()).equals("-")) {
                String beginTime = entity.getWorkTime().substring(0, pattern.length());
                String endTime = (entity.getWorkTime().substring(pattern.length() + 1));
                model.addAttribute("beginTime", beginTime);
                model.addAttribute("endTime", endTime);
            }
            entity.setCabinetList(cabinetList);
            model.addAttribute("entity", entity);
            model.addAttribute("activeStatusEnum", Estate.IsActive.values());
            model.addAttribute("AuthTypeEnum", Estate.AuthType.values());
        }
        return "/security/hdg/estate/edit_basic";
    }

    @RequestMapping(value = "edit_payee.htm")
    public void editPayee(Model model, long id, Integer agentId) {
        Estate entity = estateService.find(id);
        model.addAttribute("agentId", agentId);
        model.addAttribute("entity", entity);
    }


    @RequestMapping(value = "add_location.htm")
    public String addLocation(Model model) {
        model.addAttribute("lng", Constant.LNG);
        model.addAttribute("lat", Constant.LAT);
        return "/security/hdg/estate/add_location";
    }

    @RequestMapping(value = "edit_image.htm")
    public String editImage(Model model, long id) {
        Estate entity = estateService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/estate/edit_image";
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, long id) {
        Estate entity = estateService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/estate/view";
    }

    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, long id) {
        Estate entity = estateService.find(id);
        List<Cabinet> cabinetList = cabinetService.findListByEstateId(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            String pattern= Constant.HOUR_MINUTE;
            if (StringUtils.isNotEmpty(entity.getWorkTime()) && !StringUtils.trim(entity.getWorkTime()).equals("-")) {
                String beginTime = entity.getWorkTime().substring(0, pattern.length());
                String endTime = (entity.getWorkTime().substring(pattern.length() + 1));
                model.addAttribute("beginTime", beginTime);
                model.addAttribute("endTime", endTime);
            }
            entity.setCabinetList(cabinetList);
            model.addAttribute("entity", entity);
            model.addAttribute("activeStatusEnum", Estate.IsActive.values());
            model.addAttribute("AuthTypeEnum", Estate.AuthType.values());
        }
        return "/security/hdg/estate/view_basic";
    }

    @RequestMapping(value = "view_payee.htm")
    public String viewPayee(Model model, long id, Integer agentId) {
        Estate entity = estateService.find(id);
        model.addAttribute("agentId", agentId);
        model.addAttribute("entity", entity);
        return "/security/hdg/estate/view_payee";
    }



    @RequestMapping(value = "image.htm", method = RequestMethod.GET)
    public void image(String num, Model model){
        model.addAttribute("num", num);

    }

    @RequestMapping(value = "view_location.htm")
    public String viewLocation(Model model, long id) {
        Estate entity = estateService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/estate/view_location";
    }

    @RequestMapping(value = "view_image.htm")
    public String viewImage(Model model, long id) {
        Estate entity = estateService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/estate/view_image";
    }

    @RequestMapping("update_basic.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateBasic(Estate entity) {
        return estateService.updateBasic(entity);
    }

    @RequestMapping("update_pay_people.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updatePayPeople(long id, String payPeopleName, String payPeopleMpOpenId, String payPeopleFwOpenId, String payPeopleMobile, String payPassword) {
        estateService.updatePayPeople(id, payPeopleName, payPeopleMpOpenId, payPeopleFwOpenId, payPeopleMobile, CodecUtils.password(payPassword));
        return ExtResult.successResult();
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(long id) {
        return estateService.delete(id);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "select_estate.htm")
    public void selectEstate(Model model, Integer agentId) {
        if (agentId != null) {
            model.addAttribute("agentId", agentId);
        } else {
            model.addAttribute("agentId", null);
        }
    }

    @RequestMapping(value = "set_pay_people.htm")
    public String setPayPeople(Model model, long id) {
        model.addAttribute("id", id);
        Estate estate = estateService.find(id);
        model.addAttribute("agentId", estate.getAgentId());
        return "/security/hdg/estate/set_pay_people";
    }

    @RequestMapping("set_pay_people_update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult setPayPeopleUpdate(Estate entity) {
        return  estateService.setPayPeopleUpdate(entity);
    }

}
