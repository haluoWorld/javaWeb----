<template>
  <div class="book-list">
    <h2>图书列表</h2>
    <ul>
      <li v-for="book in books" :key="book.id" @click="selectBook(book.id)" class="book-item">
        {{ book.title }} ({{ book.author }})
      </li>
    </ul>
    <MessageBoard v-if="selectedBookId" :book-id="selectedBookId"/>
  </div>
</template>

<script>
import { getBooks } from '../utils/api';
import MessageBoard from './MessageBoard.vue'; // 引入留言板组件

export default {
  components: {
    MessageBoard
  },
  data() {
    return {
      books: [],
      selectedBookId: null,
    };
  },
  async created() {
    const response = await getBooks();
    this.books = response.data;
  },
  methods: {
    selectBook(bookId) {
      this.selectedBookId = bookId; // 设置选中的图书ID
    }
  }
};
</script>

<style scoped>
.book-list {
  padding: 20px;
  background-color: #f9f9f9; /* 设置背景颜色 */
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  max-width: 800px; /* 设置最大宽度 */
  margin: 0 auto; /* 居中 */
}

h2 {
  margin-bottom: 20px; /* 标题下方间距 */
  color: #333; /* 标题颜色 */
  text-align: center; /* 标题居中 */
}

ul {
  list-style-type: none; /* 移除默认列表样式 */
  padding: 0; /* 移除内边距 */
}

.book-item {
  padding: 15px;
  margin: 10px 0; /* 添加间距 */
  background-color: #ffffff; /* 每个图书项的背景色 */
  border-radius: 5px; /* 圆角 */
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1); /* 阴影效果 */
  cursor: pointer; /* 鼠标变成手型 */
  transition: transform 0.2s, box-shadow 0.2s; /* 添加过渡效果 */
}

.book-item:hover {
  transform: scale(1.02); /* 鼠标悬停时稍微放大 */
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2); /* 鼠标悬停时加重阴影 */
}

.message-board {
  margin-top: 20px; /* 留言板与列表之间的间距 */
}
</style>
