package cn.com.yusong.yhdg.common.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

public class JsonTypeFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(request, new CustomHttpResponse((HttpServletResponse)response));
    }

    @Override
    public void destroy() {
    }

    static class CustomHttpResponse extends HttpServletResponseWrapper {
        static final String CONTENT_TYPE = "Content-Type";
        static final String JSON = "application/json;charset=UTF-8";
        static final String PLAIN_TEXT = "text/plain;charset=UTF-8";

        CustomHttpResponse(HttpServletResponse response) {
            super(response);
        }

        @Override
        public void setContentType(String type) {
            if(JSON.equals(type)) {
                type = PLAIN_TEXT;
            }
            super.setContentType(type);
        }

        @Override
        public void setHeader(String name, String value) {
            if(CONTENT_TYPE.equals(name) && JSON.equals(value)) {
                value = PLAIN_TEXT;
            }
            super.setHeader(name, value);
        }

        @Override
        public void addHeader(String name, String value) {
            if(CONTENT_TYPE.equals(name) && JSON.equals(value)) {
                value = PLAIN_TEXT;
            }
            super.addHeader(name, value);
        }
    }
}
