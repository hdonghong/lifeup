package com.hdh.lifeup.auth;

import com.google.common.collect.Maps;
import com.hdh.lifeup.model.dto.UserInfoDTO;
import com.hdh.lifeup.model.enums.CodeMsgEnum;
import com.hdh.lifeup.exception.GlobalException;
import com.hdh.lifeup.redis.ApiKey;
import com.hdh.lifeup.redis.RedisOperator;
import com.hdh.lifeup.service.UserInfoService;
import com.hdh.lifeup.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

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

    private RedisOperator redisOperator;

    private UserInfoService userInfoService;

    @Autowired
    public ApiInterceptor(RedisOperator redisOperator,
                          UserInfoService userInfoService) {
        this.redisOperator = redisOperator;
        this.userInfoService = userInfoService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (HandlerMethod.class.isInstance(handler)) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            ApiLimiting apiLimiting = handlerMethod.getMethodAnnotation(ApiLimiting.class);
            // 没有使用鉴权限流注解，直接放行
            if (apiLimiting == null) {
                return super.preHandle(request, response, handler);
            }

            // 接口鉴权，将key拼接上用户id
            String key = request.getRequestURI();
            if (apiLimiting.toAuth()) {
                UserInfoDTO user = this.getUser(request);
                UserContext.set(user);
                key +=  "-" + user.getUserId();
            }

            // 接口限流
            ApiKey<Long> apiKey = ApiKey.withExpire(apiLimiting.seconds());
            Long accessCount = redisOperator.get(apiKey, key);
            int maxAccess = apiLimiting.maxAccess();
            if (accessCount == null) {
                redisOperator.setex(apiKey, key, 1L);
            } else if (accessCount < maxAccess) {
                redisOperator.incr(apiKey, key);
            } else {
                log.error("【Api接口拦截】限流key = [{}]，accessCount = [{}], maxAccess = [{}]", key, accessCount, maxAccess);
                throw new GlobalException(CodeMsgEnum.TOO_MANY_ACCESSES);
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
