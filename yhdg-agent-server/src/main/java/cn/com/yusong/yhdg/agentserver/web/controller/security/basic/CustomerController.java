
package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.agentserver.entity.SessionUser;
import cn.com.yusong.yhdg.agentserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.agentserver.service.basic.DictItemService;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.HttpUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import jxl.read.biff.BiffException;
import org.apache.commons.io.IOUtils;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/security/basic/customer")
public class CustomerController extends SecurityController {
    private static final Logger log = LogManager.getLogger(CustomerController.class);

    final static String UPLOAD_FILE_URL = "/security/upload/attachment.htm?type=2";

    @Autowired
    CustomerService customerService;
    @Autowired
    DictItemService dictItemService;

    @SecurityControl(limits = "basic.AgentUser:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "basic.AgentUser:list");
        model.addAttribute("registerTypeEnum", Customer.RegisterType.values());
//        model.addAttribute("typeEnum", Customer.Type.values());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(Customer search) {
        return PageResult.successResult(customerService.findPage(search));
    }

    @RequestMapping("payee_page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult payeePage(Customer search) {
        return PageResult.successResult(customerService.findPayeePage(search));
    }

    @RequestMapping("whitelist_customer_page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult whitelistCustomerPage(Customer search) {
        return PageResult.successResult(customerService.findWhitelistCustomerPage(search));
    }

    @RequestMapping("page_by_bind_time.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult pageByBindTime(Customer search) {
        return PageResult.successResult(customerService.findPageByBindTime(search));
    }

    @RequestMapping("pages.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult pages(Customer search) {
        return PageResult.successResult(customerService.findPages(search));
    }


    @RequestMapping(value = "add.htm")
    public void add(Model model) {
        model.addAttribute("registerTypeEnum", Customer.RegisterType.values());
     //   model.addAttribute("batteryTypeList", dictItemService.findByCategory(DictCategory.CategoryType.BATTERY_TYPE.getValue()));
    //    model.addAttribute("companyList", dictItemService.findByCategory(DictCategory.CategoryType.CUSTOMER_DEPT.getValue()));
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(Customer entity) throws IOException {
        return customerService.create(entity);
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, int id) {
        Customer entity = customerService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("staticUrl", appConfig.getStaticUrl());
            model.addAttribute("entity", entity);
        }
        model.addAttribute("registerTypeEnum", Customer.RegisterType.values());
       // model.addAttribute("batteryTypeList", dictItemService.findByCategory(DictCategory.CategoryType.BATTERY_TYPE.getValue()));
       // model.addAttribute("companyList", dictItemService.findByCategory(DictCategory.CategoryType.CUSTOMER_DEPT.getValue()));
        return "/security/basic/customer/edit";
    }

    @RequestMapping(value = "edit_refund.htm")
    public String editRefund(Model model, int id) {
        Customer entity = customerService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/basic/customer/edit_refund";
    }

    @RequestMapping("refund.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult refund(Customer entity, String handleName, String memo) throws IOException {
        Customer customer = customerService.find(entity.getId());
        if (customer.getBalance() <= 0) {
            return ExtResult.failResult("会员余额小于等于0不能扣款");
        }
        if (customer.getBalance() < entity.getBalance()) {
            return ExtResult.failResult("扣款金额不能大于账户余额");
        }
        return customerService.refund(entity, customer.getGiftBalance(), handleName, memo);
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

        return "/security/basic/customer/photo_path_response";
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(Customer entity) throws IOException {
        return customerService.update(entity);
    }

    @RequestMapping("resignation.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult resignation(HttpSession httpSession, Long id) {
        SessionUser sessionUser = getSessionUser(httpSession);
        return customerService.resignation(id, sessionUser.getUsername());
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(int id) {
        return customerService.delete(id);
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "unique.htm")
    public ExtResult unique(String mobile) {
        boolean unique = customerService.findUnique(mobile);
        if (unique) {
            return ExtResult.successResult();
        } else {
            return ExtResult.failResult("手机号重复");
        }
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, int id) {
        Customer entity = customerService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("staticUrl", appConfig.getStaticUrl());
            model.addAttribute("entity", entity);
        }
        model.addAttribute("id", id);
        return "/security/basic/customer/view";
    }

    @RequestMapping(value = "view_basic_info.htm")
    public String viewBasicInfo(Model model, int id) {
        Customer entity = customerService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("staticUrl", appConfig.getStaticUrl());
            model.addAttribute("entity", entity);
        }
        model.addAttribute("registerTypeEnum", Customer.RegisterType.values());
        model.addAttribute("pushTypeEnum", ConstEnum.PushType.values());
     //   model.addAttribute("batteryTypeList", dictItemService.findByCategory(DictCategory.CategoryType.BATTERY_TYPE.getValue()));
     //   model.addAttribute("companyList", dictItemService.findByCategory(DictCategory.CategoryType.CUSTOMER_DEPT.getValue()));
        model.addAttribute("id", id);
        return "/security/basic/customer/view_basic_info";
    }

    @RequestMapping(value = "view_id_card.htm")
    public String viewIdCard(Model model, int id) {
        Customer entity = customerService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("staticUrl", appConfig.getStaticUrl());
            model.addAttribute("entity", entity);
        }
        return "/security/basic/customer/view_id_card";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "select_customer.htm")
    public void selectCustomer(Model model, Integer agentId) {
        model.addAttribute("agentId", agentId);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "select_payee_customer.htm")
    public void selectPayeeCustomer(Model model) {
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "select_whitelist_customer.htm")
    public void selectWhitelistCustomer(Model model, Integer agentId, String mobile) {
        model.addAttribute("agentId", agentId);
        model.addAttribute("mobile", mobile);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "select_coupon_ticket_customers.htm")
    public void selectCouponTicketCustomers() {
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "batch_remove.htm")
    public ExtResult batchRemove(long[] id) {
        return customerService.batchRemove(id);
    }

    @RequestMapping("fw_unbind_mobile.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult fwUnbindMobile(Long[] idList) {
        int count = 0;
        for (Long e : idList) {
            Customer customer = customerService.find(e);
            if (customer.getFwOpenId() != null) {
                count += customerService.fwUnbindMobile(e, customer.getFwOpenId());
            }
        }
        return ExtResult.successResult(String.format("成功解绑%d个生活号", count));
    }

    @RequestMapping("mp_unbind_mobile.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult mpUnbindMobile(Long[] idList) {
        int count = 0;
        for (Long e : idList) {
            Customer customer = customerService.find(e);
            if (customer.getMpOpenId() != null) {
                count += customerService.mpUnbindMobile(e, customer.getMpOpenId());
            }
        }
        return ExtResult.successResult(String.format("成功解绑%d个公众号", count));
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "batch_active.htm")
    public ExtResult batchActive(long[] id) {
        return customerService.batchActive(id);
    }

    @RequestMapping(value = "btch_import_customer.htm", method = RequestMethod.POST)
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult btchImportCustomer(@RequestParam("file") MultipartFile file, Integer company, Integer batteryType) throws IOException, BiffException {
        if (company == null || batteryType == null) {
            return ExtResult.failResult("请选择所属公司和电池类型！");
        }
        String outPath = String.format("%s/%s", getAppConfig().appDir.getPath(), file.getOriginalFilename());
        File outFile = new File(outPath);
        if (!outFile.exists()) {
            outFile.getParentFile().mkdirs();
        }

        FileOutputStream outputStream = new FileOutputStream(outFile);
        int result = IOUtils.copy(file.getInputStream(), outputStream);
        if (result <= 0) {
            return ExtResult.failResult("操作失败！");
        }
        File mFile = new File(outPath);
        return customerService.batchImportCustomer(mFile, company, batteryType);
    }

    @RequestMapping("upload_file.htm")
    @ViewModel(ViewModel.INNER_PAGE)
    public void uploadFile(Model model) {
   //     model.addAttribute("batteryTypeList", dictItemService.findByCategory(DictCategory.CategoryType.BATTERY_TYPE.getValue()));
   //     model.addAttribute("companyList", dictItemService.findByCategory(DictCategory.CategoryType.CUSTOMER_DEPT.getValue()));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "select_customers_mobile.htm")
    public void selectCustomersMobile(Model model) {

    }

    @RequestMapping("app_unbind_mobile.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult appUnbindMobile(Long[] idList) {
        return customerService.appUnbindMobile(idList);
    }

}
