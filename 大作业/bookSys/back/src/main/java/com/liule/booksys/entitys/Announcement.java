package com.liule.booksys.entitys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("Announcement")
public class Announcement {
    @TableId(type = IdType.AUTO)
    private Integer announcementId;      // 公告ID（主键）
    private String title;                // 公告标题
    private String content;              // 公告内容
    private Timestamp createTime;         // 发布时间
}