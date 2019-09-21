package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerManualAuthRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface CustomerManualAuthRecordMapper extends MasterMapper {

    int insert(CustomerManualAuthRecord customerManualAuthRecord);

}
