package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.PublicNotice;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

/**
 * Created by ruanjian5 on 2017/12/27.
 */
public interface PublicNoticeMapper extends MasterMapper {
    public PublicNotice find(long id);
}
