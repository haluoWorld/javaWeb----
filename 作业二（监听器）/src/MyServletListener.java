package com.liule;

import com.liule.log.HTTPLog;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ServletListener",urlPatterns = "/Listener")
public class MyServletListener extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        String title = "日志";
        String docType =
                "<!DOCTYPE html> \n";
        out.println(docType +
                "<html>\n" +
                "<head><meta charset=\"utf-8\"><title>" + title + "</title></head>\n"+
                "<body bgcolor=\"#f0f0f0\">\n" +
                "<h1 align=\"center\">" + "日志输出" + "</h1>\n" +
                "<table width=\"100%\" border=\"1\" align=\"center\">\n" +
                "<tr bgcolor=\"#949494\">\n" +
                "<th text-align=\"center\">日志</th>\n"+
                "</tr>\n");
        ServletContext servletContext= req.getServletContext();
        for(int i = 1; i < HTTPLog.getLogCount(); i++){
            HTTPLog log = (HTTPLog) servletContext.getAttribute("Log"+i);
            out.println("<tr><td>请求处理开始时间: "+log.getParameter("startTime")+
                    " 客户端IP地址: "+log.getParameter("clientIP")+
                    " 请求方法: "+log.getParameter("method")+" 请求URI: "+
                    log.getParameter("uri")+" 查询字符串: "+
                    log.getParameter("queryString")+ " UER-Agent: "+
                    log.getParameter("userAgent")+" 处理时间: "+
                    log.getParameter("processingTime")+"</td></tr>");
        }
    }
}
