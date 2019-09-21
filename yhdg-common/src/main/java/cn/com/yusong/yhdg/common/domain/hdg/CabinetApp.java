package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.entity.pagination.Page;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CabinetApp extends Page {
    Integer appId;
    String cabinetId;
}
