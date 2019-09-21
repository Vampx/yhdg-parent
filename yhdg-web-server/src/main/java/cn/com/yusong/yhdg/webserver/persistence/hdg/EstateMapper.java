package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Estate;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zhoub on 2017/10/24.
 */
public interface EstateMapper extends MasterMapper {
    Estate find(long id);

    Estate findByPayPeopleMobile(String payPeopleMobile);

    String findMaxId(String id);

    int findPageCount(Estate estate);

    List<Estate> findPageResult(Estate estate);

    int findUnboundPageCount(Estate estate);

    List<Estate> findUnboundPageResult(Estate estate);

    int findCountByAgent(@Param("agentId") Integer agentId);

    List<Estate> findByAgent(@Param("agentId") Integer agentId);

    List<Estate> findAll();

    Shop findUnique(long id);

    int insert(Estate entity);

    int update(Estate entity);


    int updateBalance(@Param("id") long id, @Param("balance") long balance);

    int delete(long id);


    int updatePayPeople(@Param("id") long id, @Param("payPeopleName") String payPeopleName,
                        @Param("payPeopleMpOpenId") String payPeopleMpOpenId, @Param("payPeopleFwOpenId") String payPeopleFwOpenId,
                        @Param("payPeopleMobile") String payPeopleMobile, @Param("payPassword") String payPassword);
}
