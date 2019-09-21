package cn.com.yusong.yhdg.agentserver.comm.server;

public class SessionId {
    public static final byte TERMINAL_SESSION = 1;
    public static final byte FRONT_SERVER_SESSION = 2;

    public byte type;
    public Object id;
    public Integer agentId;

    public SessionId(byte type, Object id, Integer agentId) {
        this.type = type;
        this.id = id;
        this.agentId = agentId;
    }

    public SessionId(byte type, Object id) {
        this.type = type;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SessionId sessionId = (SessionId) o;

        if (type != sessionId.type) return false;
        return id.equals(sessionId.id);

    }

    @Override
    public int hashCode() {
        int result = (int) type;
        result = 31 * result + id.hashCode();
        return result;
    }
}
