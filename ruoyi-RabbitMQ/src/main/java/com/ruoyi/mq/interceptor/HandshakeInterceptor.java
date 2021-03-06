package com.ruoyi.mq.interceptor;

import com.ruoyi.mq.interceptor.common.Constants;
import com.ruoyi.mq.interceptor.common.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.text.MessageFormat;
import java.util.Map;

/**
 * 自定义{@link org.springframework.web.socket.server.HandshakeInterceptor}，实现“需要登录才允许连接WebSocket”
 *
 * @author zifangsky
 * @date 2018/10/11
 * @since 1.0.0
 */
@Component
class AuthHandshakeInterceptor implements HandshakeInterceptor {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        HttpSession session = SpringContextUtils.getSession();
        User loginUser = (User) session.getAttribute(Constants.SESSION_USER);

        if(loginUser != null){
            logger.debug(MessageFormat.format("用户{0}请求建立WebSocket连接", loginUser.getUsername()));
            return true;
        }else{
            logger.error("未登录系统，禁止连接WebSocket");
            return false;
        }

    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

    }

}
