<template>
  <div>
    <h1>{{ book.title }}</h1>
    <p>作者: {{ book.author }}</p>
    <p>ISBN: {{ book.isbn }}</p>
    <p>出版日期: {{ book.publishDate }}</p>
    
    <h2>标签</h2>
    <p>{{ book.tag}}</p>

    <h2>分类</h2>
    <p >{{categories && categories.length ? categories.join(', ') : '无' }}</p>
  

    <h2>留言板</h2>
    <form @submit.prevent="addMessage">
      <textarea v-model="newMessage" placeholder="留言内容" required></textarea>
      <button type="submit">留言</button>
    </form>
    
    <div v-for="(message) in messages" :key="message.messageId">
      <p>{{ message.content }}</p>
    </div>
  </div>
</template>

<script>
import { getBookDetails, getMessagesByBookId, addMessage, getProfile, getBookCategory } from '../utils/api'; // 导入所需 API

export default {
  data() {
    return {
      book: {},
      messages: [],
      categories: [], // 用于存储分类
      newMessage: '',
      userId: null, // 用户 ID
    };
  },
  created() {
    const bookId = this.$route.params.id;
    this.fetchBookDetails(bookId);
    this.fetchMessages(bookId);
    this.fetchUserProfile(); // 获取用户信息以获取用户 ID
    this.fetchCategories(bookId); // 获取图书分类
  },
  methods: {
    async fetchBookDetails(id) {
      try {
        const response = await getBookDetails(id);
        this.book = response.data.data.book; // 调整根据你的 API 响应结构
      } catch (error) {
        alert('获取书籍详情失败: ' + error);
      }
    },
    async fetchMessages(bookId) {
      try {
        const response = await getMessagesByBookId(bookId);
        this.messages = response.data.data; // 假设返回的留言结构包含在 data 属性中
      } catch (error) {
        alert('获取留言失败: ' + error);
      }
    },
    async fetchUserProfile() {
      try {
        const response = await getProfile();
        this.userId = response.data.data.userId; // 假设返回的数据结构
      } catch (error) {
        alert('获取用户信息失败: ' + error);
      }
    },
    async fetchCategories(bookId) {
      try {
        const response = await getBookCategory(bookId);
        return response.data.data ? response.data.data.map(category => category.categoryName) : [];
      } catch (error) {
        alert('获取分类失败: ' + error);
      }
    },
    async addMessage() {
      try {
        if (!this.newMessage.trim()) return; // 确保留言内容不为空
        
        const messageData = {
          bookId: this.book.bookId,
          userId: this.userId, // 使用真实的用户 ID
          content: this.newMessage,
          parentId: null, // 假设没有父级留言
          createdAt: null, 
        };
        
        await addMessage(messageData);
        
        this.newMessage = ''; // 清空输入框
        await this.fetchMessages(this.book.bookId); // 刷新留言
      } catch (error) {
        alert('留言失败: ' + error);
      }
    },
  },
};
</script>

<style scoped>
textarea {
  width: 100%;
  height: 100px;
  margin-top: 10px;
}
</style>
