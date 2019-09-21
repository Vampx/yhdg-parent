package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.basic.FeedbackMapper;
import cn.com.yusong.yhdg.common.domain.basic.Feedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedbackService {
    @Autowired
    FeedbackMapper feedbackMapper;

    public RestResult insert(Feedback feedback) {

        if ( feedbackMapper.insert(feedback) == 0 ){
            return RestResult.result(RespCode.CODE_1.getValue(),"添加失败");
        }
        return RestResult.SUCCESS;
    }
}
