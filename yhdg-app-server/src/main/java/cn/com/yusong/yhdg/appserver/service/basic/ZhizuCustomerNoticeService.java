package cn.com.yusong.yhdg.appserver.service.basic;
import cn.com.yusong.yhdg.appserver.persistence.basic.DeviceMapper;
import cn.com.yusong.yhdg.appserver.persistence.basic.ZhizuCustomerNoticeMapper;
import cn.com.yusong.yhdg.common.domain.basic.Device;
import cn.com.yusong.yhdg.common.domain.basic.ZhizuCustomerNotice;
import cn.com.yusong.yhdg.common.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ZhizuCustomerNoticeService extends AbstractService {
    @Autowired
    ZhizuCustomerNoticeMapper zhizuCustomerNoticeMapper;

    public ZhizuCustomerNotice find(String cabinetId) {
        return zhizuCustomerNoticeMapper.find(cabinetId);
    }

    public int insert(ZhizuCustomerNotice entity) {
        return zhizuCustomerNoticeMapper.insert(entity);
    }

    public int delete(String cabinetId) {
        return zhizuCustomerNoticeMapper.delete(cabinetId);
    }
}
