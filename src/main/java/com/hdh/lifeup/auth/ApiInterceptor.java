package com.hdh.lifeup.auth;

import com.google.common.collect.Maps;
import com.hdh.lifeup.dto.UserInfoDTO;
import com.hdh.lifeup.enums.CodeMsgEnum;
import com.hdh.lifeup.exception.GlobalException;
import com.hdh.lifeup.service.UserInfoService;
import com.hdh.lifeup.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Map;

/**
 * ApiInterceptor class<br/>
 * Api接口拦截
 * @author hdonghong
 * @since 2018/08/23
 */
@Slf4j
@Service
public class ApiInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private RedisTemplate<String, UserInfoDTO> redisTemplate;

    private UserInfoService userInfoService;

    @Autowired
    public ApiInterceptor(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (HandlerMethod.class.isInstance(handler)) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            ApiLimiting apiLimiting;
            boolean toAuth = (apiLimiting = handlerMethod.getMethodAnnotation(ApiLimiting.class)) != null
                            && apiLimiting.toAuth();
            if (toAuth) {
                // 如果需要鉴权
                UserInfoDTO user = getUser(request);
                UserContext.set(user);

                // TODO 记录访问次数

            }
        }
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        UserContext.remove();
        TokenContext.remove();
        super.afterCompletion(request, response, handler, ex);
    }


    private UserInfoDTO getUser(HttpServletRequest request) {
        String authenticityToken = request.getHeader(TokenUtil.AUTHENTICITY_TOKEN);
        if (StringUtils.isEmpty(authenticityToken)) {
            Enumeration e1 = request.getHeaderNames();
            Map<String, String> headersMap = Maps.newHashMap();
            while (e1.hasMoreElements()) {
                String headerName = (String) e1.nextElement();
                headersMap.put("\n\t" + headerName, request.getHeader(headerName));
            }

            log.error("【Api接口拦截】Request Header中没有携带[{}], \nHeaders = [{}]", TokenUtil.AUTHENTICITY_TOKEN, headersMap);
            throw new GlobalException(CodeMsgEnum.TOKEN_ABSENT);
        }
        UserInfoDTO user = userInfoService.getByToken(authenticityToken);
        if (user == null) {
            log.error("【Api接口拦截】不合法的Token或者Token失效");
            throw new GlobalException(CodeMsgEnum.TOKEN_INVALID);
        }
        TokenContext.set(authenticityToken);
        return user;
    }

}
