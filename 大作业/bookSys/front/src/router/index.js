import Main from '../components/MainHome.vue';
import Login from '../components/Login.vue';
import Register from '../components/Register.vue';
import Profile from '../components/Profile.vue';
import Category from '../components/Category.vue';
import Search from '../components/Search.vue';
import BookManagement from '../views/BookManagement.vue';
import CategoryManagement from '../views/CategoryManagement.vue';
import AnnouncementManagement from '../views/AnnouncementManagement.vue';
import BookDetail from '../components/BookDetail.vue';
import { createRouter, createWebHistory } from 'vue-router'
import Home from '@/views/Home.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', component: Main },
    { path: '/welcome', component: Home },
    { path: '/login', component: Login },
    { path: '/register', component: Register },
    { path: '/profile', component: Profile },
    { path: '/category', component: Category },
    { path: '/search', component: Search },
    { path: '/book-management', component: BookManagement },
    { path: '/category-management', component: CategoryManagement },
    { path: '/announcement-management', component: AnnouncementManagement },
    { path: '/book/:id', component: BookDetail }, 

  ],
})

export default router


