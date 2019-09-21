package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.BalanceTransferOrder;
import cn.com.yusong.yhdg.common.domain.basic.DayBalanceRecord;
import cn.com.yusong.yhdg.common.domain.basic.OrderId;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.ChargerUtils;
import cn.com.yusong.yhdg.common.utils.HttpUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.entity.SessionUser;
import cn.com.yusong.yhdg.webserver.service.basic.AgentService;
import cn.com.yusong.yhdg.webserver.service.basic.BalanceTransferOrderService;
import cn.com.yusong.yhdg.webserver.service.basic.OrderIdService;
import org.apache.commons.lang.StringUtils;
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

@Controller
@RequestMapping(value = "/security/basic/balance_transfer_order")
public class BalanceTransferOrderController extends SecurityController {
    final static String UPLOAD_FILE_URL = "/security/upload/attachment.htm?type=" + ConstEnum.AttachmentType.BALANCE_TRANSFER_ORDER_PHOTO_PATH.getValue();

    @Autowired
    BalanceTransferOrderService balanceTransferOrderService;
    @Autowired
    AgentService agentService;
    @Autowired
    OrderIdService orderIdService;

    @SecurityControl(limits = OperCodeConst.CODE_5_1_2_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        List<Agent> agentList = agentService.findAll();
        model.addAttribute("StatusEnum", BalanceTransferOrder.Status.values());
        model.addAttribute("BizTypeEnum", DayBalanceRecord.BizType.values());
        model.addAttribute("agentList", agentList);
        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_05_01_02.getValue());
    }


    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(BalanceTransferOrder search) {
        return PageResult.successResult(balanceTransferOrderService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "image_path.htm", method = RequestMethod.GET)
    public void portrait() {
    }

    @RequestMapping(value = "image_path.htm", method = RequestMethod.POST)
    public String portrait(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        String uuid = IdUtils.uuid();
        String fileSuffix = ChargerUtils.getFileSuffix(file.getOriginalFilename());
        File sourceFile = new File(getAppConfig().tempDir, uuid + "." + fileSuffix);
        ChargerUtils.makeParentDir(sourceFile);
        file.transferTo(sourceFile);
        File targetFile = cutDown(sourceFile, IdUtils.uuid(), fileSuffix, Constant.MAX_IMAGE_WIDTH);
        sourceFile.delete();
        String url = getAppConfig().staticUrl;
        HttpUtils.HttpResp httpResp = HttpUtils.uploadFile(url + UPLOAD_FILE_URL, targetFile, Collections.EMPTY_MAP); //upload to static server
        if(httpResp.status / 100 == 2) {
            Map map = (Map) ChargerUtils.decodeJson(httpResp.content, Map.class);
            List list = (List) map.get("data");
            Map<String, String> data = (Map<String, String>)list.get(0);
            model.addAttribute("success", true);
            model.addAttribute("filePath", data.get("filePath"));
            model.addAttribute("fileName", data.get("fileName"));
        } else {
            model.addAttribute("success", false);
            model.addAttribute("message", "上传文件出现错误");
        }

        return "/security/basic/balance_transfer_order/image_path_response";
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
        model.addAttribute("StatusEnum", DayBalanceRecord.Status.values());
        model.addAttribute("status", DayBalanceRecord.Status.CONFIRM_OK_BY_OFFLINE.getValue());
        model.addAttribute("BizTypeEnum", DayBalanceRecord.BizType.values());
        model.addAttribute("OrderTypeEnum", BalanceTransferOrder.OrderType.values());
        model.addAttribute("staticUrl", appConfig.getStaticUrl());
    }

    @RequestMapping(value = "create.htm")
    @ResponseBody
    @ViewModel(ViewModel.JSON)
    public ExtResult create(BalanceTransferOrder balanceTransferOrder, Long ids[], String transferImagePath, HttpSession httpSession){
        String username = getSessionUser(httpSession).getUsername();
        balanceTransferOrder.setTransferImagePath(transferImagePath);
        balanceTransferOrder.setHandleUser(username);
        balanceTransferOrder.setId(orderIdService.newOrderId(OrderId.OrderIdType.BALANCE_TRANSFER_ORDER));
        ExtResult result = balanceTransferOrderService.insert(balanceTransferOrder, ids);
        if (result.isSuccess()) {
            String title = "创建结算记录";
            String content = "创建运营商：" + balanceTransferOrder.getAgentName() +" 的结算记录！";
        }
        return result;
    }


    @RequestMapping(value = "view.htm")
    public String view(Model model, String id) {
        BalanceTransferOrder entity = balanceTransferOrderService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
            model.addAttribute("StatusEnum", BalanceTransferOrder.Status.values());
            model.addAttribute("BizTypeEnum", DayBalanceRecord.BizType.values());
        }
        return "/security/basic/balance_transfer_order/view";
    }

    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, String id) {
        BalanceTransferOrder entity = balanceTransferOrderService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
            model.addAttribute("StatusEnum", BalanceTransferOrder.Status.values());
            model.addAttribute("BizTypeEnum", DayBalanceRecord.BizType.values());
            model.addAttribute("staticUrl", appConfig.getStaticUrl());
        }
        return "/security/basic/balance_transfer_order/view_basic";
    }

    @RequestMapping(value = "view_record.htm")
    public String viewDayBalanceRecord(Model model, String id) {
        BalanceTransferOrder entity = balanceTransferOrderService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
            model.addAttribute("StatusEnum", DayBalanceRecord.Status.values());
            model.addAttribute("BizTypeEnum", DayBalanceRecord.BizType.values());
        }
        return "/security/basic/balance_transfer_order/view_record";
    }

    @RequestMapping(value = "view_log.htm")
    public String viewLog(Model model, String id) {
        BalanceTransferOrder entity = balanceTransferOrderService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/basic/balance_transfer_order/view_log";
    }

    @RequestMapping(value = "reset.htm", method = RequestMethod.GET)
    public String reset(Model model, String id, HttpSession httpSession) {
        BalanceTransferOrder order = balanceTransferOrderService.find(id);
        if(order == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        SessionUser sessionUser = getSessionUser(httpSession);
        model.addAttribute("operatorName", StringUtils.isEmpty(sessionUser.getRealName()) ? sessionUser.getUsername() : sessionUser.getRealName());
        model.addAttribute("order", order);
        return "/security/basic/balance_transfer_order/reset";
    }

    @ResponseBody
    @RequestMapping(value = "reset.htm", method = RequestMethod.POST)
    public ExtResult reset(String id, String openId, String fullName, HttpSession httpSession) {
        SessionUser sessionUser = getSessionUser(httpSession);
        String operatorName = StringUtils.isEmpty(sessionUser.getRealName()) ? sessionUser.getUsername() : sessionUser.getRealName();
        String title = "重置";
        String content = "重置id:" + id + "的结算转账记录！";
        return balanceTransferOrderService.reset(id, openId, fullName, operatorName);
    }
}
