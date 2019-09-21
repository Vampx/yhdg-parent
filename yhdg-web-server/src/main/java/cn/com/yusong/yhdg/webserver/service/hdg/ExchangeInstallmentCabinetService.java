package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentCabinet;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentSetting;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.hdg.CabinetBoxMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.CabinetMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ExchangeInstallmentCabinetMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ExchangeInstallmentSettingMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ExchangeInstallmentCabinetService  extends AbstractService {


    @Autowired
    ExchangeInstallmentCabinetMapper exchangeInstallmentCabinetMapper;
    @Autowired
    ExchangeInstallmentSettingMapper exchangeInstallmentSettingMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    AreaCache areaCache;
    @Autowired
    CabinetBoxMapper cabinetBoxMapper;

    public ExchangeInstallmentCabinet findCabinetId( ExchangeInstallmentCabinet exchangeInstallmentCabinet){
        return exchangeInstallmentCabinetMapper.findCabinetId(exchangeInstallmentCabinet);
    }


    public Page findPage(Cabinet search) {
        Page page = search.buildPage();
        page.setTotalItems(cabinetMapper.findPageMentCabinetCount(search));
        search.setBeginIndex(page.getOffset());
        List<Cabinet> list = cabinetMapper.findPageMentCabinetResult(search);
        for (Cabinet cabinet : list) {
            cabinet.setAgentName(findAgentInfo(cabinet.getAgentId()).getAgentName());
            //获取禁用格口数
            int unActiveBoxNum = cabinetBoxMapper.findUnActiveNum(cabinet.getId());
            cabinet.setUnActiveBoxNum(unActiveBoxNum);
        }
        page.setResult(setAreaProperties(areaCache, list));
        return page;
    }

    public Page findPageInstallmentCabint(Cabinet search) {
        Page page = search.buildPage();
        page.setTotalItems(cabinetMapper.findPageMentCabinetCountNum(search));
        search.setBeginIndex(page.getOffset());
        List<Cabinet> list = cabinetMapper.findPageMentCabinetResultNum(search);
        for (Cabinet cabinet : list) {
            cabinet.setAgentName(findAgentInfo(cabinet.getAgentId()).getAgentName());
            //获取禁用格口数
            int unActiveBoxNum = cabinetBoxMapper.findUnActiveNum(cabinet.getId());
            cabinet.setUnActiveBoxNum(unActiveBoxNum);
        }
        page.setResult(setAreaProperties(areaCache, list));
        return page;
    }


    @Transactional(rollbackFor=Throwable.class)
    public ExtResult insert(Long settingId, String[] cabinetIds){

        if(settingId==null){
            return ExtResult.failResult("分期设置ID不能为空！");
        }
        if(cabinetIds.length==0){
            return ExtResult.failResult("添加设备ID为空！");
        }
        ExchangeInstallmentSetting exchangeInstallmentSetting = exchangeInstallmentSettingMapper.find(settingId);
        if(exchangeInstallmentSetting == null){
            return ExtResult.failResult("分期设置不存在！");
        }
        List<Cabinet> ids = cabinetMapper.findIds(cabinetIds);
        Integer num =0;
        for (Cabinet cabinet: ids) {
            ExchangeInstallmentCabinet exchangeInstallmentCabinet = new ExchangeInstallmentCabinet();
            exchangeInstallmentCabinet.setSettingId(settingId);
            exchangeInstallmentCabinet.setCabinetId(cabinet.getId());
            exchangeInstallmentCabinet.setCabinetName(cabinet.getCabinetName());
            ExchangeInstallmentCabinet cabinetId = exchangeInstallmentCabinetMapper.findCabinetId(exchangeInstallmentCabinet);
            if (cabinetId==null){
                num += exchangeInstallmentCabinetMapper.insert(exchangeInstallmentCabinet);
            }else{
                num += exchangeInstallmentCabinetMapper.update(exchangeInstallmentCabinet);
            }

        }
        if(ids.size() !=num){
            return ExtResult.failResult("设备添加失败！");
        }
        return ExtResult.successResult("设备添加成功");

    }

    public int update(ExchangeInstallmentCabinet cabinet){
        return exchangeInstallmentCabinetMapper.update(cabinet);
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult deleteCabinetId(ExchangeInstallmentCabinet exchangeInstallmentCabinet){
        if(StringUtils.isBlank(exchangeInstallmentCabinet.getCabinetId())) {
            return ExtResult.failResult("设备ID不能为空！");
        }
        if(exchangeInstallmentCabinet.getSettingId()==null){
            return ExtResult.failResult("分期设置ID不能为空！");
        }

        int i = exchangeInstallmentCabinetMapper.deleteCabinetId(exchangeInstallmentCabinet);
        if(i==0){
            return ExtResult.failResult("解绑设备失败！");
        }

        return  ExtResult.successResult("解绑设备成功");
    }

    public List<ExchangeInstallmentCabinet> findSettingId(Long settingId){
        return exchangeInstallmentCabinetMapper.findSettingId(settingId);
    }
}
