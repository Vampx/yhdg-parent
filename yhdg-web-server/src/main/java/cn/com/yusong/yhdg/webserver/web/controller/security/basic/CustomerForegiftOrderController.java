package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.HttpUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.entity.SessionUser;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerInstallmentRecordMapper;
import cn.com.yusong.yhdg.webserver.service.basic.*;
import cn.com.yusong.yhdg.webserver.service.hdg.PacketPeriodOrderService;
import com.alipay.api.AlipayApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping(value = "/security/basic/customer_foregift_order")
public class CustomerForegiftOrderController extends SecurityController {
    @Autowired
    CustomerForegiftOrderService customerForegiftOrderService;
    @Autowired
    AlipayPayOrderService alipayPayOrderService;
    @Autowired
    WeixinPayOrderService weixinPayOrderService;
    @Autowired
    PacketPeriodOrderService packetPeriodOrderService;
    @Autowired
    CustomerInstallmentRecordOrderDetailService customerInstallmentRecordOrderDetailService;
    @Autowired
    CustomerInstallmentRecordPayDetailService customerInstallmentRecordPayDetailService;

    final static String UPLOAD_FILE_URL = "/security/upload/attachment.htm?type=" + ConstEnum.AttachmentType.CUSTOMER_FOREGIFT_ORDER_REFUND_PHOTO_PATH.getValue();

    @SecurityControl(limits = "basic.CustomerForegiftOrder:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "basic.CustomerForegiftOrder:list");
        model.addAttribute("statusEnum", CustomerForegiftOrder.Status.values());
        model.addAttribute("APPLY_REFUND", CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
        model.addAttribute("REFUND_SUCCESS", CustomerForegiftOrder.Status.REFUND_SUCCESS.getValue());
    }

    @RequestMapping(value = "view_foregift_order.htm")
    public String viewForegiftOrder(Model model, long customerId) {
        model.addAttribute("customerId", customerId);
        return "/security/basic/customer/view_foregift_order";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "select_foregift_order.htm")
    public void selectForegiftOrder(Model model) {
        model.addAttribute("payOk", CustomerForegiftOrder.Status.PAY_OK.getValue());
    }

    @ResponseBody
    @RequestMapping("find_foregift.htm")
    @ViewModel(ViewModel.JSON)
    public ExtResult findForegift(String qrCode) {
        Pattern pattern = Pattern.compile("(CF\\d{16})");
        Matcher matcher = pattern.matcher(qrCode);
        String orderId = null;
        while (matcher.find()) {
            orderId = matcher.group(1);
        }
        CustomerForegiftOrder customerForegiftOrder = customerForegiftOrderService.find(orderId);
        if (customerForegiftOrder == null) {
            return DataResult.failResult("请确认该订单是否存在！");
        } else {
            return DataResult.successResult(customerForegiftOrder);
        }
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CustomerForegiftOrder search) {
        search.setVehicleOrderFlag(ConstEnum.Flag.FALSE.getValue());
        if (search.getStatus() == null) {
            search.setDefaultQueryStatus(CustomerForegiftOrder.Status.WAIT_PAY.getValue());
        } else if (search.getStatus().equals(0)) {
            search.setStatus(null);
        }
        return PageResult.successResult(customerForegiftOrderService.findPage(search));
    }

    @RequestMapping(value = "alipay_pay_order.htm")
    public void alipayPayOrder(Model model, String id) {
        model.addAttribute("sourceId", id);
    }

    @RequestMapping("alipay_pay_order_page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult alipayPayOrderPage(AlipayPayOrder search) {
        search.setSourceType(AlipayPayOrder.SourceType.FOREGIFT_ORDER_CUSTOMER_PAY.getValue());
        search.setOrderStatus(AlipayPayOrder.Status.SUCCESS.getValue());
        return PageResult.successResult(alipayPayOrderService.findPage(search));
    }

    @RequestMapping(value = "weixin_pay_order.htm")
    public void weixinPayOrder(Model model, String id) {
        model.addAttribute("sourceId", id);

    }

    @RequestMapping("weixin_pay_order_page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult weixinPayOrderPage(WeixinPayOrder search) {
        search.setSourceType(WeixinPayOrder.SourceType.FOREGIFT_ORDER_CUSTOMER_PAY.getValue());
        search.setOrderStatus(WeixinPayOrder.Status.SUCCESS.getValue());
        return PageResult.successResult(weixinPayOrderService.findPage(search));
    }

    @RequestMapping("exchange_installment.htm")
    public void exchangeInstallment(Model model, String id) {
        model.addAttribute("sourceId", id);
    }

    @RequestMapping("exchange_installment_page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult exchangeInstallmentPage(CustomerInstallmentRecordPayDetail search) {
        CustomerInstallmentRecordOrderDetail customerInstallmentRecordOrderDetail = customerInstallmentRecordOrderDetailService.findOrderBySourceId(search.getSourceId(), ConstEnum.Category.EXCHANGE.getValue());
        if (customerInstallmentRecordOrderDetail != null) {
            search.setRecordId(customerInstallmentRecordOrderDetail.getRecordId());
            return PageResult.successResult(customerInstallmentRecordPayDetailService.findPage(search));
        }else{
            search.setRecordId(-1L);
            return PageResult.successResult(customerInstallmentRecordPayDetailService.findPage(search));
        }
    }

    @RequestMapping(value = "weixin_pay_order_refund.htm")
    public void weixinPayOrderRefund(Model model, String id) {
        model.addAttribute("sourceId", id);
    }

    @RequestMapping("weixin_pay_order_page_refund.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult weixinPayOrderPageRefund(WeixinPayOrder search) {
        search.setSourceType(WeixinPayOrder.SourceType.FOREGIFT_ORDER_CUSTOMER_PAY.getValue());
        search.setOrderStatus(WeixinPayOrder.Status.SUCCESS.getValue());
        return PageResult.successResult(weixinPayOrderService.findPage(search));
    }

    @RequestMapping(value = "alipay_pay_order_refund.htm")
    public void alipayPayOrderRefund(Model model, String id) {
        model.addAttribute("sourceId", id);
    }

    @RequestMapping("alipay_pay_order_page_refund.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult alipayPayOrderPageRefund(AlipayPayOrder search) {
        search.setSourceType(AlipayPayOrder.SourceType.FOREGIFT_ORDER_CUSTOMER_PAY.getValue());
        search.setOrderStatus(AlipayPayOrder.Status.SUCCESS.getValue());
        return PageResult.successResult(alipayPayOrderService.findPage(search));
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, String id) {
        CustomerForegiftOrder entity = customerForegiftOrderService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        model.addAttribute("id", id);
        return "/security/basic/customer_foregift_order/view";
    }

    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, String id) {
        CustomerForegiftOrder entity = customerForegiftOrderService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
            model.addAttribute("staticUrl", appConfig.getStaticUrl());
            model.addAttribute("payTypeEnum", ConstEnum.PayType.values());
            model.addAttribute("statusEnum", CustomerForegiftOrder.Status.values());
        }
        return "/security/basic/customer_foregift_order/view_basic";
    }

    @RequestMapping(value = "edit_refund.htm")
    public String editRefund(Model model, String id,int refundFlag) {
        CustomerForegiftOrder entity = customerForegiftOrderService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            if(entity.getRefundMoney()==null){
                entity.setRefundMoney(0);
            }
            model.addAttribute("refundFlag", refundFlag);
            model.addAttribute("entity", entity);
            model.addAttribute("APPLY_ALIPAY", ConstEnum.PayType.ALIPAY.getValue());
            model.addAttribute("APPLY_WEIXIN", ConstEnum.PayType.WEIXIN.getValue());
        }
        return "/security/basic/customer_foregift_order/edit_refund";
    }


    @RequestMapping(value = "edit_repulse_refund.htm")
    public String editRepulseRefund(Model model, String id) {
        CustomerForegiftOrder entity = customerForegiftOrderService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/basic/customer_foregift_order/edit_repulse_refund";
    }

    @RequestMapping("repulse_refund.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult repulseRefund(CustomerForegiftOrder customerForegiftOrder) {
        return customerForegiftOrderService.repulseRefund(customerForegiftOrder);
    }

    @RequestMapping(value = "find_by_customer_id.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult findByCustomerId(long customerId) {
        return customerForegiftOrderService.findByCustomerId(customerId);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "photo_path.htm", method = RequestMethod.GET)
    public void portrait() {
    }

    @RequestMapping(value = "photo_path.htm", method = RequestMethod.POST)
    public String portrait(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        String uuid = IdUtils.uuid();
        String fileSuffix = YhdgUtils.getFileSuffix(file.getOriginalFilename());
        File sourceFile = new File(getAppConfig().tempDir, uuid + "." + fileSuffix);
        YhdgUtils.makeParentDir(sourceFile);
        file.transferTo(sourceFile);
        File targetFile = cutDown(sourceFile, IdUtils.uuid(), fileSuffix, Constant.MAX_IMAGE_WIDTH);
        sourceFile.delete();
        String url = getAppConfig().staticUrl;
        HttpUtils.HttpResp httpResp = HttpUtils.uploadFile(url + UPLOAD_FILE_URL, targetFile, Collections.EMPTY_MAP); //upload to static server
        if (httpResp.status / 100 == 2) {
            Map map = (Map) YhdgUtils.decodeJson(httpResp.content, Map.class);
            List list = (List) map.get("data");
            Map<String, String> data = (Map<String, String>) list.get(0);
            model.addAttribute("success", true);
            model.addAttribute("filePath", data.get("filePath"));
            model.addAttribute("fileName", data.get("fileName"));
        } else {
            model.addAttribute("success", false);
            model.addAttribute("message", "上传文件出现错误");
        }

        return "/security/basic/customer_foregift_order/photo_path_response";
    }

    @RequestMapping("vehiclePage.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult vehiclePage(CustomerForegiftOrder search) {
        search.setVehicleOrderFlag(ConstEnum.Flag.TRUE.getValue());
        if (search.getStatus() == null) {
            search.setDefaultQueryStatus(CustomerForegiftOrder.Status.WAIT_PAY.getValue());
        } else if (search.getStatus().equals(0)) {
            search.setStatus(null);
        }
        return PageResult.successResult(customerForegiftOrderService.findPage(search));
    }

}
