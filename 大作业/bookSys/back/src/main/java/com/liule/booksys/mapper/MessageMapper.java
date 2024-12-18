package com.liule.booksys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liule.booksys.entitys.Message;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
}
