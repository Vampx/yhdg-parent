package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.*;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CabinetBoxService extends AbstractService {

    @Autowired
    AgentMapper agentMapper;
    @Autowired
    CabinetBoxMapper cabinetBoxMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    BatteryService batteryService;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    CabinetOperateLogMapper cabinetOperateLogMapper;
    @Autowired
    CabinetChargerMapper cabinetChargerMapper;

    public Page findPage(CabinetBox cabinetBox) {
        Page<CabinetBox> page = new Page<CabinetBox>();
        List<CabinetBox> list = cabinetBoxMapper.findPageResult(cabinetBox);
        page.setTotalItems(list.size());
        page.setResult(list);
        return page;
    }

    public CabinetBox find(String cabinetId, String boxNum) {
        return cabinetBoxMapper.find(cabinetId, boxNum);
    }

    public int statsBoxCount(Integer agentId, String cabinetId) {
        return cabinetBoxMapper.statsBoxCount(agentId, cabinetId);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult insert(CabinetBox cabinetBox) {
        if (cabinetBoxMapper.find(cabinetBox.getCabinetId(), cabinetBox.getBoxNum()) != null) {
            return ExtResult.failResult("箱号已存在不能重复添加");
        }
        cabinetBox.setIsActive(ConstEnum.Flag.TRUE.getValue());
        cabinetBox.setBoxStatus(CabinetBox.BoxStatus.EMPTY.getValue());
        Cabinet cabinet = cabinetMapper.find(cabinetBox.getCabinetId());
        if (cabinet != null) {
            cabinetBox.setSubtype(cabinet.getSubtype());
            cabinetBox.setCabinetId(cabinet.getId());
            cabinetBox.setIsOnline(cabinet.getIsOnline());
        }
        cabinetBox.setIsOpen(0);
        if (cabinetBoxMapper.insert(cabinetBox) == 0) {
            return ExtResult.failResult("操作失败");
        } else {
            return ExtResult.successResult();
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult update(CabinetBox entity) {
        if (cabinetBoxMapper.update(entity) == 0) {
            return ExtResult.failResult("操作失败");
        }
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult updateIsActive(CabinetBox entity, String operator) {
        if (cabinetBoxMapper.updateIsActive(entity) == 0) {
            return ExtResult.failResult("操作失败");
        }
        //添加操作日志
        Cabinet cabinet = cabinetMapper.find(entity.getCabinetId());
        CabinetOperateLog operateLog = new CabinetOperateLog();
        operateLog.setAgentId(cabinet.getAgentId());
        operateLog.setCabinetId(cabinet.getId());
        operateLog.setCabinetName(cabinet.getCabinetName());
        operateLog.setBoxNum(entity.getBoxNum());
        operateLog.setOperatorType(CabinetOperateLog.OperatorType.PLATFORM.getValue());
        operateLog.setOperator(operator);
        if(entity.getIsActive() == ConstEnum.Flag.TRUE.getValue()){
            operateLog.setOperateType(CabinetOperateLog.OperateType.ACTIVE.getValue());
            operateLog.setContent("箱门激活成功");
        }else{
            operateLog.setOperateType(CabinetOperateLog.OperateType.NO_ACTIVE.getValue());
            operateLog.setContent("箱门禁用成功");
        }
        operateLog.setCreateTime(new Date());
        cabinetOperateLogMapper.insert(operateLog);


        return ExtResult.successResult();
    }

    public ExtResult delete(String cabinetId, String boxNum) {
        if (cabinetBoxMapper.delete(cabinetId, boxNum) == 0) {
            return ExtResult.failResult("箱子被使用不能删除");
        }
        return ExtResult.successResult();
    }

    public int findBatteryCountByStatus(String cabinetId, int status) {
        return cabinetBoxMapper.findBatteryCountByStatus(cabinetId, status);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult batchInsert(String cabinetId, String batchBox) {
        if (StringUtils.isEmpty(batchBox)) {
            return ExtResult.failResult("操作失败");
        }
        String[] rows = batchBox.split("\r\n");
        Set<String> stringSet = new HashSet<String>();
        for (String r : rows) {
            String[] batchBoxArray = r.split(",");
            if (batchBoxArray == null || (batchBoxArray != null && (batchBoxArray.length < 2 || batchBoxArray.length > 2))) {
                return ExtResult.failResult("格式不正确");
            }
            stringSet.add(r);
        }
        Cabinet cabinet = cabinetMapper.find(cabinetId);
        if (cabinet == null || cabinet.getId() == null) {
            return ExtResult.failResult("分柜未绑定站点");
        }
        if (cabinet == null || cabinet.getChargeFullVolume() == null) {
            return ExtResult.failResult("换电柜不存在或未设置充满电量");
        }
        CabinetBox cabinetBox = new CabinetBox();
        cabinetBox.setCabinetId(cabinetId);
        cabinetBox.setIsActive(ConstEnum.Flag.TRUE.getValue());
        cabinetBox.setBoxStatus(CabinetBox.BoxStatus.EMPTY.getValue());
        cabinetBox.setSubtype(cabinet.getSubtype());
        cabinetBox.setCabinetId(cabinet.getId());
        cabinetBox.setIsOnline(cabinet.getIsOnline());
        cabinetBox.setChargeFullVolume(cabinet.getChargeFullVolume());
        cabinetBox.setIsOpen(0);
        for (String s : stringSet) {
            String[] box = s.split(",");
            if (box != null && box.length > 1) {
                cabinetBox.setBoxNum(box[0]);
                cabinetBox.setType(Integer.parseInt(box[1]));
                if (cabinetBoxMapper.find(cabinetBox.getCabinetId(), cabinetBox.getBoxNum()) == null) {
                    cabinetBoxMapper.insert(cabinetBox);
                }

            }
        }
        return ExtResult.successResult();
    }

    public List<CabinetBox> findBySubcabinet(String id) {
        List<CabinetBox> boxes = cabinetBoxMapper.findBySubcabinet(id);
        for(CabinetBox box:boxes){
            if(StringUtils.isNotEmpty(box.getBatteryId())){
                Battery battery = batteryService.find(box.getBatteryId());
                box.setBattery(battery);
            }
            CabinetCharger cabinetCharger = cabinetChargerMapper.findByCabinetBox(box.getCabinetId(), box.getBoxNum());
            box.setCabinetCharger(cabinetCharger);
            if (box.getForbiddenCause() != null && box.getForbiddenCause().length() > 7) {
                String forbiddenCause = box.getForbiddenCause().substring(0,7) + "...";
                box.setForbiddenCause(forbiddenCause);
            }
        }
        return boxes;
    }


    public Page findByIsActiveAllPage(CabinetBox cabinetBox) {
        Page page = cabinetBox.buildPage();
        cabinetBox.setBeginIndex(page.getOffset());
        List<CabinetBox> list = cabinetBoxMapper.findByIsActiveAllPage(cabinetBox);
        page.setTotalItems(cabinetBoxMapper.findByIsActiveAllCount(cabinetBox));
        for (CabinetBox box: list) {
            if (box.getForbiddenCause() != null && box.getForbiddenCause().length() > 7) {
                String forbiddenCause = box.getForbiddenCause().substring(0,7) + "...";
                box.setForbiddenCause(forbiddenCause);
            }
            if (org.apache.commons.lang3.StringUtils.isNotBlank(box.getCabinetId())){
                Cabinet cabinet = cabinetMapper.find(box.getCabinetId());
                if(cabinet!=null&&cabinet.getAgentId()!=null){
                    cabinet.setAgentName(agentMapper.find(cabinet.getAgentId()).getAgentName());
                }
                box.setCabinet(cabinet);
            }
        }
        page.setResult(list);
        return page;
    }
    public CabinetBox findById(String cabinetId, String boxNum) {
        CabinetBox box= cabinetBoxMapper.find(cabinetId, boxNum);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(box.getCabinetId())){
            Cabinet cabinet = cabinetMapper.find(box.getCabinetId());
            if(cabinet!=null&&cabinet.getAgentId()!=null){
                cabinet.setAgentName(agentMapper.find(cabinet.getAgentId()).getAgentName());
            }
            box.setCabinet(cabinet);
        }
        return box;
    }


}
