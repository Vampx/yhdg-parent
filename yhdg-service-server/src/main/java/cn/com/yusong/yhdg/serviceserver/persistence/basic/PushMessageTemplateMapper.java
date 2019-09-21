package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.PushMessageTemplate;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by ruanjian5 on 2017/12/21.
 */
public interface PushMessageTemplateMapper extends MasterMapper {
    public PushMessageTemplate find(@Param("id") Integer id );
}
