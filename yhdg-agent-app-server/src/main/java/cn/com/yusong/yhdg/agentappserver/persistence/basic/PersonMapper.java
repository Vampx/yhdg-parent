package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Person;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface PersonMapper extends MasterMapper {
    Person findByMobile(@Param("mobile") String mobile);
    Person find(@Param("id") long id);
    int updatePassword(@Param("id") long id, @Param("oldPassword") String oldPassword, @Param("newPassword") String newPassword);
    int updateLoginTime(@Param("id") Long id, @Param("lastLoginTime") Date lastLoginTime);
}
