<template>
  <div class="search-container">
    <h1>搜索书籍</h1>
    <div class="search-input">
      <input v-model="query" placeholder="请输入书名或作者名或 ISBN 号" />
      <button @click="searchBooks">搜索</button>
    </div>
    <h2 v-if="books.length > 0">搜索结果</h2>
    <ul v-if="books.length > 0">
      <li v-for="(book) in books" :key="book.bookId">
        <router-link :to="`/book/${book.bookId}`">{{ book.title }}</router-link>
      </li>
    </ul>

    <!-- 分页控制 -->
    <div class="pagination" v-if="totalPages > 1">
      <button @click="changePage(currentPage - 1)" :disabled="currentPage === 1">上一页</button>
      <span>页码: {{ currentPage }} / {{ totalPages }}</span>
      <button @click="changePage(currentPage + 1)" :disabled="currentPage === totalPages">下一页</button>
    </div>
  </div>
</template>

<script>
import { searchBooks } from '../utils/api'; // 导入搜索 API

export default {
  data() {
    return {
      query: '',
      books: [],
      currentPage: 1, // 当前页码
      totalPages: 1, // 总页数
      pageSize: 10, // 每页显示的数量
    };
  },
  methods: {
    async searchBooks() {
      if (!this.query) {
        alert('请输入搜索内容');
        return;
      }
      try {
        const response = await searchBooks({
          title: this.query,
          author: this.query,
          isbn: this.query,
          page: this.currentPage,
          size: this.pageSize,
        });
        this.books = response.data.data.records || []; // 搜索结果
        this.totalPages = Math.ceil(response.data.data.total / this.pageSize); // 计算总页数
      } catch (error) {
        alert('搜索失败: ' + error);
      }
    },
    changePage(page) {
      if (page < 1 || page > this.totalPages) return; // 防止无效页码
      this.currentPage = page; // 设置当前页码
      this.searchBooks(); // 重新搜索以更新书籍
    },
  },
};
</script>

<style scoped>
.search-container {
  width: 100%;
  max-width: 600px;
  margin: 0 auto; /* 居中显示 */
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.search-input {
  display: flex;
  margin-bottom: 20px;
}

input {
  flex: 1; /* 使输入框占据剩余空间 */
  margin-right: 10px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 16px;
}

button {
  padding: 10px 15px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}

button:hover {
  background-color: #0056b3; /* 鼠标悬停效果 */
}

h2 {
  margin-top: 20px;
}

.pagination {
  margin-top: 20px;
}

p {
  color: #666; /* 提示信息的颜色 */
}

ul {
  list-style-type: none; /* 移除默认列表样式 */
  padding: 0;
}

li {
  margin: 5px 0;
}
</style>
