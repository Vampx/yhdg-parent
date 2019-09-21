package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 客户信息
 */
@Setter
@Getter
public class CustomerManualAuthRecord extends LongIdEntity {

    Long customerId;
    Integer partnerId;
    String fullname;
    String mobile;
    String idCard;
    String idCardFace;
    String idCardRear;
    String authFacePath;
    Date auditTime;
    String auditMemo;
    String auditUser;
    Integer status;
    Date createTime;
    public enum Status{
        NOT(1,"待审核"), APPROVAL(2,"审核通过"),FAILED(3,"审核未通过");

        private final int value;
        private final String name;

        Status(int value, String name) {
            this.value = value;
            this.name = name;
        }
        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (Status e : Status.values()) {
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
    public String getStatusName() {
        if(status != null) {
            return Status.getName(status);
        }
        return "";
    }
    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getAuditTime() {
        return auditTime;
    }

}
