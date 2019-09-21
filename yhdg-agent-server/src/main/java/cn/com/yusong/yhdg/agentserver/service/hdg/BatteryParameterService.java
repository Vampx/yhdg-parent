package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.*;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class BatteryParameterService extends AbstractService {

    static Logger log = LoggerFactory.getLogger(BatteryParameterService.class);

    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    BatteryParameterMapper batteryParameterMapper;
    @Autowired
    BatteryParameterLogMapper batteryParameterLogMapper;

    public BatteryParameter find(String id) {
       return batteryParameterMapper.find(id);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult update(BatteryParameter batteryParameter, String operator) {
        //数据处理
        if(batteryParameter.getCellOvDelay() != null){
            batteryParameter.setCellOvDelay(batteryParameter.getCellOvDelay() * 10);
        }
        if(batteryParameter.getCellUvDelay() != null){
            batteryParameter.setCellUvDelay(batteryParameter.getCellUvDelay() * 10);
        }
        if(batteryParameter.getPackOvDelay() != null){
            batteryParameter.setPackOvDelay(batteryParameter.getPackOvDelay() * 10);
        }
        if(batteryParameter.getPackUvDelay() != null){
            batteryParameter.setPackUvDelay(batteryParameter.getPackUvDelay() * 10);
        }
        if(batteryParameter.getChgOtDelay() != null){
            batteryParameter.setChgOtDelay(batteryParameter.getChgOtDelay() * 10);
        }
        if(batteryParameter.getChgUtDelay() != null){
            batteryParameter.setChgUtDelay(batteryParameter.getChgUtDelay() * 10);
        }
        if(batteryParameter.getDsgOtDelay() != null){
            batteryParameter.setDsgOtDelay(batteryParameter.getDsgOtDelay() * 10);
        }
        if(batteryParameter.getDsgUtDelay() != null){
            batteryParameter.setDsgUtDelay(batteryParameter.getDsgUtDelay() * 10);
        }
        if(batteryParameter.getChgOcDelay() != null){
            batteryParameter.setChgOcDelay(batteryParameter.getChgOcDelay() * 10);
        }
        if(batteryParameter.getDsgOcDelay() != null){
            batteryParameter.setDsgOcDelay(batteryParameter.getDsgOcDelay() * 10);
        }
        //充电过流释放时间
        if (batteryParameter.getChgOcRelease() != null) {
            batteryParameter.setChgOcRelease(batteryParameter.getChgOcRelease() * 10);
        }
        //放电过流释放时间
        if (batteryParameter.getDsgOcRelease() != null) {
            batteryParameter.setDsgOcRelease(batteryParameter.getDsgOcRelease() * 10);
        }



        BatteryParameter dbBatteryParameter = batteryParameterMapper.find(batteryParameter.getId());
        if (dbBatteryParameter == null) {
            return ExtResult.failResult("记录不存在");
        }

        //比較不同
        List<Map<String ,Object>> list = YhdgUtils.compareTwoClass(dbBatteryParameter, batteryParameter);
        if(list.size()> 0){
            for(Map<String ,Object> map : list){
                String objectStr = map.get("name").toString();
                if("upBms".equals(objectStr)){
                    continue;//开关不在控制范围内
                }
                String oldValue =  (map.get("old") != null ? map.get("old").toString() : "" );
                String newValue =  (map.get("new") != null ? map.get("new").toString() : "" );
                //将未上报的作废
                batteryParameterLogMapper.cancel(dbBatteryParameter.getId(), objectStr, BatteryParameterLog.Status.NO_REPORT.getValue(), BatteryParameterLog.Status.CANCEL.getValue());
                //保存修改記錄
                BatteryParameterLog batteryParameterLog = new BatteryParameterLog();
                batteryParameterLog.setBatteryId(dbBatteryParameter.getId());
                batteryParameterLog.setParamId(objectStr);
                batteryParameterLog.setParamName(BatteryParameter.Parameter.getName(objectStr));
                batteryParameterLog.setOldValue(oldValue);
                batteryParameterLog.setNewValue(newValue);
                batteryParameterLog.setStatus(BatteryParameterLog.Status.NO_REPORT.getValue());
                batteryParameterLog.setOperator(operator);
                batteryParameterLog.setCreateTime(new Date());
                batteryParameterLogMapper.insert(batteryParameterLog);
            }

            //保存最新
            batteryParameterMapper.update(batteryParameter);
        }

        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult batchUpdate(BatteryParameter batteryParameter, String operator) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        //数据处理
        if (batteryParameter.getCellOvDelay() != null) {
            batteryParameter.setCellOvDelay(batteryParameter.getCellOvDelay() * 10);
        }
        if (batteryParameter.getCellUvDelay() != null) {
            batteryParameter.setCellUvDelay(batteryParameter.getCellUvDelay() * 10);
        }
        if (batteryParameter.getPackOvDelay() != null) {
            batteryParameter.setPackOvDelay(batteryParameter.getPackOvDelay() * 10);
        }
        if (batteryParameter.getPackUvDelay() != null) {
            batteryParameter.setPackUvDelay(batteryParameter.getPackUvDelay() * 10);
        }
        if (batteryParameter.getChgOtDelay() != null) {
            batteryParameter.setChgOtDelay(batteryParameter.getChgOtDelay() * 10);
        }
        if (batteryParameter.getChgUtDelay() != null) {
            batteryParameter.setChgUtDelay(batteryParameter.getChgUtDelay() * 10);
        }
        if (batteryParameter.getDsgOtDelay() != null) {
            batteryParameter.setDsgOtDelay(batteryParameter.getDsgOtDelay() * 10);
        }
        if (batteryParameter.getDsgUtDelay() != null) {
            batteryParameter.setDsgUtDelay(batteryParameter.getDsgUtDelay() * 10);
        }
        if (batteryParameter.getChgOcDelay() != null) {
            batteryParameter.setChgOcDelay(batteryParameter.getChgOcDelay() * 10);
        }
        if (batteryParameter.getDsgOcDelay() != null) {
            batteryParameter.setDsgOcDelay(batteryParameter.getDsgOcDelay() * 10);
        }
        //充电过流释放时间
        if (batteryParameter.getChgOcRelease() != null) {
            batteryParameter.setChgOcRelease(batteryParameter.getChgOcRelease() * 10);
        }
        //放电过流释放时间
        if (batteryParameter.getDsgOcRelease() != null) {
            batteryParameter.setDsgOcRelease(batteryParameter.getDsgOcRelease() * 10);
        }

        String idsData = batteryParameter.getIdsData();
        String[] ids = idsData.split(",");
        for (String id : ids) {
            //复制表单数据对象，用这个对象进行对比
            BatteryParameter newParameter = new BatteryParameter();
            BeanUtils.copyProperties(batteryParameter, newParameter);
            BatteryParameter dbBatteryParameter = batteryParameterMapper.find(id);
            //没有长心跳的数据过滤掉
            if (dbBatteryParameter.getLongReportTime() == null) {
                continue;
            }
            if (dbBatteryParameter == null) {
                return ExtResult.failResult("记录不存在");
            } else {
                newParameter.setId(id);
            }
            //比较之前的处理：如果没有填写，就设置为数据本来的值
            Field[] fields = newParameter.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                String fieldName = fields[i].getName();
                String firstLetter = fieldName.substring(0, 1).toUpperCase();
                String getter = "get" + firstLetter + fieldName.substring(1);
                String setter = "set" + firstLetter + fieldName.substring(1);
                Method getMethod = newParameter.getClass().getMethod(getter, new Class[]{});
                Method setMethod = newParameter.getClass().getMethod(setter, fields[i].getType());
                Object newValue = getMethod.invoke(newParameter, new Object[]{});
                Object oldValue = getMethod.invoke(dbBatteryParameter, new Object[]{});
                if (newValue == null || (fields[i].getType() == String.class && newValue.equals(""))) {
                    //根据属性名获取本来的值
                    setMethod.invoke(newParameter, oldValue);
                }
                //生产日期
                if (fieldName.equals("mfd")&&newValue.equals("//")) {
                    setMethod.invoke(newParameter, oldValue);
                }
                //过流及短路保护值翻倍
                if (fieldName.equals("rsns") && newValue.equals(0)) {
                    setMethod.invoke(newParameter, oldValue);
                }
                //功能配置
                if (fieldName.equals("function") && newValue.equals(0)) {
                    setMethod.invoke(newParameter, oldValue);
                }
                //NTC配置
                if (fieldName.equals("ntcConfig") && newValue.equals(0)) {
                    setMethod.invoke(newParameter, oldValue);
                }
                //ocvTable
                if (fieldName.equals("ocvTable")) {
                    String oldOcv = (String) oldValue;
                    if (oldOcv == null) {
                        oldOcv = ",,,,,,,,,,,,,,,,,,,,空";
                    }
                    if (oldOcv.substring(oldOcv.length() - 1).equals(",")) {
                        oldOcv = oldOcv + "空";
                    }
                    String[] oldOcvValues = oldOcv.split(",");
                    //如果最后一个值为"空"，将其设置为""
                    if (oldOcvValues[oldOcvValues.length - 1].equals("空")) {
                        oldOcvValues[oldOcvValues.length - 1] = "";
                    }
                    String newOcv = (String) newValue;
                    String[] newOcvValues = newOcv.split(",");
                    for (int j = 0; j < oldOcvValues.length; j++) {
                        if (newOcvValues[j].equals("无")) {
                            newOcvValues[j] = oldOcvValues[j];
                        }
                    }
                    StringBuilder builder = new StringBuilder();
                    for (int j = 0; j < oldOcvValues.length; j++) {
                        if (j != oldOcvValues.length - 1) {
                            builder.append(newOcvValues[j] + ",");
                        } else {
                            builder.append(newOcvValues[j]);
                        }
                    }
                    String ocvTableValue = builder.toString();
                    setMethod.invoke(newParameter, ocvTableValue);
                }
            }

            //比較不同
            List<Map<String, Object>> list = YhdgUtils.compareTwoClass(dbBatteryParameter, newParameter);
            if (list.size() > 0) {
                for (Map<String, Object> map : list) {
                    String objectStr = map.get("name").toString();
                    if ("idsData".equals(objectStr)) {
                        continue;
                    }
                    if ("upBms".equals(objectStr)) {
                        continue;//开关不在控制范围内
                    }
                    String oldValue = (map.get("old") != null ? map.get("old").toString() : "");
                    String newValue = (map.get("new") != null ? map.get("new").toString() : "");
                    //将未上报的作废
                    batteryParameterLogMapper.cancel(newParameter.getId(), objectStr, BatteryParameterLog.Status.NO_REPORT.getValue(), BatteryParameterLog.Status.CANCEL.getValue());
                    //保存修改記錄
                    BatteryParameterLog batteryParameterLog = new BatteryParameterLog();
                    batteryParameterLog.setBatteryId(newParameter.getId());
                    batteryParameterLog.setParamId(objectStr);
                    batteryParameterLog.setParamName(BatteryParameter.Parameter.getName(objectStr));
                    batteryParameterLog.setOldValue(oldValue);
                    batteryParameterLog.setNewValue(newValue);
                    batteryParameterLog.setStatus(BatteryParameterLog.Status.NO_REPORT.getValue());
                    batteryParameterLog.setOperator(operator);
                    batteryParameterLog.setCreateTime(new Date());
                    batteryParameterLogMapper.insert(batteryParameterLog);
                }
                //保存最新
                batteryParameterMapper.update(newParameter);
            }
        }
        return ExtResult.successResult();
    }
}
