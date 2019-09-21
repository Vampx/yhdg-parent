package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.domain.hdg.StationBizUser;
import cn.com.yusong.yhdg.common.domain.hdg.VipPacketPeriodPrice;
import cn.com.yusong.yhdg.common.domain.hdg.VipPriceCabinet;
import cn.com.yusong.yhdg.common.domain.hdg.VipPriceStation;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.persistence.basic.UserMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.StationBizUserMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service
public class StationBizUserService extends AbstractService {

    @Autowired
    StationBizUserMapper stationBizUserMapper;
    @Autowired
    UserMapper userMapper;

    public List<StationBizUser> findListByStationId(String stationId) {
        return stationBizUserMapper.findListByStationId(stationId);
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult createOrUpdate(StationBizUser entity) {

     /*   List<StationBizUser> stationBizUserList = stationBizUserMapper.findListByStationId(entity.getStationId());
        if (stationBizUserList.size() > 0) {
            stationBizUserMapper.deleteByStationId(entity.getStationId());
        }*/

        String[] stationBizUserIdArr = entity.getIds().split(",");
        for (String stationBizUserId : stationBizUserIdArr) {
            StationBizUser user = stationBizUserMapper.find(entity.getStationId(), Long.valueOf(stationBizUserId));
            if (user != null) {
                return ExtResult.failResult("包含已存在的拓展人员");
            }
            StationBizUser stationBizUser = new StationBizUser();
            stationBizUser.setStationId(entity.getStationId());
            stationBizUser.setUserId(Long.valueOf(stationBizUserId));
            stationBizUser.setCreateTime(new Date());
            stationBizUserMapper.insert(stationBizUser);
        }
        List<StationBizUser> bizUserList = stationBizUserMapper.findListByStationId(entity.getStationId());

        StringBuilder sb = new StringBuilder();
        for (StationBizUser stationBizUser : bizUserList) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            User user = userMapper.find(stationBizUser.getUserId());
            sb.append(user.getFullname() != null ? user.getFullname() : user.getLoginName());
        }
        String fullnameList = sb.toString();
        return DataResult.successResult(fullnameList);
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(String stationId) {
        List<StationBizUser> stationBizUserList = stationBizUserMapper.findListByStationId(stationId);
        if (stationBizUserList.size() > 0) {
            stationBizUserMapper.deleteByStationId(stationId);
        }
        return ExtResult.successResult();
    }

}
