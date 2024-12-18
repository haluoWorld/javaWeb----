package com.liule.booksys.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liule.booksys.entitys.BookCategory;
import com.liule.booksys.mapper.BookCategoryMapper;
import com.liule.booksys.service.BookCategoryService;
import org.springframework.stereotype.Service;

@Service
public class BookCategoryServiceImpl extends ServiceImpl<BookCategoryMapper, BookCategory> implements BookCategoryService {
}
