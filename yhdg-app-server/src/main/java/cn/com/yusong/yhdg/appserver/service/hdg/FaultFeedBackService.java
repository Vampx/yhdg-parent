package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.FaultFeedBackMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.hdg.FaultFeedback;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FaultFeedBackService extends AbstractService {
    @Autowired
    FaultFeedBackMapper faultFeedbackMapper;
    @Autowired
    CustomerMapper customerMapper;

    public FaultFeedback find(Long id) {
        return faultFeedbackMapper.find(id);
    }

    public RestResult view(Long id) {
        FaultFeedback faultFeedback = faultFeedbackMapper.find(id);
        if(faultFeedback==null){
            return RestResult.result(RespCode.CODE_2.getValue(), "记录不存在");
        }
        NotNullMap line = new NotNullMap();
        line.putString("cabinetId", faultFeedback.getCabinetId());
        line.putString("cabinetName", faultFeedback.getCabinetName());
        line.putString("cabinetAddress", faultFeedback.getCabinetAddress());
        line.putString("batteryId", faultFeedback.getBatteryId());
        line.putString("handlerName", faultFeedback.getHandlerName());
        line.putString("handleTime", faultFeedback.getHandleTime() == null ? null : DateFormatUtils.format(faultFeedback.getHandleTime(), Constant.DATE_TIME_FORMAT));
        line.putString("handleResult", faultFeedback.getHandleResult());
        line.putInteger("handleStatus", faultFeedback.getHandleStatus());
        line.putInteger("faultType", faultFeedback.getFaultType());
        line.putString("faultName", faultFeedback.getFaultName());
        line.putString("memo", faultFeedback.getMemo());
        line.putString("photoPath1", staticPath(faultFeedback.getPhotoPath1()));
        line.putString("photoPath2", staticPath(faultFeedback.getPhotoPath2()));
        line.putString("photoPath3", staticPath(faultFeedback.getPhotoPath3()));
        line.putString("photoPath4", staticPath(faultFeedback.getPhotoPath4()));
        line.putString("photoPath5", staticPath(faultFeedback.getPhotoPath5()));
        line.putString("photoPath6", staticPath(faultFeedback.getPhotoPath6()));
        return RestResult.dataResult(RespCode.CODE_0.getValue(), "", line);
    }

    public RestResult getListByCustomer(long customerId, int offset, int limit) {
        Customer customer = customerMapper.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }

        List<FaultFeedback> listByCustomer = faultFeedbackMapper.findListByCustomer(customerId, offset, limit);
        List<Map> list = new ArrayList<Map>();

        if (!listByCustomer.isEmpty()) {
            for (FaultFeedback faultFeedback : listByCustomer) {
                NotNullMap line = new NotNullMap();
                line.putLong("id", faultFeedback.getId());
                line.putInteger("faultType", faultFeedback.getFaultType());
                line.putString("faultName", faultFeedback.getFaultName());
                line.putString("memo", faultFeedback.getMemo());
                line.putInteger("handleStatus", faultFeedback.getHandleStatus());
                line.putString("cabinetAddress", faultFeedback.getCabinetAddress());
                list.add(line);
            }
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), "", list);
    }

    public RestResult insert(FaultFeedback faultFeedback) {
        faultFeedback.setCreateTime(new Date());
        faultFeedbackMapper.insert(faultFeedback);
        return RestResult.SUCCESS;
    }
}
