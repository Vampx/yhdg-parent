package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.AgentForegiftDepositOrder;
import cn.com.yusong.yhdg.common.domain.basic.AgentInOutMoney;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.ExportXlsUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.agentserver.constant.AppConstant;
import cn.com.yusong.yhdg.agentserver.service.basic.AgentForegiftDepositOrderService;
import cn.com.yusong.yhdg.agentserver.service.basic.AgentInOutMoneyService;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/security/basic/agent_foregift_deposit_order")
public class AgentForegiftDepositOrderController extends SecurityController {
    static Logger log = LoggerFactory.getLogger(AgentForegiftDepositOrderController.class);

    @Autowired
    AgentForegiftDepositOrderService agentForegiftDepositOrderService;


    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(AgentForegiftDepositOrder search) {
        return PageResult.successResult(agentForegiftDepositOrderService.findPage(search));
    }

}
