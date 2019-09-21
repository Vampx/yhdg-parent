package cn.com.yusong.yhdg.serviceserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ruanjian5 on 2017/12/26.
 */
public interface UserMapper extends MasterMapper {
    public List<User> findAll();
    public User find(@Param("id")Long id);
    List<User> findByAgent(@Param("agentId") Integer agentId,
                           @Param("accountType") Integer accountType,
                           @Param("id") Integer id,
                           @Param("weixinmpId") Integer weixinmpId
    );
}
