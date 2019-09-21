package cn.com.yusong.yhdg.common.tool.netty;

import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.common.tool.netty.Biz;

public class VehicleBizFactory {

    public Biz create(String msgCode) {
        return (Biz) SpringContextHolder.getBean("biz" + String.format("%s", msgCode));
    }
}