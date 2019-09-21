package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.exportrecord.hdg;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.DataResult;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.AgentService;
import cn.com.yusong.yhdg.agentappserver.service.basic.PersonService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.ExportRecordService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Person;
import cn.com.yusong.yhdg.common.domain.hdg.ExportRecord;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("agent_api_v1_exportrecord_hdg_export_record")
@RequestMapping(value = "/agent_api/v1/export_record/hdg/export_record")
public class ExportRecordController extends ApiController {

	@Autowired
	AgentService agentService;
	@Autowired
	ExportRecordService exportRecordService;
	@Autowired
	PersonService personService;

	/**
	 * 4-查询发货总数
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/first_info.htm")
	public RestResult firstInfo() {
		TokenCache.Data tokenData = getTokenData();
		Long personId = tokenData.userId;
		Person person = personService.find(personId);
		if (person == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
		}
		NotNullMap data = new NotNullMap();
		exportRecordService.findFirstInfo(data, person.getId());
		return DataResult.dataResult(RespCode.CODE_0.getValue(), null, data);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class FindListInfoParam {
		public Integer exportType;
		public int offset;
		public int limit;
	}

	/**
	 * 9-查询首页发货数据列表
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/first_list_info.htm")
	public RestResult firstListInfo(@Valid @RequestBody FindListInfoParam param) {
		if (param.exportType != ExportRecord.ExportType.BATTERY.getValue() && param.exportType != ExportRecord.ExportType.CABINET.getValue()) {
			return RestResult.result(RespCode.CODE_2.getValue(), "发货类型不存在");
		}
		TokenCache.Data tokenData = getTokenData();
		Long personId = tokenData.userId;
		Person person = personService.find(personId);
		if (person == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
		}
		List<Map> list = new ArrayList<Map>();
		exportRecordService.findFirstListInfo(list, person.getId(), param.exportType, param.offset, param.limit);
		return DataResult.dataResult(RespCode.CODE_0.getValue(), null, list);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class AgentListParam {
		public String agentName;
	}

	/**
	 * 6-查询运营商列表
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/agent_list.htm")
	public RestResult agentList(@Valid @RequestBody AgentListParam param) {
		List<Map> list = new ArrayList<Map>();

		List<Agent> agentList = agentService.findAll(param.agentName);
		for (Agent agent : agentList) {
			Map map = new HashMap();
			map.put("id", agent.getId());
			map.put("agentName", agent.getAgentName());
			list.add(map);
		}

		return DataResult.dataResult(RespCode.CODE_0.getValue(), null, list);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class SendToAgentParam {
		public Integer agentId;
		public List<Map> list;
	}

	/**
	 * 7-提交发货
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/send_to_agent.htm")
	public RestResult sendToAgent(@Valid @RequestBody SendToAgentParam param) {
		TokenCache.Data tokenData = getTokenData();
		Long personId = tokenData.userId;
		Person person = personService.find(personId);
		if (person == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
		}
		if (param.agentId == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "参数错误");
		}
		if (param.list == null || param.list.size() == 0) {
			return RestResult.result(RespCode.CODE_2.getValue(), "请输入发货信息");
		}
		return exportRecordService.sendToAgent(param.agentId, param.list, personId.intValue(), person.getFullname());
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class DeleteParam {
		public int id;
	}

	/**
	 * 8-删除发货记录
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delete.htm")
	public RestResult firstInfo(@Valid @RequestBody DeleteParam param) {
		ExportRecord exportRecord = exportRecordService.find(param.id);
		if (exportRecord == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "发货记录不存在");
		}
		exportRecordService.delete(param.id);
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
	}

}
