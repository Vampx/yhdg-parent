package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ShopAddressCorrection;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopAddressCorrectionMapper extends MasterMapper {
    int findPageCount(ShopAddressCorrection shopAddressCorrection);

    List<ShopAddressCorrection> findPageResult(ShopAddressCorrection shopAddressCorrection);

    ShopAddressCorrection find(long id);

    int updateStatus(@Param("id") long id, @Param("status") int status);

    int delete(long id);

    int deleteByCustomerId(@Param("customerId") long customerId);

}
