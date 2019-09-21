package cn.com.yusong.yhdg.common.domain.basic;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 充值优惠
 */
@Setter
@Getter
public class CustomerDepositGift implements Serializable {

   /* public static int gift(List<CustomerDepositGift> giftList, float money) {
        int size = giftList.size();
        for (int i = size - 1; i >= 0; i--) {
            CustomerDepositGift e = giftList.get(i);
            if (money >= e.getMoney()) {
                return e.getGift();
            }
        }
        return 0;
    }*/

    Integer partnerId;
    Integer money;
    Integer gift;//优惠
}
