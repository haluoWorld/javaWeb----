package com.liule.booksys.entitys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.Pattern;
import lombok.Data;



@Data
@TableName("user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long userId;
    private String username;
    private String password;
    private String role;
    @Pattern(
            regexp = "^[a-zA-Z0-9_]{1,20}$|^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
            message = "联系信息只能是用户名或邮箱"
    )
    private String contactInfo;

}