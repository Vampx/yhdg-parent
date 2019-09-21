
package cn.com.yusong.yhdg.agentappserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/4.
 */
public interface UserMapper extends MasterMapper {
    User find(@Param("id") long id);

    User findByMobile(@Param("mobile") String mobile);

    List<User> findListByMobile(@Param("mobile") String mobile, @Param("accountType") int accountType);

    List<User> findTypeUserList(@Param("mobile") String mobile);

    List<User> findShopListByMobile(@Param("mobile") String mobile, @Param("accountType") int accountType);

    List<User> findAgentCompanyListByMobile(@Param("mobile") String mobile, @Param("accountType") int accountType);

    List<User> findEstateListByMobile(@Param("mobile") String mobile, @Param("accountType") int accountType);

    List<User> findListByAgentId(@Param("agentId") Integer agentId,
                                 @Param("accountType") Integer accountType,
                                 @Param("shopId") String shopId,
                                 @Param("agentCompanyId")String agentCompanyId);

    List<User> findList(@Param("agentId") Integer agentId,
                        @Param("offset") int offset,
                        @Param("limit") int limit);

    int findAgentUserCount(@Param("agentId") Integer agentId);

    User findByLoginName(@Param("loginName") String loginName);

    int insert(User user);

    int update(User user);

    int updateLoginTime(@Param("id") long id, @Param("lastLoginTime") Date lastLoginTime);

    int updatePassword(@Param("id") long id, @Param("oldPassword") String oldPassword, @Param("newPassword") String newPassword);

    int updatePassword2(@Param("id") long id, @Param("newPassword") String newPassword);

    int updateMobile(@Param("id") long id, @Param("mobile") String mobile);

    int updateInfo(@Param("id") long id, @Param("photoPath") String photoPath);

    int updatePushToken(@Param("id") long id, @Param("pushType") Integer pushType, @Param("pushToken") String pushToken);

    int delete(long id);

}

