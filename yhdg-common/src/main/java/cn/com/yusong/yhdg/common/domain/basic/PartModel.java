package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.*;

/**
 * 角色模板
 */
@Setter
@Getter
public class PartModel extends IntIdEntity {
    public enum PartModelType {
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

        PartModelType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (PartModelType e : PartModelType.values()) {
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

    String partModelName;//角色模板名称
    Integer partModelType;/*1.平台用户角色模板 2.运营商用户角色模板 3.门店用户角色模板 4.运营公司用户角色模板 5.物业公司用户角色模板 6.站点用户角色模板 7.站点拓展人员用户角色模板 8.发货员用户角色模板 10.拉新用户角色模板*/
    Integer agentId;//运营商id
    String shopId;//门店id
    String agentCompanyId;//运营公司id
    Integer estateId;//物业id
    String stationId;//站点id
    String memo;//备注
    Date createTime;

    @Transient
    String agentName;
    String permIds;
    List<String> permIdList = new ArrayList<String>();

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
