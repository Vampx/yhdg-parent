package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerGuide extends IntIdEntity {
    String name; //
    Integer parentId; //
    String parentName; //
}
