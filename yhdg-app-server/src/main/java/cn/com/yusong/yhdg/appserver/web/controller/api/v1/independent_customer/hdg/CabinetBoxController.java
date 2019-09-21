package cn.com.yusong.yhdg.appserver.web.controller.api.v1.independent_customer.hdg;

import cn.com.yusong.yhdg.appserver.biz.client.ClientBizUtils;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.hdg.CabinetOperateLogService;
import cn.com.yusong.yhdg.appserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetOperateLog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller("api_v1_independent_customer_hdg_cabinet_box")
@RequestMapping("/api/v1/independent_customer/hdg/cabinet_box")
public class CabinetBoxController extends ApiController {

	static final Logger log = LogManager.getLogger(CabinetBoxController.class);
	@Autowired
	CabinetService cabinetService;
	@Autowired
	CabinetOperateLogService cabinetOperateLogService;

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class OpenBoxParam {
		public String cabinetId;
		public String boxNum;
		public String customerMobile;
		public Long time;
		public String sign;
	}

	@NotLogin
	@ResponseBody
	@RequestMapping(value = "/open_box.htm")
	public RestResult batteryOpenBox(@RequestBody OpenBoxParam param) throws Exception {
		if(!checkSign(param.time, param.sign)){
			return RestResult.result(RespCode.CODE_2.getValue(), "签名错误");
		}

		Cabinet cabinet = cabinetService.find(param.cabinetId);
		if (cabinet == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "换电柜不存在");
		}

		CabinetOperateLog operateLog = new CabinetOperateLog();
		operateLog.setAgentId(cabinet.getAgentId() != null ? cabinet.getAgentId() : 0);
		operateLog.setCabinetId(cabinet.getId());
		operateLog.setCabinetName(cabinet.getCabinetName());
		operateLog.setBoxNum(param.boxNum);
		operateLog.setOperateType(CabinetOperateLog.OperateType.OPEN_DOOR.getValue());
		operateLog.setOperatorType(CabinetOperateLog.OperatorType.AGENT_CABINET.getValue());
		operateLog.setOperator(param.customerMobile);

		//发送开箱指令
		RestResult result = ClientBizUtils.openStandardBox(config, param.cabinetId, param.boxNum, cabinet.getSubtype());

		int openSuccess = 0;
		Map data = new HashMap();

		if (result.getCode() == RespCode.CODE_0.getValue()) { //开箱成功后锁定箱子
			if (log.isDebugEnabled()) {
				log.debug("open {}, {} box success", param.cabinetId, param.boxNum);
			}
			openSuccess = 1;
			operateLog.setContent("远程开箱成功, 箱门状态: " + (result.getCode() == 0 ? "开门" : "关门"));
			operateLog.setCreateTime(new Date());
			cabinetOperateLogService.insert(operateLog);
		} else {
			openSuccess = 0;
			if (log.isDebugEnabled()) {
				log.error("open {}, {} box fail", param.cabinetId, param.boxNum);
			}
			operateLog.setContent("远程开箱失败, " + result.getMessage());
			operateLog.setCreateTime(new Date());
			cabinetOperateLogService.insert(operateLog);
		}

		data.put("openSuccess", openSuccess);

		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
	}

}
