package com.liule.booksys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liule.booksys.entitys.Book;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookMapper extends BaseMapper<Book> {
}
