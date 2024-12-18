<template>
    <div class="home">
      <h1>欢迎来到图书管理系统</h1>
      <p>请您先登录</p>
    </div>
  </template>
  
  <script>
  export default {
    name: 'Home',
  };
  </script>
  
  <style scoped>
  .home {
    text-align: center;
    margin-top: 50px;
  }
  h1 {
    font-size: 32px;
    font-weight: bold;
  }
  .links {
    margin-top: 30px;
  }
  .link {
    display: inline-block;
    margin: 10px 20px;
    padding: 10px 20px;
    font-size: 18px;
    text-decoration: none;
    background-color: #007bff;
    color: white;
    border-radius: 4px;
  }
  .link:hover {
    background-color: #0056b3;
  }
  </style>
  