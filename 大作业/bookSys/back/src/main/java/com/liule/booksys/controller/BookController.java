package com.liule.booksys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liule.booksys.entitys.Book;
import com.liule.booksys.entitys.Message;
import com.liule.booksys.service.BookService;
import com.liule.booksys.service.MessageService;
import com.liule.booksys.utils.R;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private MessageService messageService;


    // 添加图书
    @PreAuthorize("hasRole('admin')")
    @PostMapping("/")
    public R<Book> addBook(@Valid  @RequestBody Book book) {
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", book.getTitle());
        Book existingBook = bookService.getOne(queryWrapper);
        if (existingBook != null) {
            return R.failed("书名以存在已存在");
        }
        boolean saved = bookService.save(book);
        if (saved) {
            log.info("图书添加成功: {}", book);
            return R.ok(book);
        }
        log.error("添加图书失败: {}", book);
        return R.failed("添加图书失败");
    }

    // 删除图书
    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/{bookId}")
    public R<String> deleteBook(@PathVariable("bookId") Integer bookId) {
        boolean removed = bookService.removeById(bookId);
        if (removed) {
            log.info("删除图书成功: bookId={}", bookId);
            return R.ok("删除成功");
        }
        log.error("删除图书失败，图书不存在: bookId={}", bookId);
        return R.failed("删除失败，图书不存在");
    }

    // 更新图书信息
    @PreAuthorize("hasRole('admin')")
    @PutMapping("/")
    public R<Book> updateBook(@Valid @RequestBody Book book) {
        boolean updated = bookService.updateById(book);
        if (updated) {
            log.info("更新图书信息成功: {}", book);
            return R.ok(book);
        }
        log.error("更新图书信息失败: {}", book);
        return R.failed("更新图书信息失败");
    }

    // 获取所有图书（支持分页）
    @GetMapping("/list/")
    public R<Page<Book>> listBooks(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                   @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<Book> bookPage = bookService.page(new Page<>(page, size));
        log.info("获取图书列表: page={}, size={}, total={}", page, size, bookPage.getTotal());
        return R.ok(bookPage);
    }

    // 根据 ID 查询图书
    @GetMapping("/{bookId}")
    public R<Book> getBookById(@PathVariable("bookId") Integer bookId) {
        Book book = bookService.getById(bookId);
        if (book != null) {
            log.info("查询图书成功: {}", book);
            return R.ok(book);
        }
        log.error("查询图书失败，图书不存在: bookId={}", bookId);
        return R.failed("图书不存在");
    }

    @GetMapping("/search")
    public R<Page<Book>> searchBooks(@RequestParam(value = "title", required = false) String title,
                                     @RequestParam(value = "author", required = false) String author,
                                     @RequestParam(value = "isbn", required = false) String isbn,
                                     @RequestParam(value = "page", defaultValue = "1") Integer page,
                                     @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();

        // 对每个字段进行模糊查询
        boolean conditionAdded = false;

        if (StringUtils.hasText(title)) {
            queryWrapper.like("title", title);
            conditionAdded = true;
        }

        if (StringUtils.hasText(author)) {
            if (conditionAdded) {
                queryWrapper.or();  // 增加 OR 连接
            }
            queryWrapper.like("author", author);
            conditionAdded = true;
        }

        if (StringUtils.hasText(isbn)) {
            if (conditionAdded) {
                queryWrapper.or();  // 增加 OR 连接
            }
            queryWrapper.like("isbn", isbn);
        }

        Page<Book> bookPage = bookService.page(new Page<>(page, size), queryWrapper);
        log.info("条件查询图书: title={}, author={}, isbn={}, page={}, size={}, total={}",
                title, author, isbn, page, size, bookPage.getTotal());

        return R.ok(bookPage);
    }


    // 获取图书详细信息（包含留言）
    @GetMapping("/details/{bookId}")
    public  R<Map<String, Object>> getBookDetails(@PathVariable("bookId") Integer bookId) {
        // 查询图书信息
        Book book = bookService.getById(bookId);
        if (book == null) {
            log.error("查询图书详情失败，图书不存在: bookId={}", bookId);
            return R.failed("图书不存在");
        }

        // 查询关联留言
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("book_id", bookId);
        List<Message> message = messageService.list(queryWrapper);
        //组织返回数据
        Map<String,Object> result = new HashMap<>();
        result.put("book",book);
        result.put("message",message);
        log.info("查询图书详情成功: bookId={}, 留言数量={}", bookId, message.size());
        return R.ok(result);
    }
}