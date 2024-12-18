<template>
  <div class="announcement-management">
    <h1>公告管理</h1>
    <button class="add-button" @click="openAddModal">添加公告</button>

    <div class="announcement-list">
      <div v-for="announcement in announcements" :key="announcement.announcementId" class="announcement">
        <h2>{{ announcement.title }}</h2>
        <div v-html="announcement.content"></div>
        <p>创建时间: {{ formatDate(announcement.createTime) }}</p>
        <button @click="openEditModal(announcement)">编辑</button>
        <button @click="deleteAnnouncement(announcement.announcementId)">删除</button>
      </div>
    </div>

    <div class="modal" v-if="isModalVisible" @click.self="closeModal">
      <div class="modal-content">
        <div class="modal-header">
          <h2>{{ editingAnnouncement ? '修改公告' : '添加公告' }}</h2>
          <span class="modal-close" @click="closeModal">×</span>
        </div>
        <div class="modal-body">
          <form @submit.prevent="editingAnnouncement ? updateAnnouncement() : addAnnouncement()" class="form">
            <input type="text" v-model="title" placeholder="公告标题" required class="form-input" />
            <textarea v-model="content" placeholder="公告内容" required class="form-textarea"></textarea>
            <button type="submit" class="form-button">{{ editingAnnouncement ? '更新公告' : '添加公告' }}</button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {
  getAnnouncements,
  addAnnouncement as apiAddAnnouncement,
  updateAnnouncement as apiUpdateAnnouncement,
  deleteAnnouncement as apiDeleteAnnouncement
} from '../utils/api'; // 确保路径正确

export default {
  data() {
    return {
      announcements: [],
      newAnnouncement: {
        title: '',
        content: ''
      },
      editingAnnouncement: null,
      isModalVisible: false
    };
  },
  computed: {
    title: {
      get() {
        return this.editingAnnouncement ? this.editingAnnouncement.title : this.newAnnouncement.title;
      },
      set(value) {
        if (this.editingAnnouncement) {
          this.editingAnnouncement.title = value;
        } else {
          this.newAnnouncement.title = value;
        }
      }
    },
    content: {
      get() {
        return this.editingAnnouncement ? this.editingAnnouncement.content : this.newAnnouncement.content;
      },
      set(value) {
        if (this.editingAnnouncement) {
          this.editingAnnouncement.content = value;
        } else {
          this.newAnnouncement.content = value;
        }
      }
    }
  },
  mounted() {
    this.fetchAnnouncements();
  },
  methods: {
    async fetchAnnouncements() {
      try {
        const response = await getAnnouncements();
        this.announcements = response.data.data; // 假设响应数据的结构与上述示例一致
      } catch (error) {
        alert(error.message); // 处理错误
      }
    },
    formatDate(dateString) {
      const options = { year: 'numeric', month: '2-digit', day: '2-digit' };
      return new Date(dateString).toLocaleDateString(undefined, options);
    },
    openAddModal() {
      this.newAnnouncement = { title: '', content: '' };
      this.editingAnnouncement = null;
      this.isModalVisible = true;
    },
    openEditModal(announcement) {
      this.editingAnnouncement = { ...announcement };
      this.isModalVisible = true;
    },
    async addAnnouncement() {
      try {
        const htmlContent = this.convertToHtml(this.newAnnouncement.content);
        const data = {
          title: this.newAnnouncement.title,
          content: htmlContent
        };
        await apiAddAnnouncement(data);
        await this.fetchAnnouncements(); // 重新获取公告
        this.closeModal();
      } catch (error) {
        alert(error.message); // 处理错误
      }
    },
    async updateAnnouncement() {
      try {
        const htmlContent = this.convertToHtml(this.editingAnnouncement.content);
        const data = {
          announcementId: this.editingAnnouncement.announcementId,
          title: this.editingAnnouncement.title,
          content: htmlContent
        };
        await apiUpdateAnnouncement(data);
        await this.fetchAnnouncements(); // 重新获取公告
        this.closeModal();
      } catch (error) {
        alert(error.message); // 处理错误
      }
    },
    async deleteAnnouncement(announcementId) {
      try {
        await apiDeleteAnnouncement(announcementId);
        await this.fetchAnnouncements(); // 重新获取公告
      } catch (error) {
        alert(error.message); // 处理错误
      }
    },
    convertToHtml(content) {
      // 将文本转换为HTML格式
      return content.replace(/\n/g, '<br>'); // 示例：将换行符转换为<br>
    },
    closeModal() {
      this.isModalVisible = false;
      this.editingAnnouncement = null;
      this.newAnnouncement = { title: '', content: '' };
    }
  }
};
</script>

<style scoped>
.announcement-management {
  max-width: 800px;
  margin: auto;
  padding-bottom: 60px; /* 防止被底部内容遮挡 */
}

h1 {
  text-align: center;
}

.add-button {
  background-color: #4CAF50;
  color: white;
  padding: 10px 20px;
  border: none;
  cursor: pointer;
  margin-bottom: 20px;
}

.announcement-list {
  max-height: calc(100vh - 200px);
  overflow-y: auto;
  padding-bottom: 20px;
}

.announcement {
  border: 1px solid #ccc;
  padding: 15px;
  margin: 10px 0;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

h2 {
  margin: 0;
}

button {
  background-color: #007BFF;
  color: white;
  border: none;
  padding: 5px 10px;
  border-radius: 3px;
  cursor: pointer;
  margin-right: 5px;
}

button:hover {
  background-color: #0056b3;
}

/* Modal styles */
.modal {
  position: fixed;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  overflow: hidden;
}

.modal-content {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  background: white;
  padding: 20px;
  border-radius: 5px;
  min-width: 300px;
  width: 90%;
  max-width: 500px;
}

.modal-header {
  display: flex;
  justify-content: space-between;
}

.modal-header h2 {
  margin: 0;
}

.modal-close {
  cursor: pointer;
}

.modal-body {
  margin: 15px 0;
}

.form {
  display: flex;
  flex-direction: column;
}

.form-input,
.form-textarea {
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  width: 100%;
}

.form-textarea {
  resize: vertical;
  min-height: 100px;
}

.form-button {
  background-color: #4CAF50;
  color: white;
  border: none;
  padding: 10px;
  border-radius: 5px;
  cursor: pointer;
  align-self: flex-start;
}

.form-button:hover {
  background-color: #45a049;
}
</style>
