package com.liule.booksys.entitys;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.util.Date;


@Data
@TableName("book")
public class Book {
    @TableId(type = IdType.AUTO)
    private int bookId;
    @NotNull(message = "书名不能为空")
    private String title;

    @NotNull(message = "作者不能为空")
    private String author;
    @Pattern(
            regexp = "^(\\d{10}|\\d{13}|\\d{1,5}-\\d{1,7}-\\d{1,7}-\\d{1,7}-\\d{1,2})$",
            message = "ISBN 格式不正确"
    )
    private String isbn;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date publishDate;
    private String tag;

}
