package com.liule;

import com.liule.log.HTTPLog;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
@WebListener
public class ServletRequestListenerCount implements ServletRequestListener {
    //格式化时间
    private static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //请求结束逻辑
    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
        //适当的初始化
        ServletContext servletContext = servletRequestEvent.getServletContext();
        //取当前的Log对象
        HTTPLog log = (HTTPLog) servletContext.getAttribute("Log" + HTTPLog.getLogCount());
        String startTimeStr = log.getParameter("startTime");
        //需要转化时间
        Date startTime;
        try {
            startTime = DATEFORMAT.parse(startTimeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            startTime = new Date(0);
        }
        Date endTime = new Date();
        // 计算处理时间（毫秒）
        long processingTimeMillis = endTime.getTime() - startTime.getTime();
        // 格式化处理时间为字符串
        String processingTimeStr = String.format("%04d ms", processingTimeMillis);
        // 设置处理时间到 log 对象
        log.setParameter("processingTime", processingTimeStr);
        // 存储log对象到ServletContext
        servletContext.setAttribute(log.toString(), log);
    }

    //请求开始逻辑
    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        //从httpRequest取数据
        ServletRequest request = servletRequestEvent.getServletRequest();
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        ServletContext servletContext = servletRequestEvent.getServletContext();
        String ClientIP = httpRequest.getRemoteAddr();
        String method = httpRequest.getMethod();
        String uri = httpRequest.getRequestURI();
        String queryString = httpRequest.getQueryString();
        String userAgent = httpRequest.getHeader("User-Agent");
        //使用Data()来记录当前时间
        String startTime = DATEFORMAT.format(new Date());
        //创建HTTPLog对象来存储数据
        HTTPLog httpLog = new HTTPLog();
        httpLog.setParameter("clientIP", ClientIP);
        httpLog.setParameter("method", method);
        httpLog.setParameter("uri", uri);
        httpLog.setParameter("queryString", queryString);
        httpLog.setParameter("userAgent", userAgent);
        httpLog.setParameter("startTime", startTime);
        //将log对象放入ServletContext中实现共享
        servletContext.setAttribute(httpLog.toString(), httpLog);
        System.out.println("启动监听器");
    }
}
