package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class CustomerMultiPayDetail extends LongIdEntity {
    public enum Status {
        NOT_PAY(1, "未支付"),
        HAVE_PAY(2, "已支付"),
        ;

        private final int value;
        private final String name;

        private Status(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (CustomerMultiPayDetail.Status e : CustomerMultiPayDetail.Status.values()) {
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
    private Integer partnerId; /*平台id*/
    private Long orderId;
    private Integer agentId;/*运营商id*/
    private String agentName;
    private String agentCode;/*运营商编号*/
    private Integer money;
    private Integer payType;
    private Date payTime;
    private Integer status;
    private Long customerId;                //客户id
    private String customerMobile;
    private String customerFullname;
    private Date createTime;

    public String getPayTypeName() {
        if(payType != null) {
            return ConstEnum.PayType.getName(payType);
        }
        return "";
    }

    public String getStatusName(){
        if (status != null){
            return CustomerMultiPayDetail.Status.getName(status);
        }
        return "";
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getPayTime() {
        return payTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}