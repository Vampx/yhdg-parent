package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.AgentInOutMoneyMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.EstateInOutMoneyMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.CabinetDayDegreeStatsMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.CabinetDegreeInputMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.CabinetMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.EstateMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentInOutMoney;
import cn.com.yusong.yhdg.common.domain.basic.EstateInOutMoney;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayDegreeStats;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetDegreeInput;
import cn.com.yusong.yhdg.common.domain.hdg.Estate;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class CabinetDegreeInputService extends AbstractService {

	@Autowired
	CabinetMapper cabinetMapper;
	@Autowired
	AgentMapper agentMapper;
	@Autowired
	EstateMapper estateMapper;
	@Autowired
	CabinetDegreeInputMapper cabinetDegreeInputMapper;
	@Autowired
	CabinetDayDegreeStatsMapper cabinetDayDegreeStatsMapper;
	@Autowired
	AgentInOutMoneyMapper agentInOutMoneyMapper;
	@Autowired
	EstateInOutMoneyMapper estateInOutMoneyMapper;

	@Transactional(rollbackFor = Throwable.class)
	public RestResult create(String cabinetId, int degree, int systemDegree, String userName) {
		Date date = new Date();
		Cabinet cabinet = cabinetMapper.find(cabinetId);
		if (cabinet == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "设备不存在");
		}
		if (cabinet.getPrice() == null || cabinet.getPrice() == 0) {
			return RestResult.result(RespCode.CODE_2.getValue(), "电价未设置，请先设置电价");
		}
		if (cabinet.getEstateId() == null || cabinet.getEstateId() == 0) {
			return RestResult.result(RespCode.CODE_2.getValue(), "设备未关联物业，请先设置");
		}
		Agent agent = agentMapper.find(cabinet.getAgentId());
		Estate estate = null;
		if (cabinet.getEstateId() != null) {
			estate = estateMapper.find(cabinet.getEstateId());
		}

		CabinetDegreeInput cabinetDegreeInput = new CabinetDegreeInput();

		CabinetDegreeInput lastInput = cabinetDegreeInputMapper.findEnd(cabinetId);

		if (lastInput != null) { //上次录入过
			if (DateUtils.truncatedEquals(lastInput.getCreateTime(), date, Calendar.DAY_OF_MONTH)) {
				return RestResult.result(RespCode.CODE_2.getValue(), "设备一天内不能重复录入表码");
			}

			if (degree < lastInput.getEndNum()) {
				return RestResult.result(RespCode.CODE_2.getValue(), "本次录入度数不能小于上次结束度数");
			}
			cabinetDegreeInput.setBeginNum(systemDegree);
			cabinetDegreeInput.setEndNum(degree);
			cabinetDegreeInput.setDegree(degree - systemDegree);
			cabinetDegreeInput.setDegreeMoney((int)(1D * degree / 100 * cabinet.getPrice() * 100)-(int)(1D * systemDegree / 100 * cabinet.getPrice() * 100));
			cabinetDegreeInput.setBeginTime(lastInput.getEndTime());
			cabinetDegreeInput.setEndTime(date);

			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(DateUtils.truncate(cabinetDegreeInput.getBeginTime(), Calendar.DAY_OF_MONTH));
			int dayCount = 0;
			while (calendar.getTimeInMillis() <= cabinetDegreeInput.getEndTime().getTime()) {
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				dayCount++;
			}
			cabinetDegreeInput.setDayCount(dayCount);

		} else { //第一次录入

			cabinetDegreeInput.setBeginNum(systemDegree);
			cabinetDegreeInput.setEndNum(degree);
			cabinetDegreeInput.setDegree(degree-systemDegree);
			cabinetDegreeInput.setDegreeMoney((int)(1D * degree / 100 * cabinet.getPrice() * 100)-(int)(1D * systemDegree / 100 * cabinet.getPrice() * 100));
			cabinetDegreeInput.setBeginTime(date);
			cabinetDegreeInput.setEndTime(date);
			cabinetDegreeInput.setDayCount(0);
		}

		cabinetDegreeInput.setCabinetId(cabinet.getId());
		cabinetDegreeInput.setCabinetName(cabinet.getCabinetName());

		cabinetDegreeInput.setAgentId(agent.getId());
		cabinetDegreeInput.setAgentName(agent.getAgentName());

		if (estate != null) {
			cabinetDegreeInput.setEstateId(estate.getId());
			cabinetDegreeInput.setEstateName(estate.getEstateName());
		}
		cabinetDegreeInput.setStatus(ConstEnum.Flag.FALSE.getValue());
		cabinetDegreeInput.setDegreePrice(cabinet.getPrice());

		CabinetDayDegreeStats cabinetDayDegreeStats = cabinetDayDegreeStatsMapper.findLast(cabinet.getId());

		cabinetDegreeInput.setChargerNum(cabinetDayDegreeStats.getNum());

		cabinetDegreeInput.setCreateUserName(userName);//录入人
		cabinetDegreeInput.setCreateTime(date);

		if (agent.getBalance() < cabinetDegreeInput.getDegreeMoney()) {
			return RestResult.result(RespCode.CODE_2.getValue(), "运营商余额不足");
		}

		cabinetDegreeInputMapper.insert(cabinetDegreeInput);

		agentMapper.updateBalance(agent.getId(), -cabinetDegreeInput.getDegreeMoney());

		//运营商流水
		AgentInOutMoney inOutMoney = new AgentInOutMoney();
		inOutMoney.setAgentId(agent.getId());
		inOutMoney.setMoney(-cabinetDegreeInput.getDegreeMoney());
		inOutMoney.setBalance(agentMapper.find(agent.getId()).getBalance());
		inOutMoney.setBizType(AgentInOutMoney.BizType.OUT_AGENT_DEGREE_PRICE.getValue());
		inOutMoney.setType(AgentInOutMoney.Type.OUT.getValue());
		inOutMoney.setBizId(String.valueOf(cabinetDegreeInput.getId()));
		inOutMoney.setOperator(userName);
		inOutMoney.setCreateTime(new Date());
		agentInOutMoneyMapper.insert(inOutMoney);

		estateMapper.updateBalance(estate.getId(), cabinetDegreeInput.getDegreeMoney());

		EstateInOutMoney estateInOutMoney = new EstateInOutMoney();
		estateInOutMoney.setEstateId(estate.getId());
		estateInOutMoney.setType(EstateInOutMoney.Type.INCOME.getValue());
		estateInOutMoney.setBizType(EstateInOutMoney.BizType.IN_ESTATE_DEGREE_PRICE.getValue());
		estateInOutMoney.setBizId(String.valueOf(cabinetDegreeInput.getId()));
		estateInOutMoney.setMoney(cabinetDegreeInput.getDegreeMoney());
		estateInOutMoney.setBalance(estateMapper.find(estate.getId()).getBalance());
		estateInOutMoney.setPrice(cabinet.getPrice());
		estateInOutMoney.setUseVolume(cabinetDegreeInput.getDegree());
		estateInOutMoney.setCreateTime(new Date());
		estateInOutMoneyMapper.insert(estateInOutMoney);

		cabinetMapper.updateLastCharger(cabinetId, degree, cabinetDegreeInput.getDegreeMoney(), date);

		//更新结算状态
		cabinetDegreeInputMapper.updateStatus(cabinetDegreeInput.getId(), ConstEnum.Flag.TRUE.getValue());

		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
	}

	public List<CabinetDegreeInput> findListByEstate(int agentId, long estateId, int offset, int limit) {

		return cabinetDegreeInputMapper.findListByEstate(agentId, estateId, offset, limit);
	}

}
