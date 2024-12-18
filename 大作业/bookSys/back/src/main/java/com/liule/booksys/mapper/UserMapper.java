package com.liule.booksys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liule.booksys.entitys.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
