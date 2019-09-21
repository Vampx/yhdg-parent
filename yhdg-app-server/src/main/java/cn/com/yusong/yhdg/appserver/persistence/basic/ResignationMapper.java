package cn.com.yusong.yhdg.appserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.Resignation;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ResignationMapper extends MasterMapper {
    public Resignation findList(long customerId);

    public int cancel(@Param("customerId") long customerId, @Param("id") long id, @Param("formState") Integer formState, @Param("toState") Integer toState);

    public int insert(Resignation resignation);

}
