package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.KeepTakeOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/11.
 */
public interface KeepTakeOrderMapper extends MasterMapper {

    public List<KeepTakeOrder> findList(@Param("dispatcherId") long dispatcherId,@Param("offset") int offset,@Param("limit") int limit);

    public KeepTakeOrder find( @Param("id") String id);
}
