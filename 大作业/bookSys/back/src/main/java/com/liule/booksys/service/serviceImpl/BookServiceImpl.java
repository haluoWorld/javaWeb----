package com.liule.booksys.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liule.booksys.entitys.Book;
import com.liule.booksys.mapper.BookMapper;
import com.liule.booksys.service.BookService;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {
}

