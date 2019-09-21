
package cn.com.yusong.yhdg.appserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/4.
 */
public interface UserMapper extends MasterMapper {
    public User find(@Param("id") long id);

    public User findByMobile(@Param("mobile") String mobile);

    public User findByLoginName(@Param("loginName") String loginName);

    public int updateLoginTime(@Param("id") long id, @Param("lastLoginTime") Date lastLoginTime);

    public int updatePassword(@Param("id") long id, @Param("oldPassword") String oldPassword, @Param("newPassword") String newPassword);

    public int updatePassword2(@Param("id") long id, @Param("newPassword") String newPassword);

    public int updateMobile(@Param("id") long id, @Param("mobile") String mobile);

    public int updateInfo(@Param("id") long id, @Param("photoPath") String photoPath);

    public int updatePushToken(@Param("id") long id, @Param("pushType") Integer pushType, @Param("pushToken") String pushToken);
}

