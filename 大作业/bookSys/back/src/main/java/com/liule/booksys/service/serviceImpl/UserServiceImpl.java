package com.liule.booksys.service.serviceImpl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liule.booksys.entitys.User;
import com.liule.booksys.mapper.UserMapper;
import com.liule.booksys.service.UserService;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
}