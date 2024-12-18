<template>
    <div>
      <h2>系统公告</h2>
      <ul>
        <li v-for="announcement in announcements" :key="announcement.id">
          <h3>{{ announcement.title }}</h3>
          <p>{{ announcement.content }}</p>
        </li>
      </ul>
    </div>
  </template>
  
  <script>
  import { getAnnouncements } from '../utils/api';
  
  export default {
    data() {
      return {
        announcements: [],
      };
    },
    async created() {
      const response = await getAnnouncements();
      this.announcements = response.data;
    },
  };
  </script>
  