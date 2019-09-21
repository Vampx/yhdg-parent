package cn.com.yusong.yhdg.webserver.service.basic;
import cn.com.yusong.yhdg.common.domain.basic.PartPerm;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.persistence.basic.PartPermMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PartPermService extends AbstractService {

    @Autowired
    PartPermMapper partPermMapper;

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult create(PartPerm partPerm) {
        partPermMapper.insert(partPerm);
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult delete(Integer id) {
        partPermMapper.delete(id);
        return ExtResult.successResult();
    }

}
