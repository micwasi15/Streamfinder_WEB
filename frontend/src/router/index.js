// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '@/layouts/MainLayout.vue'
import AuthLayout from '@/layouts/AuthLayout.vue'
import LoginView from '@/views/LoginView.vue'
import ShowsView from '@/views/ShowsView.vue'
import ShowDetails from '@/components/shows/ShowDetails.vue'
import PlatformStats from '@/views/PlatformStats.vue'
import PlatformPricesStats from '@/views/PlatformPricesStats.vue'

const routes = [
  {
    path: '/',
    component: MainLayout,
    children: [
      { path: '', redirect: '/search' },
      { path: 'search', name: 'Search', component: ShowsView, props: {favoritesMode: false} },
      { path: 'favorites', name: 'Favorites', component: ShowsView, props: {favoritesMode: true} },
      { path: 'shows/:id', name: 'ShowDetails', component: ShowDetails, props: true },
      { path: 'platform/stats', name: 'PlatformStats', component: PlatformStats },
      { path: 'platform/prices', name: 'PlatformPrices', component: PlatformPricesStats },
    ],
  },
  {
    path: '/login',
    component: AuthLayout,
    children: [
      { path: '', component: LoginView },
    ]
  },
]

export default createRouter({
  history: createWebHistory(),
  routes,
})
