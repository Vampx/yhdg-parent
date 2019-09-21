package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface UserMapper extends MasterMapper {
    String hasRecordByProperty(@Param("property") String property, @Param("value") Object value);
    List<User> findByAgent(@Param("agentId") Integer agentId);
    User findByUsername(String username);
    User findByProtected(@Param("agentId") Integer agentId);
    User findByShopProtected(@Param("shopId") String shopId);
    User findByStationProtected(@Param("stationId") String stationId);
    User findByEstateProtected(@Param("estateId") long estateId);
    List<User> findListByStationId(@Param("stationId") String stationId);
    int findUnique(@Param("id") Long id, @Param("loginName") String loginName, @Param("accountType") Integer accountType);
    User find(long id);
    User findByLoginInfo(User user);
    int findPageCount(User user);
    List<User> findPageResult(User user);
    int insert(User user);
    int updatePassword(@Param("id") long id, @Param("oldPassword") String oldPassword, @Param("newPassword") String newPassword);
    int updateLoginTime(@Param("id") long id, @Param("lastLoginTime") Date lastLoginTime);
    int update(User user);
    int delete(long id);
}
