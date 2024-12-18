package com.liule.booksys.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liule.booksys.entitys.Message;
import com.liule.booksys.entitys.User;
import com.liule.booksys.mapper.MessageMapper;
import com.liule.booksys.service.MessageService;
import com.liule.booksys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {
    // 这里你可以自定义业务逻辑
    @Autowired
    UserService userService;
    @Autowired
    MessageMapper messageMapper;
    // 判断用户是否是留言的所有者
    public boolean isUserMessageOwner(Integer messageId, String username) {
        Message message = messageMapper.selectById(messageId);
        if (message != null) {
            User user = userService.getById(message.getUserId());
            return user.getUsername().equals(username);
        }
        return false;
    }
}

