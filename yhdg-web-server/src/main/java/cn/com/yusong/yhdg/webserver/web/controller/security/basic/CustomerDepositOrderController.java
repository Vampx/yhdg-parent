package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.AlipayPayOrder;
import cn.com.yusong.yhdg.common.domain.basic.CustomerDepositOrder;
import cn.com.yusong.yhdg.common.domain.basic.OrderId;
import cn.com.yusong.yhdg.common.domain.basic.WeixinPayOrder;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.HttpUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.basic.AlipayPayOrderService;
import cn.com.yusong.yhdg.webserver.service.basic.CustomerDepositOrderService;
import cn.com.yusong.yhdg.webserver.service.basic.OrderIdService;
import cn.com.yusong.yhdg.webserver.service.basic.WeixinPayOrderService;
import com.alipay.api.AlipayApiException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.util.*;

/**
 * Created by chen on 2017/5/20.
 */
@Controller
@RequestMapping(value = "/security/basic/customer_deposit_order")
public class CustomerDepositOrderController extends SecurityController {
    private static final Logger log = LogManager.getLogger(CustomerController.class);

    final static String UPLOAD_FILE_URL = "/security/upload/attachment.htm?type=" + ConstEnum.AttachmentType.CUSTOMER_DEPOSIT_ORDER_REFUND_PHOTO_PATH.getValue();

    @Autowired
    CustomerDepositOrderService customerDepositOrderService;
    @Autowired
    OrderIdService orderIdService;
    @Autowired
    AlipayPayOrderService alipayPayOrderService;
    @Autowired
    WeixinPayOrderService weixinPayOrderService;

    @SecurityControl(limits = "basic.CustomerDepositOrder:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "basic.CustomerDepositOrder:list");
        model.addAttribute("statusEnum", CustomerDepositOrder.Status.values());
        List<ConstEnum.PayType> payTypes = new ArrayList< ConstEnum.PayType>();
        payTypes.add(ConstEnum.PayType.ALIPAY);
        payTypes.add(ConstEnum.PayType.WEIXIN);
        model.addAttribute("payTypeEnum", payTypes);
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CustomerDepositOrder customerDepositOrder) {
        return PageResult.successResult(customerDepositOrderService.findPage(customerDepositOrder));
    }

    @RequestMapping(value = "alipay_pay_order.htm")
    public void alipayPayOrder(Model model,String id) {
        model.addAttribute("sourceId", id);
    }

    @RequestMapping("alipay_pay_order_page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult alipayPayOrderPage(AlipayPayOrder search) {
        search.setSourceType(AlipayPayOrder.SourceType.DEPOSIT_ORDER_CUSTOMER_PAY.getValue());
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
        search.setSourceType(WeixinPayOrder.SourceType.DEPOSIT_ORDER_CUSTOMER_PAY.getValue());
        search.setOrderStatus(WeixinPayOrder.Status.SUCCESS.getValue());
        return PageResult.successResult(weixinPayOrderService.findPage(search));
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
        model.addAttribute("payTypeEnum", ConstEnum.PayType.values());
        model.addAttribute("statusEnum", CustomerDepositOrder.Status.values());
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(CustomerDepositOrder entity) {
        entity.setId(orderIdService.newOrderId(OrderId.OrderIdType.CUSTOMER_DEPOSIT_ORDER));
        return customerDepositOrderService.create(entity);
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, String id) {
        CustomerDepositOrder entity = customerDepositOrderService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
            model.addAttribute("id", id);
        }
        return "/security/basic/customer_deposit_order/view";
    }

    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, String id) {
        CustomerDepositOrder entity = customerDepositOrderService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
            model.addAttribute("staticUrl", appConfig.getStaticUrl());
            model.addAttribute("customerId",entity.getCustomerId());
            model.addAttribute("payTypeEnum", ConstEnum.PayType.values());
            model.addAttribute("statusEnum", CustomerDepositOrder.Status.values());
        }
        return "/security/basic/customer_deposit_order/view_basic";
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, String id) {
        CustomerDepositOrder entity = customerDepositOrderService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/basic/customer_deposit_order/edit";
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
        if (httpResp.content != null) {
            if (log.isDebugEnabled()) {
                log.debug("content: {}", httpResp.content);
            }
        }
        if(httpResp.status / 100 == 2) {
            Map map = (Map) YhdgUtils.decodeJson(httpResp.content, Map.class);
            List list = (List) map.get("data");
            Map<String, String> data = (Map<String, String>)list.get(0);
            model.addAttribute("success", true);
            model.addAttribute("filePath", data.get("filePath"));
            model.addAttribute("fileName", data.get("fileName"));
        } else {
            model.addAttribute("success", false);
            model.addAttribute("message", "上传文件出现错误");
        }

        return "/security/basic/customer_deposit_order/photo_path_response";
    }

    @RequestMapping(value = "refund.htm")
    public String refund(Model model, String id) {
        CustomerDepositOrder entity = customerDepositOrderService.find(id);
        model.addAttribute("id", id);
      //  model.addAttribute("refundMoney", ((entity.getMoney() == null ? 0 : entity.getMoney()) + (entity.getGift() == null ? 0 : entity.getGift())));
        model.addAttribute("refundMoney", ((entity.getMoney() == null ? 0 : entity.getMoney())));
        return "/security/basic/customer_deposit_order/refund";
    }

    @RequestMapping("confirm_refund.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult confirmRefund(String id, float refundMoney, String photoPath, String refundReason, HttpSession session) throws AlipayApiException {
        return customerDepositOrderService.confirmRefund (id, refundMoney, photoPath, getSessionUser(session).getUsername(), refundReason);
    }


}
