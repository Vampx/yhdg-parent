package cn.com.yusong.yhdg.serviceserver.tool.voice;

public class Result {
    public static final Result FAIL = new Result();

    public int id; //对应的数据库ID
    public int httpCode; //http响应码
    public String httpContent; //http响应内容
    public boolean success;
    public String msgId;
    public String message;


}
