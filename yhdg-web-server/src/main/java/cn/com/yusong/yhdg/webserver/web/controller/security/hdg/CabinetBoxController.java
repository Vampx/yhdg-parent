package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetBox;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetOperateLog;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.protocol.msg22.Msg222000004;
import cn.com.yusong.yhdg.webserver.biz.client.ClientBizUtils;
import cn.com.yusong.yhdg.webserver.constant.RespCode;
import cn.com.yusong.yhdg.webserver.entity.SessionUser;
import cn.com.yusong.yhdg.webserver.entity.result.RestResult;
import cn.com.yusong.yhdg.webserver.service.basic.DictItemService;
import cn.com.yusong.yhdg.webserver.service.hdg.BatteryService;
import cn.com.yusong.yhdg.webserver.service.hdg.CabinetBoxService;
import cn.com.yusong.yhdg.webserver.service.hdg.CabinetOperateLogService;
import cn.com.yusong.yhdg.webserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * 换电柜 格子
 */
@Controller
@RequestMapping(value = "/security/hdg/cabinet_box")
public class CabinetBoxController extends SecurityController {

    @Autowired
    CabinetBoxService cabinetBoxService;
    @Autowired
    DictItemService dictItemService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    BatteryService batteryService;
    @Autowired
    CabinetOperateLogService cabinetOperateLogService;

    @RequestMapping(value = "index.htm")
    public void index(String cabinetId, Integer editFlag, Model model) {
        model.addAttribute("cabinetId", cabinetId);
        model.addAttribute("editFlag", editFlag);
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CabinetBox cabinetBox) {
        return PageResult.successResult(cabinetBoxService.findPage(cabinetBox));
    }

    @RequestMapping("view.htm")
    @ViewModel(ViewModel.INNER_PAGE)
    public String view(String cabinetId, String boxNum, Model model) {
        CabinetBox cabinetBox = cabinetBoxService.find(cabinetId, boxNum);
        if (cabinetBox == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", cabinetBox);
       // model.addAttribute("TypeEnum", dictItemService.findByCategory(DictCategory.CategoryType.BATTERY_TYPE.getValue()));
        return "/security/hdg/cabinet_box/view";
    }

    @RequestMapping("add.htm")
    @ViewModel(ViewModel.INNER_PAGE)
    public void add(String cabinetId, Model model) {
        model.addAttribute("cabinetId", cabinetId);
       // model.addAttribute("TypeEnum", dictItemService.findByCategory(DictCategory.CategoryType.BATTERY_TYPE.getValue()));
    }

    @RequestMapping("batch_add.htm")
    @ViewModel(ViewModel.INNER_PAGE)
    public void batch_add(String cabinetId, Model model) {
        model.addAttribute("cabinetId", cabinetId);
       // model.addAttribute("TypeEnum", dictItemService.findByCategory(DictCategory.CategoryType.BATTERY_TYPE.getValue()));
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(CabinetBox cabinetBox) {
        return cabinetBoxService.insert(cabinetBox);
    }

    /**
     * 批量创建
     *
     * @param cabinetId
     * @param batchBox
     * @return
     */
    @RequestMapping("batch_create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult batchCreate(String cabinetId, String batchBox) {

        return cabinetBoxService.batchInsert(cabinetId, batchBox);
    }

    @RequestMapping("edit.htm")
    @ViewModel(ViewModel.INNER_PAGE)
    public String edit(String cabinetId, String boxNum, Model model) {
        CabinetBox cabinetBox = cabinetBoxService.find(cabinetId, boxNum);
        if (cabinetBox == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        if (cabinetBox.getBoxStatus() != CabinetBox.BoxStatus.EMPTY.getValue()) {
            model.addAttribute("message", "箱子已经使用，不能修改");
            return SEGMENT_MESSAGE;
        }

        model.addAttribute("entity", cabinetBox);
       // model.addAttribute("TypeEnum", dictItemService.findByCategory(DictCategory.CategoryType.BATTERY_TYPE.getValue()));
        return "/security/hdg/cabinet_box/edit";
    }

    @RequestMapping("box_state_info.htm")
    @ViewModel(ViewModel.INNER_PAGE)
    public String boxStateInfo(String cabinetId, String boxNum, Model model) {
        CabinetBox cabinetBox = cabinetBoxService.find(cabinetId, boxNum);
        if (cabinetBox == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        String boxStateName = cabinetBox.getBoxStateName();
        if (StringUtils.isNotEmpty(boxStateName)) {
            String[] stateNames = boxStateName.split(",");
            for (int i = 0; i < stateNames.length; i++) {
                if (i == 0) {
                    model.addAttribute("doorState", stateNames[0]);
                } else if (i == 1) {
                    model.addAttribute("smokeAlarm", stateNames[1]);
                } else if (i == 2) {
                    model.addAttribute("socketPower", stateNames[2]);
                } else if (i == 3) {
                    model.addAttribute("boxFan", stateNames[3]);
                } else if (i == 4) {
                    model.addAttribute("boxCommu", stateNames[4]);
                } else if (i == 5) {
                    model.addAttribute("chargeControl", stateNames[5]);
                } else if (i == 5) {
                    model.addAttribute("boxNFC", stateNames[6]);
                }
            }
        }
        return "/security/hdg/cabinet_box/box_state_info";
    }

    @RequestMapping(value = "edit_up_line_status.htm")
    public void editUpLineStatus(Model model) {
    }

    @RequestMapping("update_up_line_status.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateUpLineStatus(Battery entity) {
        return batteryService.boxUpdateUpLineStatus(entity);
    }

    @RequestMapping("forbidden.htm")
    @ViewModel(ViewModel.INNER_PAGE)
    public String forbidden(Model model) {
        return "/security/hdg/cabinet_box/do_un_active";
    }

    @RequestMapping("abnormal.htm")
    @ViewModel(ViewModel.INNER_PAGE)
    public String abnormal(Model model) {
        return "/security/hdg/cabinet_box/abnormal";
    }

    @RequestMapping("update")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(CabinetBox entity) {
        return cabinetBoxService.update(entity);
    }

    @RequestMapping("updateIsActive")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateIsActive(CabinetBox entity,HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        SessionUser sessionUser = null;
        if (httpSession != null) {
            sessionUser = (SessionUser) httpSession.getAttribute(Constant.SESSION_KEY_USER);
        }
        String userName = "";
        User user = userService.find(sessionUser.getId());
        if (user != null) {
            userName = user.getFullname();
        }
        if (StringUtils.isEmpty(userName)) {
            userName = sessionUser.getUsername();
        }
        entity.setOperator(userName);
        entity.setOperatorTime(new Date());
        return cabinetBoxService.updateIsActive(entity, sessionUser.getUsername());
    }

    @RequestMapping("update_is_normal")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateIsNormal(Battery entity,HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        SessionUser sessionUser = null;
        if (httpSession != null) {
            sessionUser = (SessionUser) httpSession.getAttribute(Constant.SESSION_KEY_USER);
        }
        String userName = "";
        User user = userService.find(sessionUser.getId());
        if (user != null) {
            userName = user.getFullname();
        }
        if (StringUtils.isEmpty(userName)) {
            userName = sessionUser.getUsername();
        }
        entity.setOperator(userName);
        entity.setOperatorTime(new Date());
        return batteryService.updateIsNormal(entity);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(String cabinetId, String boxNum) {
        return cabinetBoxService.delete(cabinetId, boxNum);
    }

    @RequestMapping("open_box.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public RestResult openBox(String cabinetId, String boxNum,HttpServletRequest request) throws InterruptedException {
        CabinetBox subcabinetBox = cabinetBoxService.find(cabinetId, boxNum);
        if (subcabinetBox == null) {
            return RestResult.dataResult(RespCode.CODE_0.getValue(), "箱号不存在", null);
        }

        ClientBizUtils.SerialResult result = ClientBizUtils.openStandardBox(appConfig, cabinetId, boxNum, subcabinetBox.getSubtype());
        boolean ok = result.getCode() == RespCode.CODE_0.getValue();HttpSession httpSession = request.getSession();
        SessionUser sessionUser = null;
        if (httpSession != null) {
            sessionUser = (SessionUser) httpSession.getAttribute(Constant.SESSION_KEY_USER);
        }

        if (StringUtils.isNotEmpty(subcabinetBox.getCabinetId())) {
            Cabinet cabinet = cabinetService.find(subcabinetBox.getCabinetId());
            CabinetOperateLog operateLog = new CabinetOperateLog();
            operateLog.setAgentId(cabinet.getAgentId());
            operateLog.setCabinetId(cabinet.getId());
            operateLog.setCabinetName(cabinet.getCabinetName());
            operateLog.setBoxNum(boxNum);
            operateLog.setOperateType(CabinetOperateLog.OperateType.OPEN_DOOR.getValue());
            operateLog.setOperatorType(CabinetOperateLog.OperatorType.PLATFORM.getValue());
            operateLog.setOperator(sessionUser.getUsername());


            if (ok) {
                Msg222000004 resp = (Msg222000004) result.getData();
                operateLog.setContent("远程开箱成功, 箱门状态: " + (resp.boxStatus == 0 ? "开门" : "关门"));
                operateLog.setCreateTime(new Date());
                cabinetOperateLogService.insert(operateLog);
            } else {
                if (result.getSerial() != -1) {
                    operateLog.setContent("远程开箱失败, " + result.getMessage());
                    operateLog.setCreateTime(new Date());
                    cabinetOperateLogService.insert(operateLog);
                }
                return result;
            }
        }

        if (ok) {
            return RestResult.dataResult(RespCode.CODE_0.getValue(), "开箱成功", result.getData());
        } else {
            return result;
        }

    }

    @RequestMapping("cause.htm")
    @ViewModel(ViewModel.INNER_PAGE)
    public String forbiddenCause(String cabinetId, String boxNum, Model model) {
        CabinetBox cabinetBox = cabinetBoxService.find(cabinetId, boxNum);
        if (cabinetBox == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", cabinetBox);
        return "/security/hdg/cabinet_box/forbidden_cause";
    }

    @RequestMapping("abnormal_cause.htm")
    @ViewModel(ViewModel.INNER_PAGE)
    public String abnormalCause(String batteryId, Model model) {
        Battery battery= batteryService.find(batteryId);
        if (battery == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", battery);
        return "/security/hdg/cabinet_box/abnormal_cause";
    }

}
