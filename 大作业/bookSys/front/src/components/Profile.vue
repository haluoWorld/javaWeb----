<template>
    <div class="form-container">
        <h2>个人信息</h2>
        <form @submit.prevent="updateProfile" class="form">
            <div class="form-group">
                <label>用户名</label>
                <input v-model="profile.username" type="text" readonly />
            </div>
            <div class="form-group">
                <label>联系方式</label>
                <input v-model="profile.contactInfo" type="text" />
                <small class="text-muted">请输入有效的手机号码或邮箱</small>
            </div>
            <div class="form-group">
                <label>修改密码</label>
                <input v-model="newPassword" type="password" placeholder="输入新密码" />
            </div>
            <button type="submit" class="submit-button">更新信息</button>
        </form>
    </div>
  </template>
  
  <script>
  import { getProfile, updateProfile } from '../utils/api';
  
  export default {
    data() {
        return {
            profile: {
                userId: '',
                username: '',
                contactInfo: '',
                password: '',
            },
            newPassword: '',
        };
    },
    async created() {
        try {
            const response = await getProfile();
            if (response.data.code === 200) {
              this.profile = {
                    userId: response.data.data.userId,
                    username: response.data.data.username,
                    contactInfo: response.data.data.contactInfo || '', // 确保联系方式是字符串
                    password: response.data.data.password, // 确保密码是字符串
                };
            } else {
                alert('获取用户信息失败：' + response.msg);
            }
        } catch (error) {
            console.error('获取用户信息时发生错误:', error);
            alert('获取用户信息失败，请重试！');
        }
    },
    methods: {
        validateContactInfo(contact) {
            const phoneRegex = /^[1][3-9][0-9]{9}$/; // 简单的手机号正则
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; // 邮箱正则
            return phoneRegex.test(contact) || emailRegex.test(contact);
        },
        async updateProfile() {
            if (!this.validateContactInfo(this.profile.contactInfo)) {
                alert('联系方式格式不正确，请输入有效的手机号码或邮箱！');
                return;
            }
            try {
                // 准备要更新的数据
                const dataToUpdate = {
                    userId: this.profile.userId,
                    username: this.profile.username,
                    contactInfo: this.profile.contactInfo,
                    password: this.newPassword || this.profile.password || '', // 如果新密码为空则不发送
                };
                await updateProfile(dataToUpdate);
                alert('更新成功！');
                this.newPassword = ''; // 清空输入框
            } catch (error) {
                alert('更新失败，请重试！');
            }
        },
    },
  };
  </script>
  
  <style scoped>
  /* 样式同 Register.vue */
  .form-container {
    padding: 20px;
  }
  .form-group {
    margin-bottom: 15px;
  }
  .submit-button {
    padding: 10px 20px;
    background-color: #28a745;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
  .submit-button:hover {
    background-color: #218838;
  }
  </style>
  