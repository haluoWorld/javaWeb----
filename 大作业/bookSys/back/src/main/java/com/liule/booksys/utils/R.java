package com.liule.booksys.utils;

import lombok.Data;

@Data
public class R<T> {
    private Integer code;  // 响应状态码
    private String msg;    // 响应消息
    private T data;        // 响应数据

    // 成功的响应
    public static <T> R<T> ok(T data) {
        R<T> r = new R<>();
        r.setCode(200);
        r.setMsg("成功");
        r.setData(data);
        return r;
    }

    // 失败的响应
    public static <T> R<T> failed(String msg) {
        R<T> r = new R<>();
        r.setCode(500);
        r.setMsg(msg);
        return r;
    }


}
