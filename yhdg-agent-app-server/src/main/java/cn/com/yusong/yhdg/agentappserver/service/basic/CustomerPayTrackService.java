package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.CustomerPayTrackMapper;
import cn.com.yusong.yhdg.common.domain.basic.CustomerPayTrack;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomerPayTrackService {

    @Autowired
    CustomerPayTrackMapper customerPayTrackMapper;

    public RestResult findList(Integer agentId, Long customerId, int offset, int limit) {
        List<CustomerPayTrack> list = customerPayTrackMapper.findList(agentId, customerId, offset, limit);

        List<Map> data = new ArrayList<Map>();
        for (CustomerPayTrack customerPayTrack : list) {
            NotNullMap line = new NotNullMap();
            line.put("id", customerPayTrack.getId());
            line.put("fullname", customerPayTrack.getCustomerFullname());
            line.put("mobile", customerPayTrack.getCustomerMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
            line.put("trackType", customerPayTrack.getTrackType());
            line.put("memo", customerPayTrack.getMemo());
            line.putDateTime("createTime", customerPayTrack.getCreateTime());
            data.add(line);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

}
