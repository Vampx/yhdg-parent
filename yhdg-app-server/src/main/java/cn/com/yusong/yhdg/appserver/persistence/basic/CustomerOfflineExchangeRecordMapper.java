
package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerOfflineExchangeRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by ruanjian5 on 2017/11/4.
 */
public interface CustomerOfflineExchangeRecordMapper extends MasterMapper {
    CustomerOfflineExchangeRecord find(@Param("id") int id);


    int insert(CustomerOfflineExchangeRecord customerOfflineExchangeRecord);

}

