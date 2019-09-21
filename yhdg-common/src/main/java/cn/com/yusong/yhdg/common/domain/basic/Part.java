package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.*;

/**
 * 角色(新版)
 */
@Setter
@Getter
public class Part extends IntIdEntity {
    public enum PartType {
        PLATFORM(1, "平台用户"),
        AGENT(2, "运营商用户"),
        SHOP(3, "门店用户"),
        AGENT_COMPANY(4,"运营公司用户"),
        ESTATE(5,"物业公司用户"),
        STATION(6, "站点用户"),
        STATION_BIZ(7, "站点拓展人员用户"),
        EXPORT(8,"发货人员用户"),
        LAXIN(10,"拉新用户"),
        ;

        private final int value;
        private final String name;

        PartType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (PartType e : PartType.values()) {
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

    String partName;//角色名称
    Integer agentId;//运营商id
    String mobile;//手机号
    Integer partType;/*1.平台用户 2.运营商用户 3.门店用户 4.运营公司用户 5.物业公司用户 6.站点用户 7.站点拓展人员用户 8.发货员用户 10.拉新用户*/
    String memo;//备注
    Date createTime;

    @Transient
    String permIds;
    List<String> permIdList = new ArrayList<String>();
    String agentName;
    String partTypeName;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
