package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.agentserver.service.basic.CustomerCouponTicketGiftService;
import cn.com.yusong.yhdg.agentserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.agentserver.service.basic.WagesDayTicketGiftService;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.CustomerCouponTicketGift;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 优惠券赠送
 */
@Controller
@RequestMapping(value = "/security/basic/customer_coupon_ticket_gift_vehicle")
public class CustomerCouponTicketGiftVehicleController extends SecurityController {

    @Autowired
    CustomerCouponTicketGiftService customerCouponTicketGiftService;
    @Autowired
    WagesDayTicketGiftService wagesDayTicketGiftService;
    @Autowired
    CustomerService customerService;


    @SecurityControl(limits = "zc.CustomerCouponTicketGiftVehicle:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "zc.CustomerCouponTicketGiftVehicle:list");
        model.addAttribute("TypeEnum", CustomerCouponTicketGift.Type.vehicleTypeList);
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CustomerCouponTicketGift search) {
        if (search.getAgentId() == null) {
            search.setAgentId(0);
        }
        search.setCategory(CustomerCouponTicketGift.Category.VEHICLE.getValue());
        return PageResult.successResult(customerCouponTicketGiftService.findPage(search));
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
        model.addAttribute("TypeEnum",  CustomerCouponTicketGift.Type.vehicleTypeList);
        model.addAttribute("StatusEnum", ConstEnum.Flag.values());
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(CustomerCouponTicketGift entity) {
        entity.setCategory(CustomerCouponTicketGift.Category.VEHICLE.getValue());
        return customerCouponTicketGiftService.create(entity);
    }

    @RequestMapping("edit.htm")
    @ViewModel(ViewModel.INNER_PAGE)
    public String edit(long id, Model model) {
        CustomerCouponTicketGift entity = customerCouponTicketGiftService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", entity);
        model.addAttribute("TypeEnum", CustomerCouponTicketGift.Type.vehicleTypeList);
        model.addAttribute("StatusEnum", ConstEnum.Flag.values());
        return "/security/basic/customer_coupon_ticket_gift_vehicle/edit";
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(CustomerCouponTicketGift entity) {
        return customerCouponTicketGiftService.update(entity);
    }

    @RequestMapping("view.htm")
    @ViewModel(ViewModel.INNER_PAGE)
    public String view(long id, Model model) {
        CustomerCouponTicketGift entity = customerCouponTicketGiftService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", entity);
        model.addAttribute("TypeEnum", CustomerCouponTicketGift.Type.vehicleTypeList);
        model.addAttribute("StatusEnum", ConstEnum.Flag.values());
        return "/security/basic/customer_coupon_ticket_gift_vehicle/view";
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(long id) {
        return customerCouponTicketGiftService.delete(id);
    }

}
