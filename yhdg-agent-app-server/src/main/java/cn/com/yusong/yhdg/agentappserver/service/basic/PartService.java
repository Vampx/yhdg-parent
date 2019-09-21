package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.persistence.basic.PartMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.PersonMapper;
import cn.com.yusong.yhdg.common.domain.basic.Part;
import cn.com.yusong.yhdg.common.domain.basic.Person;
import cn.com.yusong.yhdg.common.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartService extends AbstractService {

    @Autowired
    PartMapper partMapper;

    public List<Part> findList(String mobile, Integer partType) {
        return partMapper.findList(mobile, partType);
    }
}
