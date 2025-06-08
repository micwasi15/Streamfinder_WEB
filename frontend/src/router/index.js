// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import MainLayout from '@/layouts/MainLayout.vue'
import AuthLayout from '@/layouts/AuthLayout.vue'
import LoginView from '@/views/LoginView.vue'
import RegisterView from '@/views/RegisterView.vue'

const routes = [
  {
    path: '/',
    component: MainLayout,
    children: [
      { path: '', redirect: '/home' },
      { path: 'home', name: 'Home', component: HomeView },
    ],
  },
  {
    path: '/login',
    component: AuthLayout,
    children: [
      { path: '', component: LoginView },
      { path: 'register', component: RegisterView }
    ]
  },
]

export default createRouter({
  history: createWebHistory(),
  routes,
})
