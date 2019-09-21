package cn.com.yusong.yhdg.common.tool.netty;

import cn.com.yusong.yhdg.common.spring.SpringContextHolder;

public class DefaultBizFactory implements BizFactory {

    @Override
    public Biz create(int msgCode) {
        return (Biz) SpringContextHolder.getBean("biz" + String.format("%09d", msgCode));
    }
}