package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 推送消息模板详情
 */
@Setter
@Getter
public class MpPushMessageTemplate extends IntIdEntity {
    public Map<String, String> colorMap = new HashMap<String, String>();

    Integer weixinmpId;
    String templateName;
    String variable;
    String mpCode;
    Integer isActive;
    String receiver;
    String memo;

    String weixinmpName;
    Long userId;

    public static Map<String, String> toDetailMap(String[] variable, List<MpPushMessageTemplateDetail> detailList) {
        Map<String, String> map = new HashMap<String, String>();
        for(MpPushMessageTemplateDetail detail : detailList) {
            map.put(detail.getId(), detail.replace(variable));
        }
        return map;
    }
}
