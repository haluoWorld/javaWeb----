package com.liule;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "seFilter",urlPatterns = "/*")
public class seLoginFilter implements Filter {
    private static final String[] EXCLUDED_PATHS = {"/login.html","/loginservlet"}; //允许访问login.html和访问loginsevlet的通过
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("过滤器已经在客户端和servlet之间建立链接");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        String uri = request.getRequestURI().toLowerCase();
        System.out.println("客户端正在访问的地址 : "+uri);
        //判断是否是公共访问的页面
        if(isExcludedPath(uri)){
            filterChain.doFilter(request, response);  //必须得有这句话，要不然客户端接收不到响应
            return;
        }
        //判断是否未登录就访问权限页面
        if(session == null || session.getAttribute("username") == null){
            System.out.println("用户未登录");
            response.sendRedirect(request.getContextPath()+"/login.html"); //要用完整的项目uri名才行
        }else
        {
            System.out.println("允许访问");
            filterChain.doFilter(request, response);
            System.out.println("成功访问");
        }
    }
    public static boolean isExcludedPath(String uri){   //检验器
        for(String path : EXCLUDED_PATHS){
            if(uri.endsWith(path)){
                return true;
            }
        }
        return false;
    }
    @Override
    public void destroy() {
        System.out.println("过滤器已销毁");
    }
}
