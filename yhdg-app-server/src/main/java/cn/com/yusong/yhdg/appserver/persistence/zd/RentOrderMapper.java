package cn.com.yusong.yhdg.appserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.RentOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

/**
 * Created by ruanjian5 on 2017/11/6.
 */
public interface RentOrderMapper extends MasterMapper {

    public RentOrder find(String id);
    public int insert(RentOrder rentOrder);

}
