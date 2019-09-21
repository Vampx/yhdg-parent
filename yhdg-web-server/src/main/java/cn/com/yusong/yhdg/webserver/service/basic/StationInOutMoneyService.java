package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.StationInOutMoney;
import cn.com.yusong.yhdg.common.domain.hdg.Station;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.StationInOutMoneyMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.StationMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationInOutMoneyService extends AbstractService{

    @Autowired
    StationInOutMoneyMapper stationInOutMoneyMapper;
    @Autowired
    StationMapper stationMapper;


    public Page findPage(StationInOutMoney search) {
        Page page = search.buildPage();
        page.setTotalItems(stationInOutMoneyMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<StationInOutMoney> pageResult = stationInOutMoneyMapper.findPageResult(search);
        for (StationInOutMoney stationInOutMoney : pageResult) {
            if (stationInOutMoney.getStationId() != null) {
                Station station = stationMapper.find(stationInOutMoney.getStationId());
                if (station != null) {
                    stationInOutMoney.setStationName(station.getStationName());
                }
            }
        }
        page.setResult(pageResult);
        return page;
    }

    public StationInOutMoney find(Long id) {
        StationInOutMoney shopInOutMoney = stationInOutMoneyMapper.find(id);
        if (shopInOutMoney.getStationId() != null) {
            Station station = stationMapper.find(shopInOutMoney.getStationId());
            if (station != null) {
                shopInOutMoney.setStationName(station.getStationName());
            }
        }
        return shopInOutMoney;
    }
}
