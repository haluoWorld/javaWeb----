<template>
  <div class="form-container">
    <h2>用户登录</h2>
    <form @submit.prevent="handleLogin" class="form">
      <div class="form-group">
        <label>用户名</label>
        <input v-model="username" type="text" required />
      </div>
      <div class="form-group">
        <label>密码</label>
        <input v-model="password" type="password" required />
      </div>
      <button type="submit" class="submit-button">登录</button>
    </form>
  </div>
</template>

<script>
import { login } from '../utils/api';

export default {
  data() {
    return {
      username: '',
      password: '',
    };
  },
  methods: {
    async handleLogin() {
      try {
        const response = await login({
          username: this.username,
          password: this.password,
        });
        
        if (response.data && response.data.code === 200) {
          const token = response.data.data.token;
          const role = response.data.data.role; // 假设角色加入了响应数据中
          
          this.$store.dispatch('login', { token, role });
          alert('登录成功！');
          this.$router.push('/');
        } else {
          alert('登录失败：' + (response.data.msg || '未知错误'));
        }
      } catch (error) {
        alert('登录失败，请检查用户名或密码！');
      }
    },
  },
};
</script>

<style scoped>
/* 样式同 Register.vue */
</style>
