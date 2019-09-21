package cn.com.yusong.yhdg.webserver.persistence.zc;

import cn.com.yusong.yhdg.common.domain.zc.GroupOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface GroupOrderMapper extends MasterMapper {
    int findCountByPriceId(@Param("rentPriceId") Long rentPriceId);
    GroupOrder find(@Param("id") String id);
    List<GroupOrder> findByRentPriceId(@Param("rentPriceId") Long rentPriceId);
    int findPageCount(GroupOrder groupOrder);
    List<GroupOrder> findPageResult(GroupOrder groupOrder);
    int insert(GroupOrder groupOrder);
    int update(GroupOrder groupOrder);
    int delete(String id);
    int updateStatus(@Param("id") String id, @Param("status") int status,
                     @Param("refundMoney") int refundMoney,
                     @Param("refundOperator") String refundOperator,
                     @Param("refundTime") Date refundTime,
                     @Param("refundPhoto") String refundPhoto,
                     @Param("memo") String memo,
                     @Param("handleTime") Date handleTime);
    int updatePayTime(@Param("id") String id, @Param("payTime") Date payTime);
}
