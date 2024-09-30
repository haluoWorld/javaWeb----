package com.liule.log;

public class HTTPLog extends Log {
    private String startTime;
    private String clientIP;
    private String method;
    private String uri;
    private String queryString;
    private String userAgent;
    private String processingTime;

    public HTTPLog() {
        super();
    }

    public HTTPLog(String clientIP, String method, String uri, String queryString, String userAgent) {
        super();
        this.clientIP = clientIP;
        this.method = method;
        this.uri = uri;
        this.queryString = queryString;
        this.userAgent = userAgent;
    }
    public String getParameter(String name){
        switch(name){
            case "startTime":return startTime;
            case "clientIP":return clientIP;
            case "method":return method;
            case "uri":return uri;
            case "queryString":return queryString;
            case "userAgent":return userAgent;
            case "processingTime":return processingTime;
        }
        return null;
    }
    public void setParameter(String name, String value){
        switch(name){
            case "startTime":startTime = value;break;
            case "clientIP":clientIP = value;break;
            case "method":method = value;break;
            case "uri":uri = value;break;
            case "queryString":queryString = value;break;
            case "userAgent":userAgent = value;break;
            case "processingTime":processingTime = value;break;
        }
    }
}
