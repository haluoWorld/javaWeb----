package com.liule.booksys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liule.booksys.entitys.User;
import com.liule.booksys.service.UserService;
import com.liule.booksys.utils.JwtUtils;
import com.liule.booksys.utils.R;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class UserDetailsController {

    @Autowired
    private UserService userService;

    @Autowired JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 用户注册
    @PostMapping("/register")
    public R<String> register(@Valid @RequestBody User user) {
        // 检查用户名是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        User existingUser = userService.getOne(queryWrapper);
        if (existingUser != null) {
            return R.failed("用户名已存在");
        }
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 注册用户
        boolean save = userService.save(user);
        if (save) {
            return R.ok("注册成功");
        } else {
            return R.failed("注册失败");
        }
    }

    @PostMapping("/login")
    public R<Map<String, String>> login(@RequestBody User user) {
        // 检查用户名是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        User existingUser = userService.getOne(queryWrapper);

        if (existingUser == null || !passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            return R.failed("用户名或密码错误");
        }

        // 登录成功，生成 JWT
        String token = JwtUtils.generateToken(existingUser.getUsername(),existingUser.getRole());
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("role", existingUser.getRole());
        return R.ok(response);  // 返回 token 给客户端
    }

}