package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.agentserver.service.basic.CustomerCouponTicketGiftService;
import cn.com.yusong.yhdg.agentserver.service.basic.WagesDayTicketGiftService;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.CustomerCouponTicket;
import cn.com.yusong.yhdg.common.domain.basic.CustomerCouponTicketGift;
import cn.com.yusong.yhdg.common.domain.basic.WagesDayTicketGift;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 优惠券赠送
 */
@Controller
@RequestMapping(value = "/security/basic/customer_coupon_ticket_gift")
public class CustomerCouponTicketGiftController extends SecurityController {

    @Autowired
    CustomerCouponTicketGiftService customerCouponTicketGiftService;
    @Autowired
    WagesDayTicketGiftService wagesDayTicketGiftService;
    @SecurityControl(limits = "basic.CustomerCouponTicketGift:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "basic.CustomerCouponTicketGift:list");
        model.addAttribute("TypeEnum", CustomerCouponTicketGift.Type.values());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CustomerCouponTicketGift search) {
        if (search.getAgentId() == null) {
            search.setAgentId(0);
        }
        search.setCategory(CustomerCouponTicket.Category.EXCHANGE.getValue());
        return PageResult.successResult(customerCouponTicketGiftService.findPage(search));
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
        model.addAttribute("TypeEnum", CustomerCouponTicketGift.Type.values());
        model.addAttribute("StatusEnum", ConstEnum.Flag.values());
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(CustomerCouponTicketGift entity) {
        entity.setCategory(CustomerCouponTicket.Category.EXCHANGE.getValue());
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
        model.addAttribute("TypeEnum", CustomerCouponTicketGift.Type.values());
        model.addAttribute("StatusEnum", ConstEnum.Flag.values());
        return "/security/basic/customer_coupon_ticket_gift/edit";
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
        model.addAttribute("TypeEnum", CustomerCouponTicketGift.Type.values());
        model.addAttribute("StatusEnum", ConstEnum.Flag.values());
        return "/security/basic/customer_coupon_ticket_gift/view";
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(long id) {
        return customerCouponTicketGiftService.delete(id);
    }


    @RequestMapping("binDing.htm")
    @ViewModel(ViewModel.INNER_PAGE)
    public String binDing(long id, Model model) {
        CustomerCouponTicketGift entity = customerCouponTicketGiftService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("ticketGiftId",id);
        return "/security/basic/customer_coupon_ticket_gift/binDing";
    }

    @RequestMapping("binDingPage.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult binDingPage(WagesDayTicketGift wagesDayTicketGift) {
        Page page = wagesDayTicketGiftService.findPage(wagesDayTicketGift);
        return PageResult.successResult(page);
    }

    @RequestMapping("untYing.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult untYing(Long id) {
        return  wagesDayTicketGiftService.del(id);
    }

    @RequestMapping("addRider.htm")
    @ViewModel(ViewModel.INNER_PAGE)
    public String addRider(long ticketGiftId,Model model) {
        CustomerCouponTicketGift entity = customerCouponTicketGiftService.find(ticketGiftId);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("ticketGiftId",ticketGiftId);
        return "/security/basic/customer_coupon_ticket_gift/addRider";
    }



    @RequestMapping("endBinding.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult endBinding(String mobile,long ticketGiftId) {

        CustomerCouponTicketGift entity = customerCouponTicketGiftService.find(ticketGiftId);
        if(entity==null){
            return ExtResult.failResult("未查到优惠卷信息");
        }
        if(StringUtils.isBlank(mobile)){
            return ExtResult.failResult("手机号不能为空");
        }
        String pattern = "0?(12|13|14|15|16|17|18|19)[0-9]{9}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(mobile);
        if(!m.matches()){
            return ExtResult.failResult("手机号格式不正确");
        }
        return wagesDayTicketGiftService.insert(mobile,entity);
    }
}
