package com.taotao.order.interceptor;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;
    @Value("${SSO_URL}")
    private String SSO_URL;
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //handler之前处理 true表示通过处理,false表示不通过
        //从cookie中去信息
        String token = CookieUtils.getCookieValue(request, TOKEN_KEY);
        //如果取不到,跳转到登录页面,需要把当前url作为参数传递给sso.sso登录成功之后跳转到请求页面
        if (StringUtils.isBlank(token)){
            //取当前url
            String requestURL = request.getRequestURL().toString();
            //跳转到登录页面,用redirect比较合适,登录之后还要回到当前页面,因此在url中要加一个url回调地址
            response.sendRedirect(SSO_URL+"/page/login?url="+requestURL);
            //既然没有登录,肯定要拦截
            return false;
        }
        //取到tooken,调用sso服务判断用户是否登录,
        TaotaoResult result = userService.getUserByToken(token);
        //4.如果用户未登录（有token，但是已经过期，也算是没登录），即没有取到用户信息。跳转到sso的登录页面
        //返回的TaotaoResult如果没有登录的话，状态码是400，如果登录了的话，状态码是200
        if (result.getStatus()!=200){
            StringBuffer url = request.getRequestURL();
            response.sendRedirect(SSO_URL+"/page/login?url="+url);
            return false;
        }
        //5.如果取到用户信息，就放行。
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //handler之后,返回modelview之前处理

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //返回modelview之后处理,这时候只能做一些异常处理
    }
}
