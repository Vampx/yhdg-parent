package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.hdg;

import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.yms.TerminalService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import org.apache.commons.lang.time.DateFormatUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("agent_api_v1_agent_hdg_terminal")
@RequestMapping("/agent_api/v1/agent/hdg/terminal")
public class TerminalController extends ApiController {

	@Autowired
	TerminalService terminalService;

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ListParam {
		public Integer isOnline;
		public int offset;
		public int limit;
		public int total;
	}
	/**
	 * 14-查询待分配换电柜列表
	 *
	 * @param param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/wait_assign_list.htm")
	public RestResult list(@RequestBody ListParam param) {

		List<Terminal> list = terminalService.findList(param.isOnline, param.offset, param.limit);
		List<Map> result = new ArrayList<Map>();
		Map totalMap = new HashMap();
		if (param.total == ConstEnum.Flag.TRUE.getValue()) {
			final int All = 2;
			totalMap.put("offline", terminalService.findTerminalCount(ConstEnum.Flag.FALSE.getValue()));
			totalMap.put("online", terminalService.findTerminalCount(ConstEnum.Flag.TRUE.getValue()));
			totalMap.put("all", terminalService.findTerminalCount(All));

			for (Terminal terminal : list) {
				Map line = new HashMap();
				line.put("id", terminal.getId());
				line.put("version", terminal.getVersion());
				line.put("isOnline", terminal.getIsOnline());
				line.put("heartTime", DateFormatUtils.format(terminal.getHeartTime(), Constant.DATE_TIME_FORMAT));
				result.add(line);
			}
		} else {
			for (Terminal terminal : list) {
				Map line = new HashMap();
				line.put("id", terminal.getId());
				line.put("version", terminal.getVersion());
				line.put("isOnline", terminal.getIsOnline());
				line.put("heartTime", DateFormatUtils.format(terminal.getHeartTime(), Constant.DATE_TIME_FORMAT));
				result.add(line);
			}
		}

		Map data = new HashMap();

		data.put("total", totalMap);
		data.put("list", result);

		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
	}

}
