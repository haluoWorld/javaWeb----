<template>
  <div id="app">
    <header class="header">
      <div class="logo">
        <router-link to="/">图书管理系统</router-link>
      </div>
      <nav class="nav">
        <ul class="nav-list">
          <li v-if="isLoggedIn"><router-link to="/" class="nav-link">首页</router-link></li>
          <li v-else><router-link to="/welcome" class="nav-link">首页</router-link></li>
          <li v-if="isLoggedIn"><router-link to="/profile" class="nav-link">个人信息</router-link></li>
          <li v-if="isReader || isAdmin" class="has-dropdown">
            <router-link to="/category" class="nav-link">分类</router-link>
            <router-link to="/search" class="nav-link">搜索</router-link>
          </li>
          <li v-if="isAdmin" class="has-dropdown">
            <router-link to="/book-management" class="nav-link">图书管理</router-link>
            <router-link to="/category-management" class="nav-link">分类管理</router-link>
            <router-link to="/announcement-management" class="nav-link">公告管理</router-link>
          </li>
          <li v-if="isLoggedIn">
            <button @click="logout" class="nav-link logout-button">注销</button>
          </li>
          <li v-else>
            <router-link to="/login" class="nav-link">登录</router-link>
            <router-link to="/register" class="nav-link">注册</router-link>
          </li>
        </ul>
      </nav>
    </header>
    
    <main class="main-content">
      <router-view></router-view>
    </main>
    
    <footer class="footer">
      <p>© 2024 图书管理系统</p>
    </footer>
  </div>
</template>

<script>
import { mapState } from 'vuex';

export default {
  computed: {
    ...mapState({
      isLoggedIn: state => state.isLoggedIn,
      userRole: state => state.userRole,
    }),
    isReader() {
      return this.userRole === 'user';
    },
    isAdmin() {
      return this.userRole === 'admin';
    },
  },
  methods: {
    logout() {
      this.$store.dispatch('logout');
      this.$router.push('/login');
    },
  },
};
</script>

<style scoped>
#app {
  font-family: 'Arial', sans-serif;
  background-color: #f4f4f4;
  margin: 0;
  padding: 0;
}

header {
  background-color: #343a40;
  color: white;
  padding: 15px 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

header .logo a {
  font-size: 24px;
  font-weight: bold;
  color: white;
  text-decoration: none;
}

header .nav {
  display: flex;
}

.nav-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  align-items: center; /* 确保垂直居中对齐 */
}

header .nav-list li {
  margin: 0 15px; /* 左右间距 */
}

header .nav-link {
  color: white;
  text-decoration: none;
  font-size: 18px;
}

header .nav-link:hover {
  text-decoration: underline;
}

header .logout-button {
  background-color: #dc3545;
  color: white;
  border: none;
  padding: 8px 12px;
  cursor: pointer;
}

header .logout-button:hover {
  background-color: #c82333;
}

.main-content {
  padding: 20px;
}

footer {
  background-color: #343a40;
  color: white;
  text-align: center;
  padding: 10px;
  position: fixed;
  bottom: 0;
  width: 100%;
}
</style>
