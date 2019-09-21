package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.domain.hdg.PushOrderMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 同步用户信息
 *
 */
@Setter
@Getter
public class SyncCustomerInfo extends LongIdEntity {

    String mpOpenId;
    String fwOpenId;
    String nickname; /*昵称*/
    String photoPath;/*照片路径*/
    String mobile; /*手机号*/
    String fullname;/*姓名*/
    String idCard; /*身份证卡号*/
    Integer company; /*所属单位*/
    Integer batteryType;/*电池型号*/
    String idCardFace;
    String idCardRear;
    Integer sendStatus;/*推送状态*/
    Date handleTime;/*发送成功时间*/
    Integer resendNum;/*重发次数*/
    Date createTime;

    public enum SendStatus {
        NOT(1, "未发"),
        OK(2, "成功"),
        FAIL(3, "失败"),;

        private final int value;
        private final String name;

        SendStatus(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (PushOrderMessage.SendStatus e : PushOrderMessage.SendStatus.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        public static String getName(int value) {
            return map.get(value);
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
