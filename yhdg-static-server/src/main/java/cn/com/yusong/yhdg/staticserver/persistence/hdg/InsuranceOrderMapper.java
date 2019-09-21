package cn.com.yusong.yhdg.staticserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.InsuranceOrder;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * Created by ruanjian5 on 2017/11/6.
 */
public interface InsuranceOrderMapper extends MasterMapper {
    InsuranceOrder find(String id);

    int payOk(@Param("id") String id, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus, @Param("payTime") Date payTime);

    int updatePaidMoney(@Param("id") String id, @Param("recordId") long recordId, @Param("status") int status);

    int updateCompleteInstallmentTime(@Param("id") String id,
                                      @Param("completeInstallmentTime") Date completeInstallmentTime,
                                      @Param("payTime") Date payTime);

}
