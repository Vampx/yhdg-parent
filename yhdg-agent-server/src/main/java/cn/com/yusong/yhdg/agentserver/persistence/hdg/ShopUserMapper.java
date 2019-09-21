package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ShopUser;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by zhoub on 2017/10/24.
 */
public interface ShopUserMapper extends MasterMapper {
    ShopUser find(@Param("shopId") String shopId, @Param("userId") Integer userId);
    List<ShopUser> findByShop(@Param("shopId") String shopId);
    int findPageCount(ShopUser shopUser);
    List<ShopUser> findPageResult(ShopUser shopUser);
    int insert(ShopUser shopUser);
    int update(@Param("fromUserId") Integer fromUserId, @Param("toUserId") Integer toUserId, @Param("shopId") String shopId, @Param("createTime") Date createTime);
    int delete(@Param("shopId") String shopId, @Param("userId") Integer userId);
}
