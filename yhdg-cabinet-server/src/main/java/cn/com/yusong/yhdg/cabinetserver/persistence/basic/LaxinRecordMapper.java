package cn.com.yusong.yhdg.cabinetserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.LaxinRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface LaxinRecordMapper extends MasterMapper {

    public int insert(LaxinRecord laxinRecord);
}
