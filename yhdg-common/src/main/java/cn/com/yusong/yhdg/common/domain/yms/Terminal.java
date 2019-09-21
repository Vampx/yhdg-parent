package cn.com.yusong.yhdg.common.domain.yms;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.StringIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *   终端
 */
@Setter
@Getter
public class Terminal extends StringIdEntity {

    public enum LogLevel {
        NULL(0, "无"),
        VERBOSE(2, "追踪"),
        DEBUG(3, "调试"),
        INFO(4, "信息"),
        WARN(5, "警告"),
        ERROR(6, "错误"),
        ASSERT(7, "断言"),
        ;

        static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for(LogLevel e : LogLevel.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        private final int value;
        private final String name;

        LogLevel(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static String getName(int value) {
            return map.get(value);
        }
    }

    String version; //版本
    String uid; //访问控制
    Long strategyId; //策略id
    String heartPlaylistVersion; //心跳播放列表版本号
    Integer heartPlaylistId;//心跳播放列表ID
    Integer playlistId; //播放列表id
    String playlistVersion; //播放列表版本
    Integer isOnline;
    Date heartTime;

    @Transient
    Integer agentId;
    @Transient
    Date date ;
    @Transient
    String strategyName; //策略名称
    @Transient
    String descendant;
    @Transient
    String playlistName; //播放列表名称
    @Transient
    String refreshTime;
    @Transient
    Integer groupId; //换电柜分组
    @Transient
    String cabinetName; //换电柜名称
    @Transient
    String address; //换电柜地址
    @Transient
    String cabinetId; //换电柜id
    @Transient
    String agentName; //运营商名称


    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getDate() {
        return date;
    }


    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getHeartTime() {
        return heartTime;
    }


}