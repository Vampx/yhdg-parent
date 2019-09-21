package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.hdg;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.AgentService;
import cn.com.yusong.yhdg.agentappserver.service.basic.UserService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.CabinetBatteryTypeService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.CabinetInstallRecordService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.agentappserver.service.yms.TerminalService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetInstallRecord;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller("agent_api_v1_agent_hdg_cabinet_install_record")
@RequestMapping("/agent_api/v1/agent/hdg/cabinet_install_record")
public class CabinetInstallRecordController extends ApiController{
    @Autowired
    CabinetInstallRecordService cabinetInstallRecordService;
    @Autowired
    CabinetBatteryTypeService cabinetBatteryTypeService;
    @Autowired
    AgentService agentService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    TerminalService terminalService;
    @Autowired
    UserService userService;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CabinetOnlineParam {
        public String cabinetId;
        public String cabinetName;
        public String address;
        public Integer areaId;
        public String street;
        public Double lng;
        public Double lat;
        public String terminalId;
        public Double price;
        public Integer permitExchangeVolume;
        public Integer permitSwapoutVolume;
        public Integer[] batteryTypeList;
        public String imagePath1;
        public String imagePath2;
        public Integer minExchangeVolume;
    }

    /*
    * 63 设备上线
    * */

    @ResponseBody
    @RequestMapping(value = "/cabinet_online")
    public RestResult CabinetOnline(@Valid @RequestBody CabinetOnlineParam param) {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        long userId = tokenData.userId;
        Agent agent = agentService.find(agentId);
        User user = userService.find(userId);
        Cabinet cabinet = cabinetService.find(param.cabinetId);
        if (cabinet != null) {
            if (cabinet.getUpLineStatus() == CabinetInstallRecord.Status.APPROVE.getValue() && agentId != Constant.TEST_AGENT_ID) {
                return RestResult.result(RespCode.CODE_2.getValue(), "设备已上线");
            }
            if (cabinet.getUpLineStatus() == CabinetInstallRecord.Status.UNREVIEWED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "设备上线申请中");
            }
        }
        if(StringUtils.isNotEmpty(param.terminalId)){
            Terminal terminal = terminalService.find(param.terminalId);
            if (terminal == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "屏幕终端不存在，请扫描正确二维码");
            }
            if(terminal.getCabinetId() != null && !terminal.getCabinetId().equals(param.cabinetId)){
                return RestResult.result(RespCode.CODE_2.getValue(), "屏幕已经绑定其他换电柜");
            }
        }
        CabinetInstallRecord record = new CabinetInstallRecord();
        record.setAgentId(agentId);
        record.setAgentName(agent.getAgentName());
        record.setAgentCode(agent.getAgentCode());
        record.setCabinetId(param.cabinetId);
        record.setCabinetName(param.cabinetName);
        record.setAddress(param.address);
        record.setTerminalId(param.terminalId);
        record.setPrice(param.price);
        record.setPermitExchangeVolume(param.permitExchangeVolume);
        record.setChargeFullVolume(param.permitSwapoutVolume);
        record.setImagePath1(param.imagePath1);
        record.setImagePath2(param.imagePath2);
        record.setMinExchangeVolume(param.minExchangeVolume);
        record.setBroker(user.getFullname());
        record.setTel(user.getMobile());
        cabinetInstallRecordService.insert(record);
        for (int batteryType : param.batteryTypeList) {
            cabinetBatteryTypeService.insert(record.getCabinetId(), batteryType);
        }

        //修改地址
        if(param.areaId != null && param.lng != null && param.lat != null){
            cabinetService.updateAddress(param.cabinetId,
                    param.areaId,
                    param.street,
                    param.lng,
                    param.lat);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }
}
