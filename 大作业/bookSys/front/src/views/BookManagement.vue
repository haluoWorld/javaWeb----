<template>
  <div class="book-management">
    <h2>图书管理</h2>
    <div class="buttons">
      <button class="button" @click="openAddModal()">添加新图书</button>
    </div>
    <div class="books-container">
      <table class="book-table">
        <thead>
          <tr>
            <th>书名</th>
            <th>作者</th>
            <th>ISBN</th>
            <th>出版日期</th>
            <th>标签</th>
            <th>类别</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="book in books" :key="book.bookId">
            <td>{{ book.title }}</td>
            <td>{{ book.author }}</td>
            <td>{{ book.isbn }}</td>
            <td>{{ book.publishDate }}</td>
            <td>{{ book.tag }}</td>
            <td>{{ book.categories && book.categories.length ? book.categories.map(category => category.categoryName).join(', ') : '无' }}</td>
            <td>
              <button @click="openEditModal(book)" class="btn-link">编辑</button>
              <button @click="deleteBook(book.bookId)" class="btn-delete">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="pagination">
      <button @click="changePage(currentPage - 1)" :disabled="currentPage === 1">上一页</button>
      <span>页码: {{ currentPage }} / {{ totalPages }}</span>
      <button @click="changePage(currentPage + 1)" :disabled="currentPage === totalPages">下一页</button>
    </div>

    <div class="modal" v-if="isModalVisible" @click.self="closeModal">
      <div class="modal-content">
        <div class="modal-header">
          <h2>{{ editingBook ? '编辑图书' : '添加新图书' }}</h2>
          <span class="modal-close" @click="closeModal()">×</span>
        </div>
        <div class="modal-body">
          <form @submit.prevent="editingBook ? updateBook() : addBook()" class="form">
            <input type="text" v-model="bookTitle" placeholder="书名" required class="form-input" />
            <input type="text" v-model="bookAuthor" placeholder="作者" required class="form-input" />
            <input type="text" v-model="bookIsbn" placeholder="ISBN" required class="form-input" />
            <input type="date" v-model="publishDate" required class="form-input" />
            <input type="text" v-model="bookTag" placeholder="标签" required class="form-input" />
            <div class="category-selection">
              <label for="categories">选择分类:</label>
              <div v-for="category in categories" :key="category.categoryId" class="category-button-group">
                <button type="button"
                  :class="['category-button', { 'selected': selectedCategories.includes(category.categoryId) }]"
                  @click="toggleCategory(category.categoryId)">
                  {{ category.categoryName }}
                </button>
              </div>
            </div>
            <button type="submit" class="form-button">{{ editingBook ? '更新图书' : '添加图书' }}</button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getBooks, deleteBook as apiDeleteBook, addBook as apiAddBook, updateBook as apiUpdateBook, getCategories, getBookCategory, addBookCategory, deleteBookCategory } from '../utils/api';

export default {
  data() {
    return {
      books: [],
      categories: [],
      isModalVisible: false,
      bookTitle: '',
      bookAuthor: '',
      bookIsbn: '',
      publishDate: '',
      bookTag: '',
      selectedCategories: [],
      editingBook: false,
      currentBookId: null,
      bookCategories: [],
      currentPage: 1, // 当前页码
      totalPages: 1, // 总页数
    };
  },
  async created() {
    await this.fetchCategories();
    await this.fetchBooks(this.currentPage);
  },
  methods: {
    validateIsbn(isbn) {
      const isbnRegex = /^(?:\d{13}|(?:\d{1,5}-\d{1,7}-\d{1,7}-\d{1,7}-\d{1}))$/; // 13位数字或允许使用 "-" 分隔符的格式
      return isbnRegex.test(isbn);
    },
    async fetchBooks(page) {
      const response = await getBooks({ page, size: 10 }); // 请求时传递页码和每页大小
      this.books = response.data.data.records; // 书籍列表数据
      this.totalPages = Math.ceil(response.data.data.total / 10); // 根据总记录数计算总页数
    },
    async fetchCategories() {
      const response = await getCategories();
      this.categories = response.data.data; // 获取所有分类
    },
    async getBookCategories(bookId) {
      try {
        const response = await getBookCategory(bookId);
        return response.data.data;
      } catch (error) {
        console.error(error);
        return [];
      }
    },
    async changePage(page) {
      if (page < 1 || page > this.totalPages) return; // 页码范围验证
      this.currentPage = page; // 更新当前页码
      await this.fetchBooks(this.currentPage); // 重新获取书籍
    },
    openAddModal() {
      this.bookTitle = '';
      this.bookAuthor = '';
      this.bookIsbn = '';
      this.publishDate = '';
      this.bookTag = '';
      this.selectedCategories = [];
      this.editingBook = false;
      this.isModalVisible = true;
    },
    openEditModal(book) {
      this.bookTitle = book.title;
      this.bookAuthor = book.author;
      this.bookIsbn = book.isbn;
      this.publishDate = book.publishDate;
      this.bookTag = book.tag;
      this.currentBookId = book.bookId;
      this.selectedCategories = book.categories.map(category => category.categoryId);
      this.bookCategories = book.categories.map(category => category.categoryId);
      this.editingBook = true;
      this.isModalVisible = true;
    },
    async addBook() {
      if (!this.validateIsbn(this.bookIsbn)) {
        alert('ISBN格式不正确，请输入13位数字或允许使用 "-" 分隔符的格式！');
        return;
      }
      try {
        const newBook = {
          title: this.bookTitle,
          author: this.bookAuthor,
          isbn: this.bookIsbn,
          publishDate: this.publishDate,
          tag: this.bookTag,
        };
        const response = await apiAddBook(newBook);
        await this.addBookCategories(response.data.data.bookId);
        alert('添加成功！');
        await this.fetchBooks(this.currentPage); // 更新当前页的书籍列表
        this.closeModal();
      } catch (error) {
        alert('添加失败，请重试！');
      }
    },
    async updateBook() {
      if (!this.validateIsbn(this.bookIsbn)) {
        alert('ISBN格式不正确，请输入13位数字或允许使用 "-" 分隔符的格式！');
        return;
      }
      try {
        await apiUpdateBook({
          bookId: this.currentBookId,
          title: this.bookTitle,
          author: this.bookAuthor,
          isbn: this.bookIsbn,
          publishDate: this.publishDate,
          tag: this.bookTag,
        });
        await this.updateBookCategories();
        alert('更新成功！');
        await this.fetchBooks(this.currentPage); // 更新当前页的书籍列表
        this.closeModal();
      } catch (error) {
        alert('更新失败，请重试！');
      }
    },
    async addBookCategories(bookId) {
      for (const categoryId of this.selectedCategories) {
        try {
          await addBookCategory({ bookId, categoryId });
        } catch (error) {
          console.error(error);
        }
      }
    },
    async updateBookCategories() {
      const existingCategoryIds = this.bookCategories;
      const newCategoryIds = this.selectedCategories;

      // 删除不再选择的分类
      for (const categoryId of existingCategoryIds) {
        if (!newCategoryIds.includes(categoryId)) {
          try {
            await deleteBookCategory({ bookId: this.currentBookId, categoryId });
          } catch (error) {
            console.error('删除分类失败:', error);
          }
        }
      }
      // 添加新的分类
      for (const categoryId of newCategoryIds) {
        if (!existingCategoryIds.includes(categoryId)) {
          try {
            await addBookCategory({ bookId: this.currentBookId, categoryId });
          } catch (error) {
            console.error('添加分类失败:', error);
          }
        }
      }
    },
    async deleteBook(bookId) {
      if (confirm('确认要删除该图书吗？')) {
        try {
          await apiDeleteBook(bookId);
          alert('删除成功！');
          await this.fetchBooks(this.currentPage); // 更新当前页的书籍列表
        } catch (error) {
          alert('删除失败，请重试！');
        }
      }
    },
    closeModal() {
      this.isModalVisible = false;
      this.bookTitle = '';
      this.bookAuthor = '';
      this.bookIsbn = '';
      this.publishDate = '';
      this.bookTag = '';
      this.selectedCategories = [];
      this.editingBook = false;
      this.bookCategories = [];
    },
    toggleCategory(categoryId) {
      if (this.selectedCategories.includes(categoryId)) {
        this.selectedCategories = this.selectedCategories.filter(id => id !== categoryId);
      } else {
        this.selectedCategories.push(categoryId);
      }
    },
  },
};
</script>

<style scoped>
.book-management {
  padding: 20px;
}

.buttons {
  margin-bottom: 20px;
}

.button {
  padding: 10px 20px;
  background-color: #28a745;
  color: white;
  border-radius: 4px;
  border: none;
  cursor: pointer;
}

.button:hover {
  background-color: #218838;
}

.books-container {
  max-height: 400px; /* Set a maximum height for the books container */
  overflow-y: auto;  /* Allow vertical scrolling */
}

.book-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.book-table th,
.book-table td {
  padding: 10px;
  border: 1px solid #ddd;
  text-align: left;
}

.btn-link {
  padding: 5px 10px;
  background-color: #007bff;
  color: white;
  border-radius: 4px;
  border: none;
  margin-right: 5px;
}

.btn-link:hover {
  background-color: #0056b3;
}

.btn-delete {
  padding: 5px 10px;
  background-color: #dc3545;
  color: white;
  border-radius: 4px;
  border: none;
}

.btn-delete:hover {
  background-color: #c82333;
}

/* Modal styles */
.modal {
  position: fixed;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
}

.modal-content {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  background: white;
  padding: 20px;
  border-radius: 5px;
  width: 90%;
  max-width: 400px;
}

.modal-header {
  display: flex;
  justify-content: space-between;
}

.modal-close {
  cursor: pointer;
}

.form {
  display: flex;
  flex-direction: column;
}

.form-input {
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  width: 100%;
}

.form-button {
  background-color: #28a745;
  color: white;
  border: none;
  padding: 10px;
  border-radius: 5px;
  cursor: pointer;
}

.form-button:hover {
  background-color: #218838;
}

.category-selection {
  margin-bottom: 10px;
}

.category-select {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  width: 100%;
}
</style>