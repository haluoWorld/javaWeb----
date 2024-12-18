package com.liule.booksys.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liule.booksys.entitys.Category;
import com.liule.booksys.mapper.CategoryMapper;
import com.liule.booksys.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
