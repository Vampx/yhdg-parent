
package cn.com.yusong.yhdg.appserver.service.basic;


import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.basic.UserMapper;

import cn.com.yusong.yhdg.appserver.persistence.hdg.CabinetMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.FaultLogMapper;
import cn.com.yusong.yhdg.appserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.appserver.service.hdg.FaultLogService;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.FaultLog;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by ruanjian5 on 2017/11/4.
 */
@Service
public class UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    FaultLogService faultLogService;


    public User find(long id) {
        return userMapper.find(id);
    }

    public User findByLoginName(String loginName) {
        return userMapper.findByLoginName(loginName);
    }

    public User findByMobile(String mobile) {
        return userMapper.findByMobile(mobile);
    }

    public int updatePassword(long id, String oldPassword, String newPassword) {
        return userMapper.updatePassword(id, oldPassword, newPassword);
    }

    public int updatePassword2(long id, String newPassword) {
        return userMapper.updatePassword2(id, newPassword);
    }

    public int updateMobile(long id, String mobile) {
        return userMapper.updateMobile(id, mobile);
    }

    public int updateLoginTime(long id, Date loginTime) {
        return userMapper.updateLoginTime(id, loginTime);
    }

    public RestResult getIndex(long userId){
        int cabinetCount = cabinetMapper.findCountByDispatcher(userId);
        int batteryCount = cabinetMapper.findBatteryCountByDispatcher(userId);
        int waitTakeBatteryCount = cabinetMapper.findNotFullBatteryCountByDispatcher(userId);
        int faultCount = faultLogService.findCountByDispatcher(userId, FaultLog.Status.WAIT_PROCESS.getValue());

        List<Map> list = new ArrayList<Map>();
        List<Cabinet> cabinetList = cabinetMapper.findList(userId);

        if (cabinetList != null) {
            for (Cabinet cabinet : cabinetList) {
                Map map = new HashMap();
                map.put("id", cabinet.getId());
                map.put("cabinetName", cabinet.getCabinetName());
                map.put("address", cabinet.getAddress());
                map.put("lng", cabinet.getLng());
                map.put("lat", cabinet.getLat());
                map.put("batteryCount", cabinetMapper.findBatteryCountByCabinet(cabinet.getId()));
                map.put("waitTakeBatteryCount", cabinetMapper.findNotFullBatteryCountByCabinet(cabinet.getId()));
                map.put("onlineSubcabinetCount", cabinetMapper.findOnlineSubcabinetCountByCabinet(cabinet.getId()));
                map.put("offlineSubcabinetCount", cabinetMapper.findOfflineSubcabinetCountByCabinet(cabinet.getId()));

                Map<String, Integer> map1 = faultLogService.findCabenitCount(userId, cabinet.getId(), FaultLog.Status.WAIT_PROCESS.getValue());

                map.put("faultCount", map1.get("faultCount"));
                map.put("faultLevel", map1.get("faultLevel"));
                list.add(map);
            }
        }
        Map map = new HashMap();
        map.put("cabinetCount", cabinetCount);
        map.put("batteryCount", batteryCount);
        map.put("waitTakeBatteryCount", waitTakeBatteryCount);
        map.put("faultCount", faultCount);
        map.put("cabinetList", list);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), "", map);

    }

    public int updateInfo(long id, String photoPath) {
        if(StringUtils.isNotEmpty(photoPath)) {
            return userMapper.updateInfo(id, photoPath);
        }
        return 1;
    }

    public RestResult updatePushToken(long id, Integer pushType, String pushToken) {

        if (userMapper.updatePushToken(id, pushType, pushToken) == 0) {
            return RestResult.result(RespCode.CODE_1.getValue(), null);
        }
        return RestResult.result(RespCode.CODE_0.getValue(), null);
    }

}


