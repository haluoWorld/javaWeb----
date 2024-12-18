import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/', // 后端接口地址
  headers: {
    'Content-Type': 'application/json',
  },
});

// 添加请求拦截器，设置Authorization头
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token'); // 从localStorage获取token
  if (token) {
    config.headers['Authorization'] = `Bearer ${token}`; // 添加Authorization头
  }
  return config;
}, (error) => {
  return Promise.reject(error);
});

// 错误处理示例，定义一个统一的错误处理函数
const handleError = (error) => {
  if (error.response) {
    console.error('Error Response:', error.response);
    throw new Error(error.response.data.message || '发生了错误');
  } else if (error.request) {
    console.error('Error Request:', error.request);
    throw new Error('请求未响应，请检查网络');
  } else {
    console.error('Error Message:', error.message);
    throw new Error('发生错误：' + error.message);
  }
};

// 用户接口
export const register = (data) => api.post('/auth/register', data);
export const login = (data) => api.post('/auth/login', data);
export const logout = () => api.post('/user/logout');

// 在登录时存储 token
export const loginWithHandling = async (data) => {
  try {
    const response = await login(data);
    if (response.data && response.data.token) {
      localStorage.setItem('token', response.data.token); // 存储token
    }
    return response;
  } catch (error) {
    handleError(error);
  }
};

export const logoutWithHandling = async () => {
  try {
    await logout();
    localStorage.removeItem('token'); // 登出时移除token
  } catch (error) {
    handleError(error);
  }
};

export const getProfile = () => api.get('/user/profile');
export const updateProfile = (data) => api.put('/user/profile', data);

// 图书接口
export const getBooks = (params) => api.get('/book/list/', { params });
export const getBookById = (id) => api.get(`/book/${id}`);
export const addBook = (data) => api.post('/book/', data);
export const updateBook = (data) => api.put('/book/', data);
export const deleteBook = (id) => api.delete(`/book/${id}`);
export const searchBooks = async (query) => {
  const { title, author, isbn, page = 1, size = 10 } = query; // 结构赋值和默认值

  try {
    const response = await api.get('/book/search', {
      params: {
        title,
        author,
        isbn,
        page,
        size
      }
    });
    return response // 返回响应数据
  } catch (error) {
    console.error('图书搜索失败:', error);
    throw error; // 抛出错误以便在调用处处理
  }
};
export const getBookDetails = (id) => api.get(`/book/details/${id}`);

// 分类接口
export const getCategories = () => api.get('/category/');
export const addCategory = (data) => api.post('/category/', data);
export const deleteCategory = (id) => api.delete(`/category/${id}`);
export const getBooksByCategory = (categoryId) => api.get(`/category/${categoryId}`);
export const getCategoryByBookId = (bookId) => api.get(`/category/${bookId}`);
export const updateCategory = (data) => api.put('/category/', data);
export const addBookCategory = (data) => api.post('/category/book/', data);
export const deleteBookCategory = (data) => api.delete('/category/book/', { data });
export const getBookCategory = (bookId) => api.get(`/category/book/${bookId}`);

// 留言板接口
export const postMessage = (data) => api.post('/', data);
export const getChildrenMessages = (id) => api.get(`/message/${id}`);
export const addMessage = (data) => api.post('/message/', data);
export const deleteMessage = (id) => api.delete(`/message/${id}`);
export const getMessagesByBookId = (bookId) => api.get(`/message/list/${bookId}`);

// 公告接口
export const getAnnouncements = () => api.get('/announcement/list');
export const getAnnouncementById = (announcementId) => api.get(`/announcement/${announcementId}`);
export const addAnnouncement = (data) => api.post('/announcement/', data);
export const updateAnnouncement = (data) => api.put('/announcement/', data);
export const deleteAnnouncement = (announcementId) => api.delete(`/announcement/${announcementId}`);

// 统一错误处理的API调用
export const registerWithHandling = async (data) => {
  try {
    return await register(data);
  } catch (error) {
    handleError(error);
  }
};
