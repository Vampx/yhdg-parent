package cn.com.yusong.yhdg.serviceserver.tool.voice;

public class Param {
    public int agentId;
    public long id;
    // Tts模板ID
    public String templateCode;
    // 被叫显号,可在语音控制台中找到所购买的显号
    public String calledShowNumber;
    // 被叫号码
    public String calledNumber;
    // 当模板中存在变量时需要设置此值
    public String variable;
    // 音量 取值范围 0--200
    public Integer volume;
    // 播放次数
    public Integer playTimes;

}
