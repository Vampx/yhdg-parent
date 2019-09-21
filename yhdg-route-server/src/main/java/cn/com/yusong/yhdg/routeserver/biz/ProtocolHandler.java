package cn.com.yusong.yhdg.routeserver.biz;

import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.routeserver.protocol.RequestMessage;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ProtocolHandler {
	final static Logger log = LogManager.getLogger(ProtocolHandler.class);
	static int DEFAULT_MSG_CODE = 0;

	public static void handler(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		int msgCode = DEFAULT_MSG_CODE;
		try {
            boolean isFileUpload = ServletFileUpload.isMultipartContent(httpRequest);
            if(isFileUpload) {
                doFile(httpRequest, httpResponse);
            } else {
                doXml(httpRequest, httpResponse);
            }

		} catch(Exception e) {
			httpResponse.setStatus(500);
			log.error("协议处理错误", e);
		}
	}

    private static String doXml(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
		String msgCode = httpRequest.getParameter("MsgCode");
        if(log.isDebugEnabled()) {
        	List<String> paramList = new ArrayList<String>();
			Enumeration enumeration = httpRequest.getParameterNames();
			while (enumeration.hasMoreElements()) {
				String name = (String) enumeration.nextElement();
				String value = StringUtils.trimToEmpty(httpRequest.getParameter(name));
				paramList.add(name + "=" + value);
			}
			log.debug("从{} 收到协议 {}", httpRequest.getRemoteAddr(), StringUtils.join(paramList, "\n"));
        }

        RequestMessage message = (RequestMessage) Class.forName("cn.com.yusong.yhdg.routeserver.protocol.Msg" + msgCode).newInstance();
        message.decode(httpRequest);

        BizHandler biz = (BizHandler) SpringContextHolder.getBean("biz" + msgCode);
        biz.handle(httpRequest, httpResponse, message);

        return msgCode;
    }

    private static int doFile(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        return DEFAULT_MSG_CODE;
    }
}
