// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import MainLayout from '@/layouts/MainLayout.vue'
import AuthLayout from '@/layouts/AuthLayout.vue'
import LoginView from '@/views/LoginView.vue'
import RegisterView from '@/views/RegisterView.vue'
import ShowsView from '@/views/ShowsView.vue'
import ShowDetails from '@/components/shows/ShowDetails.vue'

const routes = [
  {
    path: '/',
    component: MainLayout,
    children: [
      { path: '', redirect: '/home' },
      { path: 'home', name: 'Home', component: HomeView },
      { path: 'search', name: 'Search', component: ShowsView },
      { path: 'shows/:id', name: 'ShowDetails', component: ShowDetails, props: true },
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
