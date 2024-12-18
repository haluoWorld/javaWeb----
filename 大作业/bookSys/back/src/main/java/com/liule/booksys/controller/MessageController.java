package com.liule.booksys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liule.booksys.entitys.Message;
import com.liule.booksys.entitys.User;
import com.liule.booksys.service.MessageService;
import com.liule.booksys.utils.R;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    // 添加留言
    @PostMapping("/")
    public R<Message> addMessage(@RequestBody Message message) {


        message.setCreatedAt(new Timestamp(System.currentTimeMillis()));


        // 保存留言
        boolean saved = messageService.save(message);
        if (saved) {
            return R.ok(message);
        } else {
            return R.failed("添加留言失败");
        }
    }

    // 回复留言
    @PostMapping("/reply/{messageId}")
    public R<Message> replyMessage(@PathVariable Integer messageId, @RequestBody Message replyMessage, HttpServletRequest request) {
        // 获取当前登录用户
        User user = (User) request.getAttribute("user");
        if (user == null) {
            return R.failed("用户未登录");
        }

        // 获取留言的父留言
        Message parentMessage = messageService.getById(messageId);
        if (parentMessage == null) {
            return R.failed("父留言不存在");
        }

        replyMessage.setUserId(user.getUserId());
        replyMessage.setParentId(messageId);  // 设置父留言ID
        replyMessage.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        boolean saved = messageService.save(replyMessage);
        if (saved) {
            return R.ok(replyMessage);
        } else {
            return R.failed("回复留言失败");
        }
    }

    // 删除留言
    @DeleteMapping("/{messageId}")
    @PreAuthorize("hasRole('ROLE_admin') or @messageServiceImpl.isUserMessageOwner(#messageId, authentication.name)")
    public R<String> deleteMessage(@PathVariable("messageId") Integer messageId) {
        boolean removed = messageService.removeById(messageId);
        if (removed) {
            return R.ok("删除成功");
        } else {
            return R.failed("删除失败，留言不存在");
        }
    }

    // 查看该留言的子留言
    @GetMapping("/{messageId}")
    public R<List<Message>> listchirldenMessages(@PathVariable Integer messageId) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", messageId); // 假设Message类有parent_id字段表示父留言ID
        List<Message> subMessages = messageService.list(queryWrapper);
        return R.ok(subMessages);
    }
    // 获取图书的留言列表（包括回复）
    @GetMapping("/list/{bookId}")
    public R<List<Message>> listMessages(@PathVariable Integer bookId) {
        // 查询关联留言
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("book_id", bookId);
        List<Message> message = messageService.list(queryWrapper);
        return R.ok(message);
    }
    // 获取留言列表（分页）
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/list")
    public R<Page<Message>> listMessages(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        // 构建分页对象
        Page<Message> pageRequest = new Page<>(page, size);

        // 构建查询条件
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        // 可以根据需求添加筛选条件，比如按图书ID查询
        // queryWrapper.eq("book_id", bookId);

        // 获取分页结果
        Page<Message> messagePage = messageService.page(pageRequest, queryWrapper);

        return R.ok(messagePage);
    }


    // 查看某个用户的留言
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/user/{userId}")
    public R<List<Message>> getMessagesByUserId(@PathVariable("userId") Integer userId) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<Message> messageList = messageService.list(queryWrapper);
        return R.ok(messageList);
    }


}

