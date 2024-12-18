package com.liule.booksys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liule.booksys.entitys.User;
import com.liule.booksys.service.UserService;
import com.liule.booksys.utils.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    // 用户注销
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        if (username == null) {
            return R.failed("用户未登录");
        }
        return R.ok("注销成功");
    }

    // 获取当前用户信息
    @GetMapping("/profile")
    public R<User> getUserProfile(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        if (username == null) {
            return R.failed("用户未登录");
        }

        // 根据用户名查询用户信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userService.getOne(queryWrapper);
        return R.ok(user);
    }


    // 更新用户信息
    @PutMapping("/profile")
    public R<String> updateUser(@Valid @RequestBody User user, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        if (username == null) {
            return R.failed("用户未登录");
        }

        // 根据用户名查询用户信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User oldUser = userService.getOne(queryWrapper);

        user.setUsername(username);  // 保证修改的是当前登录的用户
        user.setUserId(oldUser.getUserId());
        user.setRole(oldUser.getRole());

        // 如果密码不为空，进行加密处理
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            // 如果密码为空，可以选择不修改密码，或者做其他处理
            user.setPassword(oldUser.getPassword()); // 保持原密码不变
        }

        boolean updated = userService.updateById(user);

        if (updated) {
            return R.ok("信息更新成功");
        } else {
            return R.failed("信息更新失败");
        }
    }

}
