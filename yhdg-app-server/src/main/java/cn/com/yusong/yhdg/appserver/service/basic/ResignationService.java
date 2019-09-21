package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.basic.ResignationMapper;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Feedback;
import cn.com.yusong.yhdg.common.domain.basic.Resignation;
import org.apache.tools.ant.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ResignationService {
    @Autowired
    ResignationMapper resignationMapper;

    public RestResult findList(long customerId) {
        Resignation resignation = resignationMapper.findList(customerId);
        Map map = new HashMap();
        if (resignation != null) {
            map.put("id", resignation.getId());
            map.put("state", resignation.getState());
            map.put("content", resignation.getContent());
            map.put("reason", resignation.getReason());
            map.put("handleTime", resignation.getHandleTime() == null ? "" : DateUtils.format(resignation.getHandleTime(), Constant.DATE_TIME_FORMAT));
            map.put("createTime", DateUtils.format(resignation.getCreateTime(), Constant.DATE_TIME_FORMAT));
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);

    }

    public RestResult cancel(Long customerId, Long id) {
        if (resignationMapper.cancel(customerId, id, Resignation.State.AUDIT.getValue(), Resignation.State.CANCEL.getValue()) == 0) {
            return RestResult.result(RespCode.CODE_1.getValue(), "取消失败");
        }
        return RestResult.SUCCESS;
    }

    public RestResult insert(Resignation resignation) {
        if (resignationMapper.insert(resignation) == 0) {
            return RestResult.result(RespCode.CODE_1.getValue(), "添加失败");
        }
        Map map = new HashMap();
        map.put("id", resignation.getId());
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }
}
