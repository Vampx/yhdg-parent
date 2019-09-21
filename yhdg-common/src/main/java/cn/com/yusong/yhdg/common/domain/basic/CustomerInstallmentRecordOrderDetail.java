package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.PageEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 欠款订单明细
 */
@Setter
@Getter
public class CustomerInstallmentRecordOrderDetail extends PageEntity {

    Long recordId;
    Integer num;
    Integer sourceType;
    String sourceId;
    Integer category; /*1 换电 2 租电*/
    Integer money;
}
