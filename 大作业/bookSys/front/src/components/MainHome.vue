<template>
  <div>
    <h1>公告</h1>
    <div class="carousel">
      <div class="carousel-slides" :style="{ transform: `translateX(-${currentIndex * 100}%)` }">
        <div class="carousel-slide" v-for="announcement in announcements" :key="announcement.id">
          <h3>{{ announcement.title }}</h3>
          <p>{{ announcement.content }}</p> <!-- 假设还有内容字段 -->
        </div>
      </div>
    </div>

    <h1>图书列表</h1>
    <ul>
      <li v-for="book in books" :key="book.bookId">
        <router-link :to="`/book/${book.bookId}`">{{ book.title }}</router-link>
      </li>
    </ul>

    <!-- 分页控制 -->
    <div class="pagination">
      <button @click="changePage(currentPage - 1)" :disabled="currentPage === 1">上一页</button>
      <span>页码: {{ currentPage }}</span>
      <button @click="changePage(currentPage + 1)" :disabled="currentPage === totalPages">下一页</button>
    </div>
  </div>
</template>

<script>
import { getAnnouncements } from '@/utils/api'; // 导入获取公告的 API
import { getBooks } from '@/utils/api'; // 导入获取图书的 API

export default {
  data() {
    return {
      announcements: [], // 公告列表
      books: [], // 图书列表
      currentIndex: 0, // 当前索引
      intervalId: null, // 定时器 ID
      currentPage: 1, // 当前页码
      totalPages: 1, // 总页数
    };
  },
  created() {
    this.fetchAnnouncements();
    this.fetchBooks(); // 载入初始图书
    this.startCarousel(); // 启动轮播
  },
  beforeDestroy() {
    clearInterval(this.intervalId); // 清除定时器
  },
  methods: {
    async fetchAnnouncements() {
      try {
        const response = await getAnnouncements();
        this.announcements = response.data.data; // 假设返回的数据包含在 data 属性中
      } catch (error) {
        this.handleError(error);
      }
    },
    async fetchBooks() {
      try {
        const response = await getBooks({ page: this.currentPage, size: 10 }); // 使用当前页码
        this.books = response.data.data.records; // 假设返回的数据包含在 data 属性中
        this.totalPages = Math.ceil(response.data.data.total / 10); // 假设返回数据中包含总记录数
      } catch (error) {
        this.handleError(error);
      }
    },
    handleError(error) {
      console.error(error);
    },
    startCarousel() {
      this.intervalId = setInterval(() => {
        this.currentIndex = (this.currentIndex + 1) % this.announcements.length; // 循环索引
      }, 3000); // 每3秒切换
    },
    changePage(page) {
      if (page < 1 || page > this.totalPages) return; // 防止无效页码
      this.currentPage = page; // 更新当前页码
      this.fetchBooks(); // 重新获取图书
    },
  },
};
</script>

<style scoped>
.carousel {
  overflow: hidden; /* 隐藏超出部分 */
  position: relative; /* 相对定位 */
  width: 100%; /* 自适应宽度 */
}

.carousel-slides {
  display: flex; /* 水平排列 */
  transition: transform 0.5s ease; /* 动画效果 */
}

.carousel-slide {
  min-width: 100%; /* 每个幻灯片占满整个宽度 */
  box-sizing: border-box; /* 包含 padding 和 margin */
}

.pagination {
  margin-top: 20px;
}
</style>
