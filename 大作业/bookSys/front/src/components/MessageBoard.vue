<template>
  <div class="message-board">
    <h2>留言板</h2>
    <ul class="message-list">
      <li v-for="message in messages" :key="message.id" class="message-item">
        <p>{{ message.content }}</p>
        <small>作者: {{ message.author }}</small>
      </li>
    </ul>
    <form @submit.prevent="addMessage" class="form">
      <textarea v-model="newMessage" placeholder="输入你的留言" class="message-input"></textarea>
      <button type="submit" class="submit-button">发布留言</button>
    </form>
  </div>
</template>

<script>
import { getMessagesByBookId, postMessage } from '../utils/api';

export default {
  props: {
    bookId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      messages: [],
      newMessage: '',
    };
  },
  async created() {
    await this.fetchMessages();
  },
  methods: {
    async fetchMessages() {
      try {
        const response = await getMessagesByBookId(this.bookId); // 根据图书 ID 获取留言
        this.messages = response.data;
      } catch (error) {
        alert('获取留言失败，请重试！');
      }
    },
    async addMessage() {
      try {
        await postMessage({ 
          content: this.newMessage, 
          bookId: this.bookId // 传递图书 ID
        });
        this.newMessage = '';
        await this.fetchMessages(); // 重新获取留言
      } catch (error) {
        alert('发布失败，请重试！');
      }
    },
  },
};
</script>

<style>
.message-board {
  padding: 20px;
}
.message-list {
  list-style: none;
  padding: 0;
}
.message-item {
  border-bottom: 1px solid #ddd;
  padding: 10px 0;
}
.message-input {
  width: 100%;
  min-height: 60px;
  margin-bottom: 10px;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
</style>
