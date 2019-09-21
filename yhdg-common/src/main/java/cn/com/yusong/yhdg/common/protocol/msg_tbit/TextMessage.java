package cn.com.yusong.yhdg.common.protocol.msg_tbit;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


@Log4j2
public abstract class TextMessage {
    public static final String HEAD = "[";
    public static final String TAIL = "]";
    public static final String SPLIT = ",";


    public String source;
    public String time;
    public String msgCode;
    public String terminalType;
    public String version;
    public String vinNo;

    public String encode() {
        StringBuilder builder = new StringBuilder();
        builder.append(HEAD);
        writeBody(builder);
        builder.append(TAIL);
        return builder.toString();
    }

    public static TextMessage decode(String text) {
        if(!text.startsWith(HEAD)){
            return null;
        }

        String body = text.substring(1);
        String[] array = body.split(",");
        if(array.length < 5){
            return null;
        }
        TextMessage message = null;

        String code = array[4];

        if(code.equals("T0")){
            message = new Msg_T0();
            message.readBody0(array);
            message.source = text;

        }else if(code.equals("T1")){
             message = new Msg_T1();
            message.readBody0(array);
            message.source = text;

        }else if(code.equals("T2")){
            message = new Msg_T1();
            message.readBody0(array);
            message.source = text;

        }else if(code.equals("T3")){
            message = new Msg_T3();
            message.readBody0(array);
            message.source = text;

        }else if(code.equals("T14")){
            message = new Msg_T14();
            message.readBody0(array);
            message.source = text;

        }else if(code.equals("T15")){
            message = new Msg_T15();
            message.readBody0(array);
            message.source = text;

        }else if(code.equals("WEB_01")){
            message = new Msg_WEB_01();
            message.readBody0(array);
            message.source = text;

        }else if(code.equals("WEB_11")){
            message = new Msg_WEB_11();
            message.readBody0(array);
            message.source = text;

        }else if(code.equals("WEB_12")){
            message = new Msg_WEB_12();
            message.readBody0(array);
            message.source = text;

        }else if(code.equals("WEB_21")){
            message = new Msg_WEB_21();
            message.readBody0(array);
            message.source = text;

        }else if(code.equals("WEB_22")){
            message = new Msg_WEB_22();
            message.readBody0(array);
            message.source = text;

        }else if(code.equals("WEB_31")){
            message = new Msg_WEB_31();
            message.readBody0(array);
            message.source = text;

        }else if(code.equals("WEB_32")){
            message = new Msg_WEB_32();
            message.readBody0(array);
            message.source = text;
        }

        return message;
    }

    private void readBody0(String[] array) {
        try {
            readBody(array);
        } catch (Exception e) {
            log.error("readBody0 error, text={}", StringUtils.join(array, ","));
            log.error("readBody0 error", e);
        }
    }

    protected void readBody(String[] array) {

    }

    protected void writeBody(StringBuilder body) {

    }

    public abstract String getMsgCode();

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
