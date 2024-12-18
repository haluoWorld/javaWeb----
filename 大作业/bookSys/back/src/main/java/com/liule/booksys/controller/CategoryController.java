package com.liule.booksys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liule.booksys.entitys.Book;
import com.liule.booksys.entitys.BookCategory;
import com.liule.booksys.entitys.Category;
import com.liule.booksys.mapper.BookMapper;
import com.liule.booksys.service.BookCategoryService;
import com.liule.booksys.service.BookService;
import com.liule.booksys.service.CategoryService;
import com.liule.booksys.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BookService bookService;
    @Autowired
    private BookCategoryService bookCategoryService;

    // 获取所有分类
    @GetMapping("/")
    public R<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.list();
        return R.ok(categories);
    }

    // 添加新分类（管理员权限）
    @PreAuthorize("hasRole('admin')")
    @PostMapping("/")
    public R<Category> addCategory(@RequestBody Category category) {
        boolean added = categoryService.save(category);
        if (added) {
            return R.ok(category);
        } else {
            return R.failed("添加分类失败");
        }
    }

    // 删除分类（管理员权限）
    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/{id}")
    public R<String> deleteCategory(@PathVariable("id") Integer id) {
        boolean removed = categoryService.removeById(id);
        if (removed) {
            return R.ok("删除成功");
        } else {
            return R.failed("删除分类失败");
        }
    }
    //修改分类（管理员权限）
    @PreAuthorize("hasRole('admin')")
    @PutMapping("/")
    public R<Category> updateCategory(@RequestBody Category category) {
        boolean updated = categoryService.updateById(category);
        if (updated) {
            return R.ok(category);
        }else{
            return R.failed("失败");
        }
    }
    //通过分类搜索图书
    @GetMapping("/{categoryId}")
    public R<Page<Book>> getBooksByCategoryId(
            @PathVariable("categoryId") Integer categoryId,
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,  // 页码，默认为1
            @RequestParam(value = "size", defaultValue = "10") Integer pageSize) {  // 每页条数，默认为10

        // 校验分类ID是否有效
        if (categoryId == null || categoryId <= 0) {
            return R.failed("无效的分类ID");
        }

        // 查询分类关联的图书ID
        QueryWrapper<BookCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId);
        List<BookCategory> bookCategories = bookCategoryService.list(queryWrapper);

        if (bookCategories == null || bookCategories.isEmpty()) {
            return R.failed("该分类下暂无图书");
        }

        // 提取出图书ID列表
        List<Integer> bookIds = bookCategories.stream()
                .map(bookCategory -> bookCategory.getBookId().intValue())  // 强制转换
                .collect(Collectors.toList());

        // 创建分页对象
        Page<Book> page = new Page<>(pageNum, pageSize);  // 创建分页请求对象

        // 查询图书信息，并分页
        QueryWrapper<Book> bookQueryWrapper = new QueryWrapper<>();
        bookQueryWrapper.in("book_id", bookIds);  // 使用in查询所有匹配的图书
        Page<Book> bookPage = bookService.page(page, bookQueryWrapper);  // 使用分页查询

        return R.ok(bookPage);
    }
    //为图书添加分类
    @PreAuthorize("hasRole('admin')")
    @PostMapping("/book/")
    public R<BookCategory> addBookCategory(@RequestBody BookCategory bookCategory) {
        boolean added = bookCategoryService.save(bookCategory);
        if (added) {
            return R.ok(bookCategory);
        }
        else {
            return R.failed("添加失败");
        }
    }
    //删除图书的某个分类
    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/book/")
    public R<String> deleteBookCategory(@RequestBody BookCategory bookCategory) {
        if (bookCategory.getBookId() == null || bookCategory.getCategoryId() == null) {
            return R.failed("缺少必要参数：bookId 或 categoryId");
        }

        // 创建 QueryWrapper 查询条件
        QueryWrapper<BookCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("book_id", bookCategory.getBookId());
        queryWrapper.eq("category_id", bookCategory.getCategoryId());

        // 查找对应的 bookCategoryId
        BookCategory target = bookCategoryService.getOne(queryWrapper);
        if (target == null) {
            return R.failed("未找到指定的图书分类记录");
        }
        // 根据 bookCategoryId 删除记录
        boolean removed = bookCategoryService.removeById(target.getBookCategoryId());
        if (removed) {
            return R.ok("删除成功");
        } else {
            return R.failed("删除失败，请重试");
        }
    }

    // 获取某个图书的所有分类
    @GetMapping("/book/{bookId}")
    public R<List<Category>> getCategoriesByBookId(@PathVariable("bookId") Integer bookId) {
        // 校验bookId
        if (bookId == null || bookId <= 0) {
            return R.failed("无效的图书ID");
        }

        // 查询该图书ID对应的所有分类ID
        QueryWrapper<BookCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("book_id", bookId);
        List<BookCategory> bookCategories = bookCategoryService.list(queryWrapper);

        if (bookCategories == null || bookCategories.isEmpty()) {
            return R.failed("该图书没有关联任何分类");
        }

        // 提取所有的categoryId
        List<Integer> categoryIds = bookCategories.stream()
                .map(bookCategory -> bookCategory.getCategoryId().intValue())
                .collect(Collectors.toList());

        // 根据categoryIds查询所有分类信息
        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.in("category_id", categoryIds);  // 假设category_id是分类表的主键
        List<Category> categories = categoryService.list(categoryQueryWrapper);

        return R.ok(categories);
    }
}

