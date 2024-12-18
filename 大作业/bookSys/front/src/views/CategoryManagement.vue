<template>
  <div class="category-management">
    <h2>分类管理</h2>
    <div class="buttons">
      <button class="button" @click="openAddModal">添加新分类</button>
    </div>
    <table class="category-table">
      <thead>
        <tr>
          <th>分类名称</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="category in categories" :key="category.categoryId">
          <td>{{ category.categoryName }}</td>
          <td>
            <button @click="openEditModal(category)" class="btn-edit">编辑</button>
            <button @click="deleteCategory(category.categoryId)" class="btn-delete">删除</button>
          </td>
        </tr>
      </tbody>
    </table>

    <div class="modal" v-if="isModalVisible" @click.self="closeModal">
      <div class="modal-content">
        <div class="modal-header">
          <h2>{{ editingCategory ? '编辑分类' : '添加新分类' }}</h2>
          <span class="modal-close" @click="closeModal">×</span>
        </div>
        <div class="modal-body">
          <form @submit.prevent="editingCategory ? updateCategory() : addCategory()" class="form">
            <input type="text" v-model="categoryName" placeholder="分类名称" required class="form-input" />
            <button type="submit" class="form-button">{{ editingCategory ? '更新分类' : '添加分类' }}</button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getCategories, deleteCategory as apiDeleteCategory, addCategory as apiAddCategory, updateCategory as apiUpdateCategory } from '../utils/api';

export default {
  data() {
    return {
      categories: [],
      isModalVisible: false,
      categoryName: '',
      editingCategory: false,
      currentCategoryId: null,
    };
  },
  async created() {
    await this.fetchCategories();
  },
  methods: {
    async fetchCategories() {
      const response = await getCategories();
      this.categories = response.data.data; // 确保响应数据格式正确
    },
    async deleteCategory(categoryId) {
      if (confirm('确认要删除该分类吗？')) {
        try {
          await apiDeleteCategory(categoryId);
          alert('删除成功！');
          await this.fetchCategories();
        } catch (error) {
          alert('删除失败，请重试！');
        }
      }
    },
    openAddModal() {
      this.categoryName = '';  // 重新初始化输入字段
      this.editingCategory = false; // 重置编辑状态
      this.isModalVisible = true; // 显示模态框
    },
    openEditModal(category) {
      this.categoryName = category.categoryName; // 设置当前编辑的分类名称
      this.currentCategoryId = category.categoryId; // 保存当前分类ID
      this.editingCategory = true; // 设置为编辑模式
      this.isModalVisible = true; // 显示模态框
    },
    async addCategory() {
      try {
        await apiAddCategory({ categoryName: this.categoryName }); // 发送添加请求
        alert('添加成功！');
        await this.fetchCategories(); // 刷新分类列表
        this.closeModal(); // 关闭模态框
      } catch (error) {
        alert('添加失败，请重试！');
      }
    },
    async updateCategory() {
      try {
        await apiUpdateCategory({ categoryId: this.currentCategoryId, categoryName: this.categoryName }); // 发送更新请求
        alert('更新成功！');
        await this.fetchCategories(); // 刷新分类列表
        this.closeModal(); // 关闭模态框
      } catch (error) {
        alert('更新失败，请重试！');
      }
    },
    closeModal() {
      this.isModalVisible = false; // 隐藏模态框
      this.categoryName = ''; // 重置输入字段
      this.editingCategory = false; // 重置编辑状态
    },
  },
};
</script>

<style scoped>
.category-management {
  padding: 20px;
}

.buttons {
  margin-bottom: 20px;
}

.button {
  padding: 10px 20px;
  background-color: #28a745;
  color: white;
  border-radius: 4px;
  border: none;
  cursor: pointer;
}

.button:hover {
  background-color: #218838;
}

.category-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.category-table th,
.category-table td {
  padding: 10px;
  border: 1px solid #ddd;
  text-align: left;
}

.btn-edit {
  padding: 5px 10px;
  background-color: #007bff;
  color: white;
  border-radius: 4px;
  border: none;
  margin-right: 5px;
}

.btn-edit:hover {
  background-color: #0056b3;
}

.btn-delete {
  padding: 5px 10px;
  background-color: #dc3545;
  color: white;
  border-radius: 4px;
  border: none;
}

.btn-delete:hover {
  background-color: #c82333;
}

/* Modal styles */
.modal {
  position: fixed;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
}

.modal-content {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  background: white;
  padding: 20px;
  border-radius: 5px;
  width: 90%;
  max-width: 400px;
}

.modal-header {
  display: flex;
  justify-content: space-between;
}

.modal-close {
  cursor: pointer;
}

.form {
  display: flex;
  flex-direction: column;
}

.form-input {
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  width: 100%;
}

.form-button {
  background-color: #28a745;
  color: white;
  border: none;
  padding: 10px;
  border-radius: 5px;
  cursor: pointer;
}

.form-button:hover {
  background-color: #218838;
}
</style>
