package cn.com.yusong.yhdg.routeserver.biz;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.routeserver.config.AppConfig;
import cn.com.yusong.yhdg.routeserver.protocol.RequestMessage;
import cn.com.yusong.yhdg.routeserver.protocol.ResponseMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public abstract class BizHandler {
	
	final static Logger log = LogManager.getLogger(BizHandler.class);

	@Autowired
    protected AppConfig appConfig;

    public void setAppConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public void handle(HttpServletRequest request, HttpServletResponse response, RequestMessage message) throws Exception {
        if(doCheck()) {
            biz(request, response, message);
        }
	}

    protected boolean doCheck() throws IOException, InterruptedException {


        return true;
    }

	protected void write(HttpServletRequest request, HttpServletResponse response, ResponseMessage message) throws IOException {
		String text = message.encode();
		OutputStream outputStream = response.getOutputStream();
		byte[] source = text.getBytes(Constant.ENCODING_UTF_8);
        response.setContentLength(source.length);
		outputStream.write( source );
		outputStream.flush();

        if(log.isDebugEnabled()) {
            log.debug("响应{} 协议 {}", request.getRemoteAddr(), text);
        }
	}

    protected void write(HttpServletRequest request, HttpServletResponse response, int msgCode, int rtnCode) throws IOException {
    }

	protected abstract void biz(HttpServletRequest request, HttpServletResponse response, RequestMessage message) throws Exception;
}
