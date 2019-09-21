package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Person;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface PersonMapper extends MasterMapper {
    List<Person> findByAgent(@Param("agentId") Integer agentId);
    Person findByMobile(@Param("mobile") String mobile);
    int findUnique(@Param("id") Long id, @Param("mobile") String mobile);
    Person find(long id);
    Person findByLoginInfo(Person person);
    int findPageCount(Person person);
    List<Person> findPageResult(Person person);
    int insert(Person person);
    int updatePassword(@Param("id") long id, @Param("oldPassword") String oldPassword, @Param("newPassword") String newPassword);
    int updateLoginTime(@Param("id") long id, @Param("lastLoginTime") Date lastLoginTime);
    int update(Person person);
    int delete(long id);
}
