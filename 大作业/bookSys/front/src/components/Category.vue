<template>
  <div class="category-container">
    <h1>类别选择</h1>
    <div class="category-list">
      <div 
        v-for="(category, index) in categories" 
        :key="category.id" 
        class="category-item"
        @click="getBooksByCategory(category.categoryId)"
      >
        {{ category.categoryName }}
      </div>
    </div>
    <h2 v-if="books.length > 0">该分类下的书籍</h2>
    <p v-else>暂时没有书籍</p>
    <ul v-if="books.length > 0">
      <li v-for="(book) in books" :key="book.bookId">
        <router-link :to="`/book/${book.bookId}`">{{ book.title }}</router-link>
      </li>
    </ul>
  </div>
</template>

<script>
import { getCategories, getBooksByCategory } from '../utils/api';

export default {
  data() {
    return {
      categories: [],
      books: [],
    };
  },
  created() {
    this.fetchCategories();
  },
  methods: {
    async fetchCategories() {
      try {
        const response = await getCategories();
        this.categories = response.data.data; // 根据你的 API 响应结构进行调整
      } catch (error) {
        alert('获取类别失败: ' + error);
      }
    },
    async getBooksByCategory(categoryId) {
      try {
        const response = await getBooksByCategory(categoryId);
        if(response.data.code == 200) {
          this.books = response.data.data.records; // 根据你的 API 响应结构进行调整
        } else {
          this.books = [];
        }
      } catch (error) {
        alert('获取书籍失败: ' + error);
      }
    },
  },
};
</script>

<style scoped>
.category-container {
  max-width: 800px;
  margin: 0 auto; /* 居中显示 */
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.category-list {
  display: flex;
  flex-direction: column;
  margin-bottom: 20px;
}

.category-item {
  padding: 15px;
  background-color: #f4f4f4;
  margin: 5px 0;
  cursor: pointer;
  border-radius: 5px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.2); /* 添加阴影效果 */
  transition: background-color 0.3s, transform 0.2s; /* 添加过渡效果 */
}

.category-item:hover {
  background-color: #e2e2e2;
  transform: translateY(-2px); /* 鼠标悬停时稍微上升 */
}

h2 {
  color: #333; /* 标题颜色 */
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
