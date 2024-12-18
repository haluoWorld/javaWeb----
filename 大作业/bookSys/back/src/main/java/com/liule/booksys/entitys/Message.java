package com.liule.booksys.entitys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.sql.Timestamp;
import java.util.List;

@Data
@TableName("message")
public class Message {
    @TableId(type = IdType.AUTO)
    private Integer messageId;      // 留言ID（主键）
    @NotNull(message = "关联图书不能为空")
    private Integer bookId;
    @NotNull(message = "关联用户不能为空")
    private Long userId;         // 用户ID（外键）
    @NotNull(message = "留言内容不能为空")
    private String content;         // 留言内容
    private Integer parentId;      //父留言
    private Timestamp createdAt;         // 留言时间

//    //非数据库映射字段
//    @TableField(exist = false)
//    private List<Message> children;//查看子留言
}
