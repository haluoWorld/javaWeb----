package com.liule.booksys.entitys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("book_category")
public class BookCategory {

    @TableId(type = IdType.AUTO)
    private Long bookCategoryId; // 主键

    private Long categoryId; // 分类 ID
    private Long bookId; // 图书 ID
}