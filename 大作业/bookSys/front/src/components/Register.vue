<template>
  <div class="form-container">
    <h2>用户注册</h2>
    <form @submit.prevent="handleRegister" class="form">
      <div class="form-group">
        <label>用户名</label>
        <input v-model="username" type="text" required />
      </div>
      <div class="form-group">
        <label>密码</label>
        <input v-model="password" type="password" required />
      </div>
      <div class="form-group">
        <label>身份</label>
        <select v-model="role">
          <option value="user">读者</option>
          <option value="admin">图书管理员</option>
        </select>
      </div>
      <div class="form-group">
        <label>联系方式</label>
        <input v-model="contactInfo" type="text" required />
        <small class="text-muted">请输入手机号码或邮箱</small>
      </div>
      <button type="submit" class="submit-button">注册</button>
    </form>
  </div>
</template>

<script>
import { register } from '../utils/api';

export default {
  data() {
    return {
      username: '',
      password: '',
      role: 'user', // 默认身份为用户
      contactInfo: ''
    };
  },
  methods: {
    validateContactInfo(contact) {
      const phoneRegex = /^[1][3-9][0-9]{9}$/; // 简单的手机号正则
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; // 邮箱正则
      return phoneRegex.test(contact) || emailRegex.test(contact);
    },
    async handleRegister() {
      if (!this.validateContactInfo(this.contactInfo)) {
        alert('联系方式格式不正确，请输入有效的手机号码或邮箱！');
        return;
      }
      try {
        await register({
          username: this.username,
          password: this.password,
          role: this.role,
          contactInfo: this.contactInfo
        });
        alert('注册成功！');
        this.$router.push('/login');
      } catch (error) {
        alert('注册失败，请重试！');
      }
    }
  }
};
</script>

<style>
.form-container {
  max-width: 400px;
  margin: 50px auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #f9f9f9;
}
.form-group {
  margin-bottom: 15px;
}
label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}
input, select {
  width: 100%;
  padding: 8px;
  margin-bottom: 5px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.submit-button {
  display: block;
  width: 100%;
  padding: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.submit-button:hover {
  background-color: #0056b3;
}
</style>
