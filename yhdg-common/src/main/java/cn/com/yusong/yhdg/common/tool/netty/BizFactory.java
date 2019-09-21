package cn.com.yusong.yhdg.common.tool.netty;

public interface BizFactory {
    public Biz create(int msgCode);
}