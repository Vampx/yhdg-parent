package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayDegreeStats;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetDegreeInput;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.hdg.CabinetDegreeInputMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CabinetDegreeInputService extends AbstractService {

    @Autowired
    CabinetDegreeInputMapper cabinetDegreeInputMapper;
    @Autowired
    CabinetDayDegreeStatsService cabinetDayDegreeStatsService;
    @Autowired
    CabinetService cabinetService;

    public Page findPage(CabinetDegreeInput cabinetDegreeInput){
        Page page = cabinetDegreeInput.buildPage();
        if(cabinetDegreeInput.getEndTime()!=null){
            cabinetDegreeInput.setEndTime(getEndDate(cabinetDegreeInput.getEndTime()));
        }
        page.setTotalItems(cabinetDegreeInputMapper.findPageCount(cabinetDegreeInput));
        cabinetDegreeInput.setBeginIndex(page.getOffset());

        List<CabinetDegreeInput> list = cabinetDegreeInputMapper.findPageResult(cabinetDegreeInput);
        for (CabinetDegreeInput cabinetDegreeInput1 : list) {
            AgentInfo agentInfo = findAgentInfo(cabinetDegreeInput1.getAgentId());
            if (agentInfo != null) {
                cabinetDegreeInput1.setAgentName(agentInfo.getAgentName());
            }
            if(cabinetDegreeInput1.getStatus()!=null){
                cabinetDegreeInput1.setStatusName(CabinetDegreeInput.Status.getName(cabinetDegreeInput1.getStatus()));
            }
            cabinetDegreeInput1.setBeginDate(DateFormatUtils.format(cabinetDegreeInput1.getBeginTime(), Constant.DATE_FORMAT));
            cabinetDegreeInput1.setEndDate(DateFormatUtils.format(cabinetDegreeInput1.getEndTime(), Constant.DATE_FORMAT));
            CabinetDayDegreeStats cabinetDayDegreeStats = cabinetDayDegreeStatsService.findLast(cabinetDegreeInput1.getCabinetId());
            cabinetDegreeInput1.setSystemInputNum(cabinetDayDegreeStats.getEndNum());
        }
        page.setResult(list);
        return page;
    }

    private Date getEndDate(Date startDate){
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(startDate);
        calendarStart.set(Calendar.HOUR_OF_DAY, 23);
        calendarStart.set(Calendar.MINUTE, 59);
        calendarStart.set(Calendar.SECOND, 59);
        calendarStart.set(Calendar.MILLISECOND, 999);
        startDate = calendarStart.getTime();
        return startDate;
    }

    public  Map<String,Object>  findByCabinetId(String cabinetId){
        Map<String,Object> map =new HashMap<String, Object>();
        List<CabinetDegreeInput> entity = cabinetDegreeInputMapper.findByCabinetId(cabinetId);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String agentName=null;
        String cabinetName=null;
        String cabinetIds=null;
        String createTime=null;
        Double degreePrice=null;
        Double totalElectricity=0D;
        Double totalElectricityCharges=0D;
        for (CabinetDegreeInput cabinetDegreeInput:entity) {
            if(cabinetDegreeInput !=null){
                if (cabinetDegreeInput.getAgentId() != null) {
                    AgentInfo agentInfo = findAgentInfo(cabinetDegreeInput.getAgentId());
                    if(agentInfo!=null){
                        cabinetDegreeInput.setAgentName(agentInfo.getAgentName());
                    }
                }
                totalElectricity=totalElectricity+cabinetDegreeInput.getDegree();
                totalElectricityCharges=totalElectricityCharges+cabinetDegreeInput.getDegreeMoney();
            }
        }
        if(entity!=null&&entity.size()>0){
            agentName=entity.get(0).getAgentName();
            cabinetName=entity.get(0).getCabinetName();
            cabinetIds=entity.get(0).getCabinetId();
            createTime=formatter.format(entity.get(0).getCreateTime());
            degreePrice=entity.get(0).getDegreePrice();
        }else{
            return map;
        }
        map.put("agentName",agentName);
        map.put("cabinetName",cabinetName);
        map.put("cabinetId",cabinetIds);
        map.put("createTime",createTime);
        map.put("degreePrice",formatDouble(degreePrice));
        map.put("totalElectricity",formatDouble(totalElectricity/100));
        map.put("totalElectricityCharges",formatDouble(totalElectricity/100));
        return map;
    }

    public Page findVoidPage(CabinetDegreeInput cabinetDegreeInput){
        Page page = cabinetDegreeInput.buildPage();
        if(cabinetDegreeInput.getEndTime()!=null){
            cabinetDegreeInput.setEndTime(getEndDate(cabinetDegreeInput.getEndTime()));
        }
        page.setTotalItems(cabinetDegreeInputMapper.findViewPageCount(cabinetDegreeInput));
        cabinetDegreeInput.setBeginIndex(page.getOffset());
        List<CabinetDegreeInput> pageResult = cabinetDegreeInputMapper.findViewPageResult(cabinetDegreeInput);
        for (CabinetDegreeInput cabinetDegreeInput1 : pageResult) {
            AgentInfo agentInfo = findAgentInfo(cabinetDegreeInput1.getAgentId());
            if (agentInfo != null) {
                cabinetDegreeInput1.setAgentName(agentInfo.getAgentName());
            }
            if(cabinetDegreeInput1.getStatus()!=null){
                cabinetDegreeInput1.setStatusName(CabinetDegreeInput.Status.getName(cabinetDegreeInput1.getStatus()));
            }
            cabinetDegreeInput1.setBeginDate(DateFormatUtils.format(cabinetDegreeInput1.getBeginTime(), Constant.DATE_FORMAT));
            cabinetDegreeInput1.setEndDate(DateFormatUtils.format(cabinetDegreeInput1.getEndTime(), Constant.DATE_FORMAT));
        }
        page.setResult(pageResult);
        return page;
    }

    public static String formatDouble(double d) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(d);
    }
}
