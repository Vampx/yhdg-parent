package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chen on 2017/10/28.
 */
@Getter
@Setter
public class RotateImage extends IntIdEntity {
    String imagePath;
    Integer orderNum;
    String url;
    Integer isShow;
    Date createTime;
    Integer type;
    Integer  sourceId;
    Integer category;

    public enum Type {
        GZH(1, "公众号配置"),
        SHH(2, "生活号配置"),
        APP(3,"APP配置"),
        XCX(4,"小程序配置")
        ;
        private final int value;
        private final String name;

        Type(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (RotateImage.Type e : RotateImage.Type.values()) {
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String  getStringType() {
        return Type.getName(this.type);
    }

    public String  getCategoryType() {
        if(category!=null){
            String name = ConstEnum.Category.getName(category);
            return name;
        }
        return null;
    }

}
