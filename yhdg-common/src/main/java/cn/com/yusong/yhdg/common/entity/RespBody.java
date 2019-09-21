package cn.com.yusong.yhdg.common.entity;

public class RespBody<T> {

    T body;
    long waitTime; //ms

    public RespBody(long waitTime) {
        this.waitTime = waitTime;
    }

    public RespBody() {
        this(0);
    }

    public synchronized T getBody() throws InterruptedException {
        if(body == null) {
            if(waitTime <= 0) {
                wait();
            } else {
                wait(waitTime);
            }
        }

        return body;
    }

    public synchronized void setBody(T body) {
        this.body = body;
        notifyAll();
    }
}
